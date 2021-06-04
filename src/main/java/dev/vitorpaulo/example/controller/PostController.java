package dev.vitorpaulo.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.vitorpaulo.example.model.Post;
import dev.vitorpaulo.example.model.User;
import dev.vitorpaulo.example.process.PostProcess;
import dev.vitorpaulo.example.process.UserProcess;
import dev.vitorpaulo.example.process.VoteProcess;
import dev.vitorpaulo.example.util.AuthUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.Instant;

@RestController
@AllArgsConstructor
@RequestMapping
public class PostController {

    private final PostProcess postProcess;
    private final UserProcess userProcess;
    private final VoteProcess voteProcess;
    private final AuthUtils authUtils;

    @GetMapping("/api/posts")
    public Object getPosts(HttpSession session, HttpServletResponse response) {

        User user = authUtils.getUser(session);

        ObjectMapper mapper = JsonMapper.builder()
                .findAndAddModules()
                .build();

        ArrayNode posts = mapper.createArrayNode();

        postProcess.loadAll().forEach(
                post -> {

                    ObjectNode json = post.toJson();

                    if (post.getVotes() > 0 && user != null) {

                        json.put("userVote", voteProcess.loadByPostAndAuthor(post, user).isPresent());

                    }

                    posts.add(json);

                }
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        posts
                );

    }

    @PostMapping("/api/post")
    public Object publishPosts(@RequestBody ObjectNode requestBody, HttpSession session, HttpServletResponse response) {

        User user = authUtils.getUser(session);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);

        Post post = postProcess.save(
                Post
                    .builder()
                        .description(requestBody.get("description").asText())
                        .image(requestBody.has("image")? requestBody.get("image").asText() : null)
                        .votes(0)
                        .author(user)
                        .url("/post/")
                        .created(Instant.now())
                        .build()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(post.toJson());

    }

}
