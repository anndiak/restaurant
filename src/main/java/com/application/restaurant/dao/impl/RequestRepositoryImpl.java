package com.application.restaurant.dao.impl;

import com.application.restaurant.dao.RequestRepository;
import com.application.restaurant.model.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import org.springframework.data.mongodb.core.query.Query;
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
    public List<Request> getRequestsByFilter(Query query) {
        return mt.find(query, Request.class);
    }

    @Override
    public void removeRequest(Request request) {
        mt.remove(request);
    }

    @Override
    public Request saveRequest(Request request) {
        return mt.save(request);
    }

    @Override
    public Request createRequest(Request request) {
        return mt.insert(request);
    }
}
