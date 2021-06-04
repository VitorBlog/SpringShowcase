package dev.vitorpaulo.example.controller;

import dev.vitorpaulo.example.renderer.TemplateRenderer;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@AllArgsConstructor
public class IndexController {

    private final TemplateRenderer templateRenderer;

    @GetMapping({"/", "/index"})
    public void execute(HttpServletResponse response, HttpSession session) {

        templateRenderer.render("index.jte", null, response);

    }

}
