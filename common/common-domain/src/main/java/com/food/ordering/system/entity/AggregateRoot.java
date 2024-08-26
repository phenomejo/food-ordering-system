package com.food.ordering.system.entity;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public abstract class AggregateRoot<ID> extends BaseEntity<ID> {
}
