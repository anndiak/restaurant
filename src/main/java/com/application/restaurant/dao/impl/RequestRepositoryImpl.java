package com.application.restaurant.dao.impl;

import com.application.restaurant.dao.RequestRepository;
import com.application.restaurant.model.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RequestRepositoryImpl implements RequestRepository {

    @Autowired
    private MongoTemplate mt;

    @Override
    public List<Request> getAllRequests() {
        return mt.findAll(Request.class);
    }

    @Override
    public Request getRequestById(String id) {
        return mt.findById(id, Request.class);
    }

    @Override
    public void cancelRequest(Request request) {
        mt.remove(request);
    }

    @Override
    public void acceptRequest(Request request) {
        mt.insert(request.getOrder(), "orders");
        mt.remove(request);
    }

    @Override
    public void sentRequest(Request request) {
        mt.insert(request);
    }
}
