package com.food.ordering.system.domain.valueobject;

import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
public class CustomerId extends BaseId<UUID> {
    public CustomerId(UUID value) {
        super(value);
    }
}
