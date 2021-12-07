package com.application.restaurant.controller;

import com.application.restaurant.dao.MealRepository;
import com.application.restaurant.dao.OrderRepository;
import com.application.restaurant.dao.RequestRepository;
import com.application.restaurant.dao.UserRepository;
import com.application.restaurant.model.*;
import com.application.restaurant.model.dto.AddOrderDto;
import com.application.restaurant.model.dto.ChangeOrderDto;
import com.application.restaurant.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/admin")
public class AdminController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MealRepository mealRepository;

    public User getAuthenticatedUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User)auth.getPrincipal();
    }

    @PostMapping("/test")
    public void test() {
        Order order = orderRepository.getOrderById("61acd12d4a1a64087dee6221");
        Request request = new Request();
        request.setUserId(order.getUserId());
        request.setOrder(order);
        request.setRequestStatus(RequestStatus.IN_PROGRESS);
        requestRepository.createRequest(request);
    }

    @RequestMapping("/homepage")
    public ModelAndView homePage() {
        return new ModelAndView("dashboards/admin");
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAllUsers();
    }

    @PostMapping("/users/add")
    public ResponseEntity<User> addUserToSystem(@Valid @RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.create(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
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

    @PutMapping(path = "users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") String id, @RequestBody User user) {
        if(id == null || user == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(userRepository.findUserById(id) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User updatedUser = updateUserFields(id, user);
        return new ResponseEntity<>(userRepository.update(updatedUser), HttpStatus.OK);
    }

    @DeleteMapping(path = "users/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") String id) {
        if(id == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(userRepository.findUserById(id) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(userRepository.findUserById(id).getUserRoles() == UserRoles.ROLE_ADMIN){
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

    @PutMapping("/requests/{id}/accept")
    public ResponseEntity<Request> acceptOrderRequest(@PathVariable("id") String id){
        if(id == null ){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Request request = requestRepository.getRequestById(id);

        if(request == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        request.setRequestStatus(RequestStatus.ACCEPTED);
        requestRepository.saveRequest(request);

        orderRepository.addOrder(request.getOrder());
        return new ResponseEntity<>(request,HttpStatus.OK);
    }

    @PutMapping("/requests/{id}/cancel")
    public ResponseEntity<Request> cancelOrderRequest(@PathVariable("id") String id){
        if(id == null ){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Request request = requestRepository.getRequestById(id);

        if(request == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        request.setRequestStatus(RequestStatus.CANCELLED);
        requestRepository.saveRequest(request);
        return new ResponseEntity<>(request,HttpStatus.OK);
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

    @GetMapping("/meals")
    public ResponseEntity<List<Meal>> getAllMeals(){ return new ResponseEntity<>(mealRepository.getAllMeals(), HttpStatus.OK);}

    @PostMapping("/meals/add")
    public ResponseEntity<Meal> addMealToSystem(@RequestBody Meal meal) {
        return new ResponseEntity<>(mealRepository.creatMeal(meal), HttpStatus.OK);
    }

    private User updateUserFields(String id, User newUser){
        User user = userRepository.findUserById(id);
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setEmail(newUser.getEmail());
        user.setPhoneNumber(newUser.getPhoneNumber());
        user.setEnabled(newUser.getEnabled());
        return user;
    }

    private Order updateOrderFields(String id, ChangeOrderDto orderDto){
        Order order = orderRepository.getOrderById(id);
        order.setNumOfTableOrReceiptPlace(orderDto.getNumOfTableOrReceiptPlace());
        order.setStatus(orderDto.getStatus());
        return order;
    }
}
