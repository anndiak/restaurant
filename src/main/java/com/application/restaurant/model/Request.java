package com.application.restaurant.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("requests")
public class Request {
    @Id
    private String id;

    private String userId;

    private  Order order;
}
