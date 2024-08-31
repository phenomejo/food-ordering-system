package com.food.ordering.system.order.service.dataaccess.customer.mapper;

import com.food.ordering.system.order.service.dataaccess.customer.entity.CustomerEntity;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.domain.valueobject.CustomerId;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataAccessMapper {

    public Customer customerEntityToCustomer(CustomerEntity customerEntity) {
        return Customer.builder()
                .id(new CustomerId(customerEntity.getId()))
//                .username(customerEntity.getUsername())
//                .firstName(customerEntity.getFirstName())
//                .lastName(customerEntity.getLastName())
                .build();
    }

//    public CustomerEntity customerToCustomerEntity(Customer customer) {
//        return CustomerEntity.builder()
//                .id(customer.getId().getValue())
//                .username(customer.getUsername())
//                .firstName(customer.getFirstName())
//                .lastName(customer.getLastName())
//                .build();
//    }

}
