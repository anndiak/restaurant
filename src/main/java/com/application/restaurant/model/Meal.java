package com.application.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@Document("meals")
public class Meal {
    @Id
    private String id;
    private String name;
    private String description;
    private double price;

}
