package dev.vitorpaulo.example.process;

import dev.vitorpaulo.example.model.Post;
import dev.vitorpaulo.example.model.User;
import dev.vitorpaulo.example.model.Vote;
import dev.vitorpaulo.example.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteProcess {

    private final VoteRepository voteRepository;

    public List<Vote> loadByPost(Post post) {
        return voteRepository.findByPost(post);
    }

    public Optional<Vote> loadByPostAndAuthor(Post post, User author) {
        return voteRepository.findByPostAndAuthor(post, author);
    }

    public Vote save(Vote vote) {
        return voteRepository.save(vote);
    }

    public void delete(Vote vote) {
        voteRepository.delete(vote);
    }

    public void deleteByPostAndAuthor(Post post, User author) {
        voteRepository.deleteByPostAndAuthor(post, author);
    }

}
