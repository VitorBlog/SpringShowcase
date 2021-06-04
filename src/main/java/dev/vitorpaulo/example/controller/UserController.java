package dev.vitorpaulo.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.vitorpaulo.example.model.User;
import dev.vitorpaulo.example.process.PostProcess;
import dev.vitorpaulo.example.process.UserProcess;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserProcess userProcess;

    @GetMapping({"/api/user/{id}"})
    public Object execute(@PathVariable Long id, HttpServletResponse response) throws JsonProcessingException {

        Optional<User> user = userProcess.load(id);

        if (user.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Can't find a user with that ID.");

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        user.get().toJson(false)
                );

    }

}
