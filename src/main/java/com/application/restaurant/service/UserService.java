package com.application.restaurant.service;

import com.application.restaurant.dao.UserRepository;
import com.application.restaurant.registration.token.ConfirmationToken;
import com.application.restaurant.registration.token.ConfirmationTokenService;

import com.application.restaurant.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG =
            "user with email %s not found";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, email)));
    }

    public void deleteUser(User user) {
        userRepository.deleteUser(user.getId());
    }

    public List<User> getAllUsers() {return userRepository.findAllUsers();}

    public String sighUpUser(User user) {
       boolean userExists = userRepository
               .findByEmail(user.getEmail())
               .isPresent();

       if (userExists) {
           throw new IllegalStateException("User with this email exists.");
       }

       String encodedPassword = bCryptPasswordEncoder
               .encode(user.getPassword());

       user.setPassword(encodedPassword);

       userRepository.create(user);
        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    public void enableUser(String email) {
        userRepository.findByEmail(email).ifPresent(u -> userRepository.update(u.enable()));
    }

}
