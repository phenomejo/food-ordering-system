package com.food.ordering.system.order.service.domain.ports.output.repository.message.publisher.payment;

import com.food.ordering.system.event.publisher.DomainEventPublisher;
import order.service.domain.event.OrderCreatedEvent;

public interface OrderCreatedPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCreatedEvent> {

}
