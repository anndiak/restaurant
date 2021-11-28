package com.application.restaurant.registration.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

   private final ConfirmationTokenRepository confirmationTokenRepository;

   public void saveConfirmationToken(ConfirmationToken token) {
       confirmationTokenRepository.save(token);
   }

   public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }
    public void deleteConfirmationToken(ConfirmationToken token) { confirmationTokenRepository.delete(token); }

    public List<ConfirmationToken> getAllConfirmationTokens() { return confirmationTokenRepository.findAll(); }
}
