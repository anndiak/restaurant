package com.application.restaurant.controller;

import com.application.restaurant.dao.MealRepository;
import com.application.restaurant.dao.OrderRepository;
import com.application.restaurant.dao.RequestRepository;
import com.application.restaurant.dao.UserRepository;
import com.application.restaurant.model.*;
import com.application.restaurant.model.dto.AddOrderDto;
import com.application.restaurant.model.dto.ChangeOrderDto;
import com.application.restaurant.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/waiter")
public class WaiterController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private OrderService orderService;

    @GetMapping("/me")
    public User getAuthenticatedUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User)auth.getPrincipal();
    }

    @RequestMapping("/homepage")
    public ModelAndView homePage() {
        return new ModelAndView("dashboards/waiter");
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllWaiters() {
        return new ResponseEntity<>(userRepository.findAllUsers(), HttpStatus.OK);
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
    public ResponseEntity<Order> addOrderToSystem(@Valid @RequestBody AddOrderDto orderDto) {
        Order order = new Order();
        order.setUserId(getAuthenticatedUser().getId());
        order.setTotalPrice(orderService.countOrderPrice(orderDto.getMealList()));
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setMealList(orderDto.getMealList());
        order.setNumOfTableOrReceiptPlace(orderDto.getNumOfTableOrReceiptPlace());
        return new ResponseEntity<>(orderRepository.addOrder(order), HttpStatus.OK);
    }

    @PutMapping("/orders/{id}")
    public ResponseEntity<Order> changeOrder(@Valid @RequestBody ChangeOrderDto orderDto, @PathVariable("id") String id) {
        if(id == null || orderDto == null ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(orderRepository.getOrderById(id) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Order changedOrder = updateOrderFields(id, orderDto);
        return new ResponseEntity<>(orderRepository.changeOrder(changedOrder),HttpStatus.OK);
    }

    @GetMapping("/requests/{id}")
    public ResponseEntity<Request> getRequest(@PathVariable("id") String id) {
        if(id == null ){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(requestRepository.getRequestById(id) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        final Request request = requestRepository.getRequestById(id);
        return new ResponseEntity<>(request,HttpStatus.OK);
    }

    @GetMapping("/meals")
    public ResponseEntity<List<Meal>> getAllMeals(){ return new ResponseEntity<>(mealRepository.getAllMeals(), HttpStatus.OK);}

    @PostMapping("/meals/add")
    public ResponseEntity<Meal> addMealToSystem(@RequestBody Meal meal) {
        return new ResponseEntity<>(mealRepository.creatMeal(meal), HttpStatus.OK);
    }

    private Order updateOrderFields(String id, ChangeOrderDto orderDto){
        Order order = orderRepository.getOrderById(id);
        order.setNumOfTableOrReceiptPlace(orderDto.getNumOfTableOrReceiptPlace());
        order.setStatus(orderDto.getStatus());
        return order;
    }
}


