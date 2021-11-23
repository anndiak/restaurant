package com.application.restaurant.login;

import com.application.restaurant.dao.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public String login(String email, String password) {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(password)) {
            throw new IllegalStateException("Credentials fields should not be empty.");
        } else if (userRepository.findByEmail(email).isEmpty()) {
             throw new IllegalStateException(String.format("User with email %s does not exist.", email));
        } else if (!userRepository.findByEmail(email).get().getPassword().equals(bCryptPasswordEncoder.encode(password))) {
            throw new IllegalStateException("Wrong password.");
        } else if (!userRepository.findByEmail(email).get().getEnabled()) {
            throw new IllegalStateException("You are not enabled, please sigh up again or confirm token from your email.");
        }
        return "Login successfully";
    }
}
