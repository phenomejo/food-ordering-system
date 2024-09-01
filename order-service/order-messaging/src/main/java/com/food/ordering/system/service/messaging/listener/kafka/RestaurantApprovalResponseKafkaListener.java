package com.food.ordering.system.service.messaging.listener.kafka;

import static com.food.ordering.system.order.service.domain.entity.Order.FAILURE_MESSAGE_DELIMITER;

import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.OrderApprovalStatus;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;
import com.food.ordering.system.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class RestaurantApprovalResponseKafkaListener implements
        KafkaConsumer<RestaurantApprovalResponseAvroModel> {

    private final RestaurantApprovalResponseMessageListener restaurantApprovalResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}",
            topics = "${order-service.restaurant-approval-response-topic-name}")
    public void receive(
            ConsumerRecords<String, RestaurantApprovalResponseAvroModel> consumerRecords) {
        log.info("{} number of RestaurantApprovalResponseAvroModel responses received",
                consumerRecords.count());

        consumerRecords.forEach(consumerRecord -> {
            RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel = consumerRecord.value();
            log.info(
                    "Received RestaurantApprovalResponseAvroModel: {} with keys {}, partitions {} and offsets {}",
                    restaurantApprovalResponseAvroModel, consumerRecord.key(),
                    consumerRecord.partition(), consumerRecord.offset());

            if (OrderApprovalStatus.APPROVED.equals(restaurantApprovalResponseAvroModel.getOrderApprovalStatus())) {
                log.info("Processing approved order for order id: {}",
                        restaurantApprovalResponseAvroModel.getOrderId());

                restaurantApprovalResponseMessageListener
                        .orderApproved(orderMessagingDataMapper.approvalResponseAvroModelToApprovalResponse(
                                restaurantApprovalResponseAvroModel));
            } else if (OrderApprovalStatus.REJECTED.equals(
                    restaurantApprovalResponseAvroModel.getOrderApprovalStatus())) {
                log.info("Processing rejected order for order id: {}, with failure messages: {}",
                        restaurantApprovalResponseAvroModel.getOrderId(),
                        String.join(FAILURE_MESSAGE_DELIMITER,
                                restaurantApprovalResponseAvroModel.getFailureMessages()));

                restaurantApprovalResponseMessageListener
                        .orderRejected(orderMessagingDataMapper.approvalResponseAvroModelToApprovalResponse(
                                restaurantApprovalResponseAvroModel));
            }
        });
    }
}
