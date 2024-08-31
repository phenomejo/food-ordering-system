package com.food.ordering.system.domain.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode
@NoArgsConstructor
public abstract class BaseEntity<ID> {

    private ID id;

}
