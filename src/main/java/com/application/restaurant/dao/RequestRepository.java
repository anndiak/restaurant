package com.application.restaurant.dao;

import com.application.restaurant.model.Request;

import java.util.List;

public interface RequestRepository {

    List<Request> getAllRequests();

    Request getRequestById(String id);

    void removeRequest(Request request);

    Request saveRequest(Request request);

    void createRequest(Request request);
}
