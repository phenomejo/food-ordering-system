package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.event.publisher.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import order.service.domain.event.OrderCreatedEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationDomainEventPublisher
        implements ApplicationContextAware,
        DomainEventPublisher<OrderCreatedEvent> {

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(OrderCreatedEvent domainEvent) {
        this.applicationEventPublisher.publishEvent(domainEvent);
        log.info("OrderCreatedEvent is published for order id: {}", domainEvent.getOrder().getId().getValue());
    }
}
