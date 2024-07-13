package com.food.ordering.system.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@EqualsAndHashCode
public abstract class BaseId<T> {

    protected BaseId(T value) {
        this.value = value;
    }

    private final T value;
}
