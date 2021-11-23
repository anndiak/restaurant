package com.application.restaurant.service;

import com.application.restaurant.model.Meal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    public double countOrderPrice(List<Meal> mealList) {
        return mealList.stream().mapToDouble(Meal::getPrice).sum();
    }
}
