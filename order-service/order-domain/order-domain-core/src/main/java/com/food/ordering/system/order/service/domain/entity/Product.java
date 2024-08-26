package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.entity.AggregateRoot;
import com.food.ordering.system.valueobject.Money;
import com.food.ordering.system.valueobject.ProductId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Product extends AggregateRoot<ProductId> {

    private String name;
    private Money price;

    public void updateWithConfirmedNameAndPrice(String name, Money price) {
        this.name = name;
        this.price = price;
    }
}
