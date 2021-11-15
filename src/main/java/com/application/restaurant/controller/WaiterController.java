package com.application.restaurant.controller;

import com.application.restaurant.dao.OrderRepository;
import com.application.restaurant.dao.RequestRepository;
import com.application.restaurant.dao.UserRepository;
import com.application.restaurant.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.mongodb.core.query.Query;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/waiter")
public class WaiterController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/orders/add")
    public ResponseEntity<Order> addOrderToSystem(@Valid @RequestBody Order order) {
        order.setId(UUID.randomUUID().toString());
        orderRepository.addOrder(order);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/users/waiters")
    public ResponseEntity<List<User>> getAllWaiters() {
        List<User> waiters = userRepository.findAllUsersByFilter(Query.query(Criteria.where("roles").is(UserRoles.WAITER)));
        return new ResponseEntity<>(waiters, HttpStatus.OK);
    }

    @GetMapping("/orders/{waiter_id}")
    public ResponseEntity<List<Order>> getWaiterOrders(@PathVariable("waiter_id") String waiter_id) {
        return new ResponseEntity<>(getOrdersByWaiterId(waiter_id) ,HttpStatus.OK);
    }

    @GetMapping("/orders/{waiter_id}/{id}")
    public ResponseEntity<Order> getWaiterOrder(@PathVariable("waiter_id") String waiter_id, @PathVariable("id") String id) {
        Optional<Order> foundedOrder = getOrdersByWaiterId(waiter_id).stream().filter(order -> order.getId().equals(id)).findAny();
        if (foundedOrder.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(foundedOrder.get(),HttpStatus.OK);
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

    private List<Order> getOrdersByWaiterId (String waiter_id) {
        return orderRepository.getOrdersByFilter(Query.query(Criteria.where("waiterId").is(waiter_id)));
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
}


