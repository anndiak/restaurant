package com.application.restaurant.email_validation.registration.token;

import com.application.restaurant.email_validation.appuser.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
              return 1;
          }
        }
        return 0;
    }
}
