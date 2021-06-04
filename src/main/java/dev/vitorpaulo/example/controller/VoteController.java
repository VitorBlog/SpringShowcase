package dev.vitorpaulo.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.vitorpaulo.example.model.Post;
import dev.vitorpaulo.example.model.User;
import dev.vitorpaulo.example.model.Vote;
import dev.vitorpaulo.example.process.PostProcess;
import dev.vitorpaulo.example.process.UserProcess;
import dev.vitorpaulo.example.process.VoteProcess;
import dev.vitorpaulo.example.util.AuthUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping
public class VoteController {

    private final PostProcess postProcess;
    private final UserProcess userProcess;
    private final VoteProcess voteProcess;
    private final AuthUtils authUtils;

    @PostMapping("/api/vote")
    public Object toggleVote(@RequestBody ObjectNode requestBody, HttpSession session, HttpServletResponse response) {

        User user = authUtils.getUser(session);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);

        Optional<Post> optionalPost = postProcess.loadById(requestBody.get("post").asLong());

        if (optionalPost.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);

        Post post = optionalPost.get();
        Optional<Vote> vote = voteProcess.loadByPostAndAuthor(post, user);

        if (vote.isEmpty()) {

            voteProcess.save(
                    Vote
                            .builder()
                            .post(optionalPost.get())
                            .author(user)
                            .created(Instant.now())
                            .build()
            );

            post.setVotes(post.getVotes() + 1);
            postProcess.save(post);

            return ResponseEntity.status(HttpStatus.CREATED).body(true);

        } else {

            voteProcess.delete(vote.get());

            post.setVotes(post.getVotes() - 1);
            postProcess.save(post);

            return ResponseEntity.status(HttpStatus.OK).body(true);

        }


    }

}
