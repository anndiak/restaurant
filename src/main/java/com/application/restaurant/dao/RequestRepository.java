package com.application.restaurant.dao;

import com.application.restaurant.model.Request;

import org.springframework.data.mongodb.core.query.Query;
import java.util.List;

public interface RequestRepository {

    List<Request> getAllRequests();

    Request getRequestById(String id);

    List<Request> getRequestsByFilter(Query query);

    void removeRequest(Request request);

    Request saveRequest(Request request);

    void createRequest(Request request);
}
