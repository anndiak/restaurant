package com.application.restaurant.dao;

import com.application.restaurant.model.User;
import com.mongodb.client.result.DeleteResult;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public interface UserRepository{

    User addUser(User user);

    DeleteResult deleteUser(String userId);

    User findUserById(String id);

    User findUserEmail(String email);

    List<User> findAllUsersByFilter(Query query);

    List<User> findAllUsers();
}
