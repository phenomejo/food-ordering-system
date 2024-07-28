package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.ports.output.repository.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import order.service.domain.event.OrderCreatedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@AllArgsConstructor
@Component
public class OrderCreateEventApplicationListener {

    private final OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher;

    @TransactionalEventListener
    public void process(OrderCreatedEvent orderCreatedEvent) {
        orderCreatedPaymentRequestMessagePublisher.publish(orderCreatedEvent);
    }
}
