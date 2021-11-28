package com.application.restaurant.registration.token;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
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

    public void delete(ConfirmationToken confirmationToken){ mongoTemplate.remove(confirmationToken);}

    public List<ConfirmationToken> findAll(){ return mongoTemplate.findAll(ConfirmationToken.class);}
}
