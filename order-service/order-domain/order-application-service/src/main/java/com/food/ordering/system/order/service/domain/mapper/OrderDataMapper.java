package com.food.ordering.system.order.service.domain.mapper;

import com.food.ordering.system.order.service.domain.dto.OrderAddress;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.ordering.system.valueobject.CustomerId;
import com.food.ordering.system.valueobject.Money;
import com.food.ordering.system.valueobject.ProductId;
import com.food.ordering.system.valueobject.RestaurantId;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderDataMapper {

    public Restaurant creOrderCommandToRestaurant(CreateOrderCommand createOrderCommand) {
        return Restaurant.builder()
                .id(RestaurantId.builder()
                        .value(createOrderCommand.getRestaurantId())
                        .build())
                .products(createOrderCommand.getItems()
                        .stream()
                        .map(orderItem -> Product.builder()
                                .id(ProductId.builder()
                                        .value(orderItem.getProductId())
                                        .build())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand) {
        return Order.builder()
                .customerId(CustomerId.builder()
                        .value(createOrderCommand.getCustomerId())
                        .build())
                .restaurantId(RestaurantId.builder()
                        .value(createOrderCommand.getRestaurantId())
                        .build())
                .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.getAddress()))
                .price(new Money(createOrderCommand.getPrice()))
                .items(orderItemsToOrderItemEntities(createOrderCommand.getItems()))
                .build();

    }

    public CreateOrderResponse orderTOCreateOrderResponse(Order order, String message) {
        return CreateOrderResponse.builder()
                .orderTracking(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .message(message)
                .build();
    }

    private List<OrderItem> orderItemsToOrderItemEntities(
            List<com.food.ordering.system.order.service.domain.dto.create.OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> OrderItem.builder()
                        .product(Product.builder()
                                .id(ProductId.builder()
                                        .value(orderItem.getProductId())
                                        .build())
                                .build())
                        .price(new Money(orderItem.getPrice()))
                        .quantity(orderItem.getQuantity())
                        .subTotal(new Money(orderItem.getSubtotal()))
                        .build())
                .collect(Collectors.toList());
    }

    private StreetAddress orderAddressToStreetAddress(OrderAddress address) {
        return StreetAddress.builder()
                .id(UUID.randomUUID())
                .city(address.getCity())
                .street(address.getStreet())
                .postCode(address.getPostalCode())
                .build();
    }

    public TrackOrderResponse orderToTrackOrderResponse(Order order) {
        return TrackOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages())
                .build();
    }
}
