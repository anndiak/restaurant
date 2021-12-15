package com.application.restaurant.dao.impl;

import com.application.restaurant.dao.MealRepository;
import com.application.restaurant.model.Meal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MealRepositoryImpl implements MealRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Meal> getAllMeals() {
        return mongoTemplate.findAll(Meal.class);
    }

    @Override
    public Meal getMealById(String id) {
        return mongoTemplate.findById(id, Meal.class);
    }

    @Override
    public List<Meal> getMealByType(String type) {
        Query query = Query.query(Criteria.where("mealType").is(type));
        return mongoTemplate.find(query, Meal.class);
    }

    @Override
    public Meal creatMeal(Meal meal) {
        return mongoTemplate.insert(meal);
    }

    @Override
    public Meal updateMeal(Meal meal) {
        return mongoTemplate.save(meal);
    }

    @Override
    public void deleteMeal(String id) {
        mongoTemplate.remove(getMealById(id));
    }
}
