package dev.vitorpaulo.example.util;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.sun.istack.Nullable;
import dev.vitorpaulo.example.model.User;
import dev.vitorpaulo.example.process.UserProcess;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AuthUtils {

    private final UserProcess userProcess;

    @Nullable
    public User loginUser(HttpSession session, String username, String password) {

        if (username == null || password == null) return null;

        User user = userProcess.loadByUsername(username).orElse(null);

        if (user == null || !BCrypt.verifyer().verify(password.toCharArray(), user.getPassword()).verified) return null;

        saveUser(session, user);

        return user;

    }

    @Nullable
    public User getUser(HttpSession session) {

        Object username = session.getAttribute("username");
        Object password = session.getAttribute("password");

        if (username == null || password == null) return null;

        User user = userProcess.loadByUsername((String) username).orElse(null);

        if (user == null || !password.equals(user.getPassword())) return null;

        return user;

    }

    public void saveUser(HttpSession session, User user) {

        session.setAttribute("username", user.getUsername());
        session.setAttribute("password", user.getPassword());

    }

}
