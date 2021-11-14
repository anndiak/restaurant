package com.application.restaurant.email_validation.appuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AppUserRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    Optional<AppUser> findByEmail(String email) {
        Query query = Query.query(Criteria.where("email").is(email));
        return Optional.ofNullable(mongoTemplate.findOne(query, AppUser.class));
    }

    public void save(AppUser appUser) {
        mongoTemplate.insert(appUser);
    }

    public void update(AppUser appUser) { mongoTemplate.save(appUser); }
}
