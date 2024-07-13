package order.service.domain.event;

import com.food.ordering.system.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import order.service.domain.entity.Order;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
public abstract class OrderEvent implements DomainEvent<Order> {

    private final Order order;
    private final ZonedDateTime createdAt;
}
