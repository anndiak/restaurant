package com.application.restaurant.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Document("orders")
public class Order {

    @Id
    private String id;

    private String userId;

    @NotNull
    @NotEmpty
    private List<Meal> mealList;

    @NotNull
    @NotEmpty
    private OrderStatus status;

    private String waiterId;

}

