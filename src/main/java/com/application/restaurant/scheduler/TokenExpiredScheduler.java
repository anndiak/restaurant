package com.application.restaurant.scheduler;

import com.application.restaurant.registration.token.ConfirmationToken;
import com.application.restaurant.registration.token.ConfirmationTokenService;
import com.application.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//@EnableScheduling
public class TokenExpiredScheduler {

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private UserService userService;

    @Scheduled(fixedRate = 60_000)
    public void scheduler() {
        List<ConfirmationToken> expiredConfirmationTokens = confirmationTokenService.getAllConfirmationTokens()
                .stream()
                .filter(token -> token.getExpiresAt().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());

        expiredConfirmationTokens.forEach(confirmationToken -> userService.deleteUser(confirmationToken.getUser()));
        expiredConfirmationTokens.forEach(confirmationToken -> confirmationTokenService.deleteConfirmationToken(confirmationToken));
    }
}
