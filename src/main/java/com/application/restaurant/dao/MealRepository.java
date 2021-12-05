package com.application.restaurant.dao;

import com.application.restaurant.model.Meal;

import java.util.List;

public interface MealRepository {

    List<Meal> getAllMeals();

    Meal getMealById(String id);

    Meal creatMeal(Meal meal);

    Meal updateMeal(Meal meal);

    void deleteMeal(String id);
}
