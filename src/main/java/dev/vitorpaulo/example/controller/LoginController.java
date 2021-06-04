package dev.vitorpaulo.example.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.vitorpaulo.example.model.User;
import dev.vitorpaulo.example.process.UserProcess;
import dev.vitorpaulo.example.renderer.TemplateRenderer;
import dev.vitorpaulo.example.util.AuthUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Instant;

@Controller
@AllArgsConstructor
public class LoginController {

    private final TemplateRenderer templateRenderer;
    private final AuthUtils authUtils;
    private final UserProcess userProcess;

    @GetMapping({"/login"})
    public void login(HttpServletResponse response, HttpSession session) throws IOException {

        User user = authUtils.getUser(session);
        if (user != null) response.sendRedirect("/");

        templateRenderer.render("login.jte", null, response);

    }


    @GetMapping({"/register"})
    public void register(HttpServletResponse response, HttpSession session) throws IOException {

        User user = authUtils.getUser(session);
        if (user != null) response.sendRedirect("/");

        templateRenderer.render("register.jte", null, response);

    }

    @PostMapping({"/api/auth"})
    public Object authUser(@RequestBody ObjectNode requestBody, HttpServletResponse response, HttpSession session) {

        ObjectMapper mapper = JsonMapper.builder()
                .findAndAddModules()
                .build();

        if (authUtils.getUser(session) != null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(
                            mapper.createObjectNode()
                                    .put("success", false)
                                    .put("message", "Você já esta logado.")
                    );

        if (authUtils.loginUser(session, requestBody.get("username").asText(), requestBody.get("password").asText()) == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(
                            mapper.createObjectNode()
                                    .put("success", false)
                                    .put("message", "Login inválido.")
                    );

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        mapper.createObjectNode()
                                .put("success", true)
                                .put("message", "Bem vindo!")
                );

    }

    @PostMapping({"/api/register"})
    public Object createUser(@RequestBody ObjectNode requestBody, HttpServletResponse response, HttpSession session) {

        ObjectMapper mapper = JsonMapper.builder()
                .findAndAddModules()
                .build();

        if (authUtils.getUser(session) != null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(
                            mapper.createObjectNode()
                                    .put("success", false)
                                    .put("message", "Você já esta logado.")
                    );

        User user = User
                .builder()
                .name(requestBody.get("name").asText())
                .username(requestBody.get("username").asText())
                .email(requestBody.get("email").asText())
                .password(BCrypt.withDefaults().hashToString(10, requestBody.get("password").asText().toCharArray()))
                .created(Instant.now())
                .build();

        if (userProcess.loadByEmail(user.getEmail()).isPresent()
            || userProcess.loadByUsername(user.getUsername()).isPresent())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(
                            mapper.createObjectNode()
                                    .put("success", false)
                                    .put("message", "Conta já cadastrada.")
                    );

        userProcess.save(user);

        authUtils.saveUser(session, user);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        mapper.createObjectNode()
                                .put("success", true)
                                .put("message", "Bem vindo!")
                );

    }

}
