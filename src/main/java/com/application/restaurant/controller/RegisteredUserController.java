package com.application.restaurant.controller;

import com.application.restaurant.dao.OrderRepository;
import com.application.restaurant.dao.RequestRepository;
import com.application.restaurant.dao.UserRepository;
import com.application.restaurant.model.Order;
import com.application.restaurant.model.Request;
import com.application.restaurant.model.RequestStatus;
import com.application.restaurant.model.User;
import com.application.restaurant.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/registered_user")
public class RegisteredUserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;


    public User getAuthenticatedUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User)auth.getPrincipal();
    }


    @RequestMapping("/homepage")
    public ModelAndView homePage() {
        return new ModelAndView("dashboards/registered_user");
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        Query query = Query.query(Criteria.where("id").is(getAuthenticatedUser().getId()));
        return new ResponseEntity<>(orderRepository.getOrdersByFilter(query), HttpStatus.OK);
    }

    @GetMapping("/orders/{order_id}")
    public ResponseEntity<Order> getOrder(@PathVariable("order_id") String order_id) {
        if(order_id == null ){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(orderRepository.getOrderById(order_id) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(orderRepository.getOrderById(order_id),HttpStatus.OK);
    }

    @PostMapping("/request/new")
    public ResponseEntity<Request> makeOrderRequest(@RequestBody Order order) {
        order.setTotalPrice(orderService.countOrderPrice(order.getMealList()));
        Request request = new Request();
        request.setId(UUID.randomUUID().toString());
        request.setUserId(getAuthenticatedUser().getId());
        request.setOrder(order);
        request.setRequestStatus(RequestStatus.IN_PROGRESS);
        requestRepository.sentRequest(request);
        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @DeleteMapping("/requests/{request_id}/cancel")
    public ResponseEntity<Request> cancelOrderRequest(@PathVariable("request_id") String request_id){
        if(request_id == null ){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(requestRepository.getRequestById(request_id) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            requestRepository.cancelRequest(requestRepository.getRequestById(request_id));
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
