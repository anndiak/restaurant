package com.application.restaurant.dao.impl;

import com.application.restaurant.dao.UserRepository;
import com.application.restaurant.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private MongoTemplate mt;

    @Override
    public User create(User user) { return mt.insert(user); }

    @Override
    public User update(User user) { return mt.save(user); }

    @Override
    public void deleteUser(String userId) {
        mt.remove(findUserById(userId));
    }

    @Override
    public User findUserById(String id) {
        return mt.findById(id, User.class);
    }

    @Override
    public User findUserEmail(String email) {
        Query query = Query.query(Criteria.where("email").is(email));
        return mt.findOne(query, User.class);
    }

    @Override
    public User findUserByResetPasswordToken(String resetPasswordToken) {
            Query query = Query.query(Criteria.where("resetPasswordToken").is(resetPasswordToken));
            return mt.findOne(query, User.class);
    }

    @Override
    public List<User> findAllUsersByFilter(Query query) {
        return mt.find(query, User.class);
    }

    @Override
    public List<User> findAllUsers() {
        return mt.findAll(User.class);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Query query = Query.query(Criteria.where("email").is(email));
        return Optional.ofNullable(mt.findOne(query,User.class));
    }
}
