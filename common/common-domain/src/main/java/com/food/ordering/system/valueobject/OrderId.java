package com.food.ordering.system.valueobject;

import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
public class OrderId extends BaseId<UUID> {
    public OrderId(UUID value) {
        super(value);
    }
}
