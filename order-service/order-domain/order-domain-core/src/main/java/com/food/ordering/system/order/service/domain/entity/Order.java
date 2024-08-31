package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.valueobject.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Data
@SuperBuilder
@NoArgsConstructor(force = true)
@EqualsAndHashCode(callSuper = true)
public class Order extends AggregateRoot<OrderId> {

    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> items;

    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    public static final String FAILURE_MESSAGE_DELIMITER = ",";

    public void initializerOrder() {
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        initializerOrderItems();
    }

    public void validateOrder() {
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    public void pay() {
        if (orderStatus != OrderStatus.PENDING) {
            throw new OrderDomainException("Order is not in correct state for pay operation!");
        }

        orderStatus = OrderStatus.PAID;
    }

    public void approve() {
        if (orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not in correct state for approve operation!");
        }

        orderStatus = OrderStatus.APPROVED;
    }

    public void initCancel(List<String> failureMessage) {
        if (orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not in correct state for initCancel operation!");
        }

        orderStatus = OrderStatus.CANCELLING;
        updateFailureMessage(failureMessage);
    }

    public void cancel(List<String> failureMessage) {
        if (!(orderStatus == OrderStatus.CANCELLING
                || orderStatus == OrderStatus.PENDING)) {
            throw new OrderDomainException("Order is not in correct state for cancel operation!");
        }

        orderStatus = OrderStatus.CANCELLED;
        updateFailureMessage(failureMessage);
    }

    private void validateInitialOrder() {
        if (orderStatus != null || getId() != null) {
            throw new OrderDomainException("Order is not in correct state for initialization!");
        }
    }

    private void validateTotalPrice() {
        if (price == null || !price.isGreaterThanZero()) {
            throw new OrderDomainException("Total price must be greater than zero!");
        }

    }

    private void validateItemsPrice() {
        Money orderItemsTotal = Optional.ofNullable(items)
                .orElse(new ArrayList<>())
                .stream()
                .map(orderItem -> {
                    validateItemPrice(orderItem);

                    return orderItem.getSubTotal();
                }).reduce(Money.ZERO, Money::add);

        if (!orderItemsTotal.equals(price)) {
            throw new OrderDomainException("Total price: " + price.amount() +
                    " is not equal to Order items total: " + orderItemsTotal.amount());
        }
    }

    private void validateItemPrice(OrderItem orderItem) {
        if (!orderItem.isPriceValid()) {
            throw new OrderDomainException("Order item price: " + orderItem.getPrice().amount() +
                    " is not valid for product: " + orderItem.getProduct().getId().getValue());
        }
    }

    private void initializerOrderItems() {
        AtomicLong itemId = new AtomicLong(1);
        Optional.ofNullable(items)
                .orElse(new ArrayList<>()).forEach(orderItem ->
                orderItem.initializerOrderItem(
                        super.getId(),
                        new OrderItemId(itemId.addAndGet(1)))
        );
    }

    private void updateFailureMessage(List<String> failureMessage) {
        if (failureMessage == null) {
            failureMessage = new ArrayList<>();
        }

        if (this.failureMessages == null) {
            this.failureMessages = failureMessage;
        }

        this.failureMessages.addAll(failureMessage.stream()
                .filter(message -> !message.isEmpty()).toList());
    }
}
