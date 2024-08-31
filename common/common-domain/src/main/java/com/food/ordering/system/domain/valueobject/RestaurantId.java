package com.food.ordering.system.domain.valueobject;

import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
public class RestaurantId extends BaseId<UUID> {
    public RestaurantId(UUID value) {
        super(value);
    }
}
