package com.application.restaurant.dao.impl;

import com.application.restaurant.dao.OrderRepository;
import com.application.restaurant.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderRepositoryImpl implements OrderRepository {

    @Autowired
    private MongoTemplate mt;

    @Override
    public List<Order> getAllOrders() {
        return mt.findAll(Order.class);
    }

    @Override
    public List<Order> getOrdersByFilter(Query query) { return mt.find(query, Order.class); }

    @Override
    public Order getOrderById(String id) {
        return mt.findById(id, Order.class);
    }

    @Override
    public Order addOrder(Order order) {
        LocalDateTime now = LocalDateTime.now();
        order.setCreatedAt(now);
        order.setUpdatedAt(now);
        return mt.insert(order);
    }

    @Override
    public Order changeOrder(Order order) {
        order.setUpdatedAt(LocalDateTime.now());
        return mt.save(order);
    }

    @Override
    public void deleteOrderById(String id) {
        mt.remove(mt.findById(id, Order.class));
    }
}
