package com.food.ordering.system.order.service.domain.dto.create;

import com.food.ordering.system.valueobject.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class CreateOrderResponse {

    @NotNull
    private final UUID orderTracking;
    @NotNull
    private final OrderStatus orderStatus;
    @NotNull
    private final String message;

}