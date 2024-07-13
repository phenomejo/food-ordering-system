package order.service.domain;

import com.food.ordering.system.valueobject.ProductId;
import lombok.extern.slf4j.Slf4j;
import order.service.domain.entity.Order;
import order.service.domain.entity.Product;
import order.service.domain.entity.Restaurant;
import order.service.domain.event.OrderCancelledEvent;
import order.service.domain.event.OrderCreatedEvent;
import order.service.domain.event.OrderPaidEvent;
import order.service.domain.exception.OrderDomainException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {

    public static final ZoneId ZONE_ASIA_BANGKOK = ZoneId.of("Asia/Bangkok");

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);

        order.validateOrder();
        order.initializerOrder();

        log.info("Order with id: {} is initiated", order.getId().getValue());
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZONE_ASIA_BANGKOK));
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();

        log.info("Order with id: {}, is paid", order.getId().getValue());
        return new OrderPaidEvent(order, ZonedDateTime.now(ZONE_ASIA_BANGKOK));
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();

        log.info("Order with id: {}, is approve", order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);

        log.info("Order payment is cancelling for order id: {}", order.getId().getValue());
        return new OrderCancelledEvent(order, ZonedDateTime.now(ZONE_ASIA_BANGKOK));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);

        log.info("Order with id: {} cancelled", order.getId().getValue());
    }

    private void validateRestaurant(Restaurant restaurant) {
        if (!restaurant.isActive()) {
            throw new OrderDomainException("Restaurant with id " + restaurant.getId().getValue() + " is currently not active!");
        }
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        Map<ProductId, Product> idProductMap = restaurant.getProducts().stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        order.getItems().forEach(orderItem -> {
            Product currentProduct = orderItem.getProduct();
            Product restaurantProduct = idProductMap.get(currentProduct.getId());
            if (restaurantProduct != null) {
                currentProduct.updateWithConfirmedNameAndPrice(restaurantProduct.getName(), restaurantProduct.getPrice());
            }
        });
    }
}
