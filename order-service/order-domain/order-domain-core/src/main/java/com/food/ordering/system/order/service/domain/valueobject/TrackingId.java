package com.food.ordering.system.order.service.domain.valueobject;

import com.food.ordering.system.valueobject.BaseId;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

import java.util.UUID;


@SuperBuilder
public class TrackingId extends BaseId<UUID> {
    public TrackingId(UUID value) {
        super(value);
    }
}
