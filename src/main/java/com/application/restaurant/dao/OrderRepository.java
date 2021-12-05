package com.application.restaurant.dao;

import com.application.restaurant.model.Order;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public interface OrderRepository {

    List<Order> getAllOrders();

    List<Order> getOrdersByFilter(Query query);

    Order getOrderById(String id);

    Order addOrder(Order order);

    Order changeOrder(Order order);

    void deleteOrderById(String id);
}
