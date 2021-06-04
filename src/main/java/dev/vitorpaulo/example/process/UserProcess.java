package dev.vitorpaulo.example.process;

import dev.vitorpaulo.example.model.User;
import dev.vitorpaulo.example.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserProcess {

    private final UserRepository userRepository;

    public Optional<User> load(Long id) { return userRepository.findById(id); }

    public Optional<User> loadByName(String name) { return userRepository.findByName(name); }

    public Optional<User> loadByUsername(String name) { return userRepository.findByUsername(name); }

    public Optional<User> loadByEmail(String email) { return userRepository.findByEmail(email); }

    public List<User> loadAll() { return userRepository.findAll(); }

    public void save(User user) { userRepository.save(user); }

}
