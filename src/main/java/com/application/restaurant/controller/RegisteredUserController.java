package com.application.restaurant.controller;

import com.application.restaurant.dao.MealRepository;
import com.application.restaurant.dao.OrderRepository;
import com.application.restaurant.dao.RequestRepository;
import com.application.restaurant.dao.UserRepository;
import com.application.restaurant.model.*;
import com.application.restaurant.model.dto.AddOrderDto;
import com.application.restaurant.service.OrderService;
import org.bson.types.ObjectId;
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

@RestController
@RequestMapping(path = "api/v1/registered_user")
public class RegisteredUserController {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MealRepository mealRepository;


    @GetMapping("/me")
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
        Query query = Query.query(Criteria.where("userId").is(getAuthenticatedUser().getId()));
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

    @GetMapping("/requests")
    public ResponseEntity<List<Request>> getAllRequests() {
        Query query = Query.query(Criteria.where("userId").is(getAuthenticatedUser().getId()));
        return new ResponseEntity<>(requestRepository.getRequestsByFilter(query), HttpStatus.OK);
    }

    @PostMapping("/requests/add")
    public ResponseEntity<Request> makeOrderRequest(@RequestBody AddOrderDto addOrderDto) {
        Order order = new Order();
        order.setUserId(getAuthenticatedUser().getId());
        order.setMealList(addOrderDto.getMealList());
        order.setNumOfTableOrReceiptPlace(addOrderDto.getNumOfTableOrReceiptPlace());
        order.setTotalPrice(orderService.countOrderPrice(addOrderDto.getMealList()));
        order.setStatus(OrderStatus.IN_PROGRESS);
        Request request = new Request();
        request.setUserId(getAuthenticatedUser().getId());
        request.setOrder(order);
        request.setRequestStatus(RequestStatus.IN_PROGRESS);
        requestRepository.createRequest(request);
        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @GetMapping("/meals")
    public ResponseEntity<List<Meal>> getAllMeals(){ return new ResponseEntity<>(mealRepository.getAllMeals(), HttpStatus.OK);}

    @PostMapping("/meals/add")
    public ResponseEntity<Meal> addMealToSystem(@RequestBody Meal meal) {
        return new ResponseEntity<>(mealRepository.creatMeal(meal), HttpStatus.OK);
    }
}
