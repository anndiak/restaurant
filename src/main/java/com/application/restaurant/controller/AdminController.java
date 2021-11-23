package com.application.restaurant.controller;

import com.application.restaurant.dao.OrderRepository;
import com.application.restaurant.dao.RequestRepository;
import com.application.restaurant.dao.UserRepository;
import com.application.restaurant.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAllUsers();
    }

    @PostMapping("/users/add")
    public ResponseEntity<User> addUserToSystem(@Valid @RequestBody User user) {
        user.setId(UUID.randomUUID().toString());
        userRepository.create(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/users/{id}/delete")
    public ResponseEntity<User> deleteUserFromSystem(@PathVariable("id") String id) {
        if(id == null ){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(userRepository.findUserById(id) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userRepository.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "users/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") String id) {
        if(id == null ){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(userRepository.findUserById(id) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        final User user = userRepository.findUserById(id);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @DeleteMapping(path = "users/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") String id) {
        if(id == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(userRepository.findUserById(id) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(userRepository.findUserById(id).getUserRoles() == UserRoles.ADMIN){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userRepository.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/requests")
    public List<Request> getAllRequests() {
        return requestRepository.getAllRequests();
    }

    @GetMapping("/requests/{id}")
    public ResponseEntity<Request> getRequest(@PathVariable("id") String id) {
        if(id == null ){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(requestRepository.getRequestById(id) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        final Request request = requestRepository.getRequestById(id);;
        return new ResponseEntity<>(request,HttpStatus.OK);
    }

    @PostMapping("/requests/{id}/accept")
    public ResponseEntity<HttpStatus> acceptRequest(@PathVariable("id") String id) {
        if(id == null ){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(requestRepository.getRequestById(id) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        requestRepository.acceptRequest(requestRepository.getRequestById(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/requests/{id}")
    public ResponseEntity<Request> cancelRequest(@PathVariable("id") String id) {
        if(id == null ){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(requestRepository.getRequestById(id) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        requestRepository.cancelRequest(requestRepository.getRequestById(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        return new ResponseEntity<>(orderRepository.getAllOrders(),HttpStatus.OK);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable("id") String id) {
        if(id == null ){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(requestRepository.getRequestById(id) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(orderRepository.getOrderById(id),HttpStatus.OK);
    }

    @PostMapping("/orders/add")
    public ResponseEntity<Order> addOrderToSystem(@Valid @RequestBody Order order) {
        order.setId(UUID.randomUUID().toString());
        orderRepository.addOrder(order);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PostMapping("/orders/{id}/{status}")
    public ResponseEntity<Order> changeStatusOfOrder(@PathVariable("id") String id, @PathVariable("status") String orderStatus) {
        if(id == null || orderStatus == null || orderStatus.equals("") ||
                (!orderStatus.equalsIgnoreCase(OrderStatus.IN_PROGRESS.name()) && !orderStatus.equalsIgnoreCase(OrderStatus.DONE.name()))){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(requestRepository.getRequestById(id) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Order order;
        if (orderStatus.equalsIgnoreCase(OrderStatus.IN_PROGRESS.name())) {
            order = orderRepository.changeOrderStatus(id,OrderStatus.IN_PROGRESS);
        } else {
            order = orderRepository.changeOrderStatus(id, OrderStatus.DONE);
        }

        return new ResponseEntity<>(order,HttpStatus.OK);
    }


}
