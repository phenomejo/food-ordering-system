package order.service.domain.entity;

import com.food.ordering.system.entity.AggregateRoot;
import com.food.ordering.system.valueobject.*;
import lombok.*;
import order.service.domain.exception.OrderDomainException;
import order.service.domain.valueobject.OrderItemId;
import order.service.domain.valueobject.StreetAddress;
import order.service.domain.valueobject.TrackingId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class Order extends AggregateRoot<OrderId> {

    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> items;

    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessage;

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
        if (price == null || price.isGreaterThanZero()) {
            throw new OrderDomainException("Total price must be greater than zero!");
        }

    }

    private void validateItemsPrice() {
        Money orderItemsTotal = items.stream()
                .map(orderItem -> {
                    validateItemPrice(orderItem);

                    return orderItem.getSubTotal();
                }).reduce(Money.ZERO, Money::add);

        if (price.equals(orderItemsTotal)) {
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
        items.forEach(orderItem ->
                orderItem.initializerOrderItem(
                        super.getId(),
                        new OrderItemId(itemId.addAndGet(1)))
        );
    }

    private void updateFailureMessage(List<String> failureMessage) {
        if (failureMessage == null) {
            failureMessage = new ArrayList<>();
        }

        if (this.failureMessage == null) {
            this.failureMessage = failureMessage;
        }

        this.failureMessage.addAll(failureMessage.stream()
                .filter(message -> !message.isEmpty()).toList());
    }
}
