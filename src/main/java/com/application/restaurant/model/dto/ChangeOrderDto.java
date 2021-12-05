package com.application.restaurant.model.dto;

import com.application.restaurant.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeOrderDto {
    @NotNull
    private OrderStatus status;

    private String numOfTableOrReceiptPlace;
}
