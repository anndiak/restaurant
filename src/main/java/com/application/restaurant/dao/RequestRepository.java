package com.application.restaurant.dao;

import com.application.restaurant.model.Request;

import java.util.List;

public interface RequestRepository {

    List<Request> getAllRequests();

    Request getRequestById(String id);

    void cancelRequest(Request request);

    void acceptRequest(Request request);

    void sentRequest(Request request);
}
