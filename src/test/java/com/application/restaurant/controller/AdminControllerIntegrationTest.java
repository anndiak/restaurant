package com.application.restaurant.controller;

import com.application.restaurant.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@AutoConfigureDataMongo
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdminControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void beforeEach() {
        mongoTemplate.dropCollection("users");
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void getAllUsersTest() throws Exception
    {
        User user = new User("Ivan", "Antonych", "iv.antonych@gmail.com",
                "1234567", UserRoles.UNREGISTERED_USER);
        user.setId("1");
        mongoTemplate.save(user);
        mvc.perform( MockMvcRequestBuilders
                .get("/api/v1/admin/users")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").exists())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void addUserToSystemTest() throws Exception
    {
        mvc.perform( MockMvcRequestBuilders
                .post("/api/v1/admin/users/add")
                .content("{\"firstName\": \"Daniel\",\"lastName\": \"Croft\",\"email\": \"dan@gmail.com\",\"password\": \"password\",\"userRoles\": \"ADMIN\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").exists())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void getUserTest() throws Exception
    {
        User user = new User("Ivan", "Antonych", "iv.antonych@gmail.com",
                "1234567", UserRoles.UNREGISTERED_USER);
        user.setId("1");
        mongoTemplate.save(user);
        mvc.perform( MockMvcRequestBuilders
                .get("/api/v1/admin/users/1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").exists())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void getNotExistsUserTest() throws Exception
    {
        mvc.perform( MockMvcRequestBuilders
                .get("/api/v1/admin/users/1"))
                .andExpect(status().is(404));
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void deleteUserFromSystemTest() throws Exception
    {
        User user = new User("Ivan", "Antonych", "iv.antonych@gmail.com",
                "1234567", UserRoles.UNREGISTERED_USER);
        user.setId("1");
        mongoTemplate.save(user);
        mvc.perform( MockMvcRequestBuilders
                .delete("/api/v1/admin/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void deleteNotExistsUserFromSystemTest() throws Exception
    {
        mvc.perform( MockMvcRequestBuilders
                .delete("/api/v1/admin/users/1"))
                .andExpect(status().is(404));
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void changeStatusOfOrderTest() throws Exception
    {
        Order order = new Order();
        order.setId("1");
        order.setUserId("test");
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setTotalPrice(45.0);
        order.setMealList(new ArrayList<>());
        mongoTemplate.save(order);
        mvc.perform( MockMvcRequestBuilders
                .put("/api/v1/admin/orders/1/" + OrderStatus.DONE)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void addOrderToSystemTest() throws Exception
    {
        Order order = new Order();
        order.setId("1");
        order.setUserId("test");
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setTotalPrice(45.0);
        List<Meal> mealList = new ArrayList<>();
        mealList.add(new Meal("1","meal", "some meal", 3.4));
        order.setMealList(mealList);
        order.setWaiterId("test");

        mvc.perform( MockMvcRequestBuilders
                .post("/api/v1/admin/orders/add")
                .content(writeJsonAsString(order))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void getAllOrdersTest() throws Exception
    {
        Order order = new Order();
        order.setId("1");
        order.setUserId("test");
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setTotalPrice(45.0);
        List<Meal> mealList = new ArrayList<>();
        mealList.add(new Meal("1","meal", "some meal", 3.4));
        order.setMealList(mealList);
        order.setWaiterId("test");

        mongoTemplate.save(order);
        mvc.perform( MockMvcRequestBuilders
                .get("/api/v1/admin/orders")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void cancelRequestTest() throws Exception
    {
        Order order = new Order();
        order.setId("1");
        order.setUserId("test");
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setTotalPrice(45.0);
        List<Meal> mealList = new ArrayList<>();
        mealList.add(new Meal("1","meal", "some meal", 3.4));
        order.setMealList(mealList);
        order.setWaiterId("test");

        Request request = new Request();
        request.setId("1");
        request.setRequestStatus(RequestStatus.IN_PROGRESS);
        request.setUserId("test_id");
        request.setOrder(order);

        mongoTemplate.save(request);
        mvc.perform( MockMvcRequestBuilders
                .delete("/api/v1/admin/requests/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void cancelNotExistsRequestTest() throws Exception
    {
        mvc.perform( MockMvcRequestBuilders
                .delete("/api/v1/admin/requests/1"))
                .andExpect(status().is(404));
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void acceptNotExistsRequestTest() throws Exception
    {
        mvc.perform( MockMvcRequestBuilders
                .put("/api/v1/admin/requests/1/accept"))
                .andExpect(status().is(404));
    }

    private String writeJsonAsString(Object object) {
        try {
           return new ObjectMapper().writeValueAsString(object);
        }catch (Exception e) {
            return null;
        }
    }




}
