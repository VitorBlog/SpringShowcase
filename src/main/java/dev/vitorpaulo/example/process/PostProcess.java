package dev.vitorpaulo.example.process;

import dev.vitorpaulo.example.model.Post;
import dev.vitorpaulo.example.model.User;
import dev.vitorpaulo.example.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostProcess {

    private final PostRepository postRepository;

    public Optional<Post> loadById(Long id) { return postRepository.findById(id); }

    public Optional<Post> loadByUrl(String url) { return postRepository.findByUrl(url); }

    public List<Post> loadByAuthor(User author) { return postRepository.findByAuthor(author); }

    public List<Post> loadAll() { return postRepository.findAll(Sort.by(Sort.Direction.ASC, "created")); }

    public Post save(Post post) { return postRepository.save(post); }

}
