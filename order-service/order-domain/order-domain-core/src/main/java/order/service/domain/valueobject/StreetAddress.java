package order.service.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class StreetAddress {

    private UUID id;
    private String street;
    private String postCode;
    private String city;
}
