package com.application.restaurant.registration.token;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ConfirmationTokenRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    Optional<ConfirmationToken> findByToken(String token) {
        Query query = Query.query(Criteria.where("token").is(token));
        return Optional.ofNullable(mongoTemplate.findOne(query, ConfirmationToken.class));
    }

    public void save (ConfirmationToken confirmationToken) {
        mongoTemplate.insert(confirmationToken);
    }

    public int updateConfirmedAt (String token,
                                  LocalDateTime confirmedAt) {
        Optional<ConfirmationToken> confirmationToken = findByToken(token);
        if (confirmationToken.isPresent()) {
          boolean isValid = confirmationToken.get().getExpiresAt().isAfter(confirmedAt);
          if (isValid) {
              mongoTemplate.save(confirmationToken.get().updateConfirmedAt(LocalDateTime.now()));
              return 1;
          }
        }
        return 0;
    }
}
