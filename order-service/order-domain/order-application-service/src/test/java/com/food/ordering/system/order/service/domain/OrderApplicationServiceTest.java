package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.mock.MockFactory;
import com.food.ordering.system.order.service.domain.ports.input.service.OrderApplicationService;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import com.food.ordering.system.valueobject.OrderId;
import com.food.ordering.system.valueobject.OrderStatus;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.exception.OrderNotFoundException;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static com.food.ordering.system.order.service.domain.mock.MockFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = OrderTestConfiguration.class)
public class OrderApplicationServiceTest {

    @Autowired
    private OrderApplicationService orderApplicationService;

    @Autowired
    private OrderDataMapper orderDataMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private CreateOrderCommand createOrderCommand;
    private CreateOrderCommand createOrderCommandWrongPrice;
    private CreateOrderCommand createOrderCommandWrongProductPrice;
    private Order order;


    @BeforeAll
    public void init() {
        createOrderCommand = MockFactory.createOrderCommand();
        createOrderCommandWrongPrice = MockFactory.createOrderCommandWrongPrice();
        createOrderCommandWrongProductPrice = MockFactory.createOrderCommandWrongProductPrice();

        Customer customer = MockFactory.customer();
        Restaurant restaurantResponse = MockFactory.restaurant();

        order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        order.setId(new OrderId(ORDER_ID));

        when(customerRepository.findCustomer(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(restaurantRepository.findRestaurantInformation(orderDataMapper.creOrderCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurantResponse));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
    }

    @Test
    public void testCreateOrder() {
        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);
        assertEquals(OrderStatus.PENDING, createOrderResponse.getOrderStatus());
        assertEquals("Order Created Successfully", createOrderResponse.getMessage());
        assertNotNull(createOrderResponse.getOrderTracking());
    }

    @Test
    public void testCreateOrderWrongTotalPrice() {
        OrderDomainException ex = assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommandWrongPrice));

        assertEquals("Total price: 250.00 is not equal to Order items total: 200.00", ex.getMessage());
    }

    @Test
    public void testCreateOrderWrongProductPrice() {
        OrderDomainException ex = assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommandWrongProductPrice));

        assertEquals("Order item price: 55.00 is not valid for product: "+ PRODUCT_ID_2, ex.getMessage());
    }

    @Test
    public void testCreateOrderWithPassiveRestaurant() {
        Restaurant restaurantResponse = MockFactory.restaurantNotActive();
        when(restaurantRepository.findRestaurantInformation(orderDataMapper.creOrderCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurantResponse));

        OrderDomainException ex = assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommand));

        assertEquals( "Restaurant with id " + restaurantResponse.getId().getValue() + " is currently not active!", ex.getMessage());
    }

    @Test
    public void testTrackOrder() {
        order.setTrackingId(new TrackingId(ORDER_TRACKING_ID));
        order.setOrderStatus(OrderStatus.PENDING);

        when(orderRepository.findByTrackingId(any(TrackingId.class))).thenReturn(Optional.of(order));
        TrackOrderResponse trackOrderResponse = orderApplicationService.trackOrder(MockFactory.trackOrderQuery());

        assertEquals(ORDER_TRACKING_ID, trackOrderResponse.getOrderTrackingId());
        assertEquals(OrderStatus.PENDING, trackOrderResponse.getOrderStatus());
        assertNull(trackOrderResponse.getFailureMessages());
    }

    @Test
    public void testTrackOrderNotFound() {
        when(orderRepository.findByTrackingId(any(TrackingId.class))).thenReturn(Optional.empty());

        OrderNotFoundException ex = assertThrows(OrderNotFoundException.class,
                () -> orderApplicationService.trackOrder(MockFactory.trackOrderQuery()));

        assertEquals("Could not find order with tracking id: "+ ORDER_TRACKING_ID, ex.getMessage());
    }
}
