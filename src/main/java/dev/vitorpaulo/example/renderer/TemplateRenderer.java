package dev.vitorpaulo.example.renderer;

import gg.jte.CodeResolver;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.output.Utf8ByteOutput;
import gg.jte.resolve.DirectoryCodeResolver;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * SpringBoot integration with JTE.
 *
 * @author casid
 * @author benjamin-thomas
 */
@Service
public class TemplateRenderer {

    private final Boolean devMode = true;
    private final TemplateEngine templateEngine;

    public TemplateRenderer() {
        if (devMode) {
            CodeResolver codeResolver = new DirectoryCodeResolver(Paths.get("src/main/resources/templates/"));
            templateEngine = TemplateEngine.create(codeResolver, Paths.get("jte-classes"), ContentType.Html, getClass().getClassLoader());
            templateEngine.setBinaryStaticContent(true);
        } else {
            templateEngine = TemplateEngine.createPrecompiled(ContentType.Html);
        }
    }

    public void render(String name, Object model, HttpServletResponse response) {

        render(name, new HashMap<>() {
            {
                put("model", model);
            } }, response);

    }

    public void render(String name, Map<String, Object> model, HttpServletResponse response) {

        Utf8ByteOutput output = new Utf8ByteOutput();
        templateEngine.render(name, model, output);

        response.setContentType("text/html");
        response.setContentLength(output.getContentLength());

        try {
            output.writeTo(response.getOutputStream());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

}
