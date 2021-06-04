package dev.vitorpaulo.example.repository;

import dev.vitorpaulo.example.model.Post;
import dev.vitorpaulo.example.model.User;
import dev.vitorpaulo.example.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    List<Vote> findByPost(Post post);

    Optional<Vote> findByPostAndAuthor(Post post, User author);

    void deleteByPostAndAuthor(Post post, User author);

}