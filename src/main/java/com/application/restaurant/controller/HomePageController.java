package com.application.restaurant.controller;

import com.application.restaurant.dao.MealRepository;
import com.application.restaurant.model.Meal;
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
        List<Meal> mealList = mealRepository.getAllMeals();
        model.addAttribute("mealList", mealList);
        return "index";
    }

    @RequestMapping("/article")
    public String articlePage() {
        return "article";
    }
}
