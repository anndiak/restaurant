package com.application.restaurant.controller;

import com.application.restaurant.dao.MealRepository;
import com.application.restaurant.model.Meal;
import com.application.restaurant.model.MealType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomePageController {

    @Autowired
    private MealRepository mealRepository;

    @RequestMapping("/")
    public String homePage(Model model) {
        List<Meal> pizzaList = mealRepository.getMealByType(MealType.PIZZA.name());
        List<Meal> drinkList = mealRepository.getMealByType(MealType.DRINK.name());
        List<Meal> dessertList = mealRepository.getMealByType(MealType.DESSERT.name());
        model.addAttribute("pizzaList", pizzaList);
        model.addAttribute("drinkList", drinkList);
        model.addAttribute("dessertList", dessertList);
        return "index";
    }

    @RequestMapping("/article")
    public String articlePage() {
        return "article";
    }
}
