package com.food.ordering.system.order.service.domain.mock;

import com.food.ordering.system.order.service.domain.dto.OrderAddress;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.OrderItem;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.ProductId;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class MockFactory {


    public static final UUID CUSTOMER_ID = UUID.randomUUID();
    public static final UUID RESTAURANT_ID = UUID.randomUUID();
    public static final UUID PRODUCT_ID_1 = UUID.randomUUID();
    public static final UUID PRODUCT_ID_2 = UUID.randomUUID();
    public static final UUID ORDER_ID = UUID.randomUUID();
    public static final BigDecimal PRICE = new BigDecimal("200.00");
    public static final UUID ORDER_TRACKING_ID = UUID.randomUUID();

    public static CreateOrderCommand createOrderCommand() {
        return CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("street")
                        .postalCode("10000A")
                        .city("Paris")
                        .build())
                .price(PRICE)
                .items(List.of(OrderItem.builder()
                                .productId(PRODUCT_ID_1)
                                .quantity(1)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("50.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID_2)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()))
                .build();
    }

    public static CreateOrderCommand createOrderCommandWrongPrice() {
        return CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("street")
                        .postalCode("10000A")
                        .city("Paris")
                        .build())
                .price(new BigDecimal("250.00"))
                .items(List.of(OrderItem.builder()
                                .productId(PRODUCT_ID_1)
                                .quantity(1)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("50.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID_2)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()))
                .build();
    }

    public static CreateOrderCommand createOrderCommandWrongProductPrice() {
        return CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("street")
                        .postalCode("10000A")
                        .city("Paris")
                        .build())
                .price(new BigDecimal("250.00"))
                .items(List.of(OrderItem.builder()
                                .productId(PRODUCT_ID_1)
                                .quantity(1)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("50.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID_2)
                                .quantity(3)
                                .price(new BigDecimal("55.00"))
                                .subTotal(new BigDecimal("350.00"))
                                .build()))
                .build();
    }

    public static Customer customer() {
        return Customer.builder()
                .id(new CustomerId(CUSTOMER_ID))
                .build();
    }

    public static Restaurant restaurant() {
        return Restaurant.builder()
                .id(new RestaurantId(RESTAURANT_ID))
                .products(List.of(Product.builder()
                                .id(new ProductId(PRODUCT_ID_1))
                                .name("product_1")
                                .price(new Money(new BigDecimal("50.00")))
                                .build(),
                        Product.builder()
                                .id(new ProductId(PRODUCT_ID_2))
                                .name("product_2")
                                .price(new Money(new BigDecimal("50.00")))
                                .build()))
                .active(true)
                .build();
    }

    public static Restaurant restaurantNotActive() {
        return Restaurant.builder()
                .id(new RestaurantId(RESTAURANT_ID))
                .products(List.of(Product.builder()
                                .id(new ProductId(PRODUCT_ID_1))
                                .name("product_1")
                                .price(new Money(new BigDecimal("50.00")))
                                .build(),
                        Product.builder()
                                .id(new ProductId(PRODUCT_ID_2))
                                .name("product_2")
                                .price(new Money(new BigDecimal("50.00")))
                                .build()))
                .active(false)
                .build();
    }

    public static TrackOrderQuery trackOrderQuery() {
        return TrackOrderQuery.builder()
                .orderTrackingId(ORDER_TRACKING_ID)
                .build();
    }

}
