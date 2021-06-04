package dev.vitorpaulo.example.repository;

import dev.vitorpaulo.example.model.Post;
import dev.vitorpaulo.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByUrl(String url);

    List<Post> findByAuthor(User author);

}