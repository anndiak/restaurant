package com.application.restaurant.dao;

import com.application.restaurant.model.User;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository{

    void deleteUser(String userId);

    User findUserById(String id);

    User findUserEmail(String email);

    User findUserByResetPasswordToken(String resetPasswordToken);

    List<User> findAllUsersByFilter(Query query);

    List<User> findAllUsers();

    Optional<User> findByEmail(String email);

    User create(User user);

    User update(User user);
}
