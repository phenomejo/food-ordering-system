package com.food.ordering.system.domain.valueobject;

import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
public class ProductId extends BaseId<UUID> {
    public ProductId(UUID value) {
        super(value);
    }
}