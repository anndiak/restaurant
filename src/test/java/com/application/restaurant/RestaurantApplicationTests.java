package com.application.restaurant;

import com.application.restaurant.dao.UserRepository;
import com.application.restaurant.model.User;
import com.application.restaurant.model.UserRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class RestaurantApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @BeforeEach
    void beforeEach() {
        mongoTemplate.dropCollection("users");
    }

    @Test
    void addUserToDbTest() {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setFirstName("Ivan");
        user.setLastName("Antonych");
        user.setEmail("iv.antonych@gmail.com");
        user.setPassword("1234567");
        assertEquals(userRepository.create(user), user);
    }

    @Test()
    void findUserByEmailInDbTest() {
        User user = new User("Ivan", "Antonych", "iv.antonych@gmail.com",
                "1234567", UserRoles.ROLE_REGISTERED_USER);
        user.setId(UUID.randomUUID().toString());
        userRepository.create(user);
        User foundedUser = userRepository.findByEmail(user.getEmail()).get();
        assertEquals(user.getId(), foundedUser.getId());
    }

    @Test
    void updateUserFieldInDbTest(){
        User user = new User("Ivan", "Antonych", "iv.antonych@gmail.com",
                "1234567", UserRoles.ROLE_REGISTERED_USER);
        userRepository.create(user);
        user.setPassword("2345");
        userRepository.update(user);
        User foundedUser = userRepository.findByEmail(user.getEmail()).get();
        assertEquals(foundedUser.getPassword(), user.getPassword());
    }

    @Test
    void deleteUserFromDbTest(){
        User user = new User("Ivan", "Antonych", "iv.antonych@gmail.com",
                "1234567", UserRoles.ROLE_REGISTERED_USER);
        user.setId(UUID.randomUUID().toString());
        assertNotNull(userRepository.create(user));
        userRepository.deleteUser(user.getId());
        assertNull(userRepository.findUserById(user.getId()));
    }

    @Test
    void findAllUsersTest(){
        User user1 = new User("Ivan", "Antonych", "iv.antonych@gmail.com",
                "1234567", UserRoles.ROLE_REGISTERED_USER);
        User user2 = new User("Taras", "Romanchuk", "taras.roman@gmail.com",
                "12sdf567", UserRoles.ROLE_REGISTERED_USER);
        User user3 = new User("Volodymir", "Stepaniuk", "vova.step@gmail.com",
                "646yhc567", UserRoles.ROLE_REGISTERED_USER);
        userRepository.create(user1);
        userRepository.create(user2);
        userRepository.create(user3);
        assertEquals(mongoTemplate.findAll(User.class).size(), 3);
    }

    @Test()
    void findUserByIdTest() {
        User user = new User("Ivan", "Antonych", "iv.antonych@gmail.com",
                "1234567", UserRoles.ROLE_REGISTERED_USER);
        user.setId(UUID.randomUUID().toString());
        userRepository.create(user);
        assertEquals(userRepository.findUserById(user.getId()), user);
    }


}
