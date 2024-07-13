package order.service.domain.event;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import order.service.domain.entity.Order;

import java.time.ZonedDateTime;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class OrderCancelledEvent extends OrderEvent {

    public OrderCancelledEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);
    }
}
