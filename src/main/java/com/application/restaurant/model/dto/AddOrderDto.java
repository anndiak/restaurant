package com.application.restaurant.model.dto;

import com.application.restaurant.model.Meal;
import com.application.restaurant.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddOrderDto {
    @NotNull
    @NotEmpty
    private List<Meal> mealList;

    @NotNull
    private String numOfTableOrReceiptPlace;
}
