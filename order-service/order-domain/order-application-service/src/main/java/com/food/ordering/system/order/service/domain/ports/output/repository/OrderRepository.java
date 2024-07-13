package com.food.ordering.system.order.service.domain.ports.output.repository;

import order.service.domain.entity.Order;
import order.service.domain.valueobject.TrackingId;

import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findByTrackingId(TrackingId trackingId);

}
