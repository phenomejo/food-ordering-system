package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.entity.AggregateRoot;
import com.food.ordering.system.valueobject.Money;
import com.food.ordering.system.valueobject.OrderId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class OrderItem extends AggregateRoot<OrderItemId> {

    private OrderId orderId;

    private final Product product;
    private final Integer quantity;
    private final Money price;
    private final Money subTotal;

    public boolean isPriceValid() {
        return price.isGreaterThanZero()
                && price.equals(product.getPrice())
                && price.multiply(quantity).equals(subTotal);
    }

    public void initializerOrderItem(OrderId orderId, OrderItemId orderItemId) {
        this.orderId = orderId;
        super.setId(orderItemId);
    }
}
