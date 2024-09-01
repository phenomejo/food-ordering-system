package com.food.ordering.system.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.kafka.order.avro.model.PaymentStatus;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import com.food.ordering.system.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponseAvroModel> {

    private final PaymentResponseMessageListener paymentResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}",
            topics = "${order-service.payment-response-topic-name}")
    public void receive(ConsumerRecords<String, PaymentResponseAvroModel> consumerRecords) {
        log.info("{} number of PaymentResponseAvroModel responses received",
                consumerRecords.count());

        consumerRecords.forEach(consumerRecord -> {
            PaymentResponseAvroModel paymentResponseAvroModel = consumerRecord.value();
            log.info(
                    "Received PaymentResponseAvroModel: {} with keys {}, partitions {} and offsets {}",
                    paymentResponseAvroModel, consumerRecord.key(), consumerRecord.partition(),
                    consumerRecord.offset());

            if (PaymentStatus.COMPLETED.equals(paymentResponseAvroModel.getPaymentStatus())) {
                log.info("Processing successful payment for order id: {}",
                        paymentResponseAvroModel.getOrderId());
                paymentResponseMessageListener
                        .paymentCompleted(orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(
                                paymentResponseAvroModel));
            } else if (PaymentStatus.CANCELLED.equals(paymentResponseAvroModel.getPaymentStatus())) {
                log.info("Processing unsuccessful payment for order id: {}",
                        paymentResponseAvroModel.getOrderId());
                paymentResponseMessageListener
                        .paymentCancelled(orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(
                                paymentResponseAvroModel));
            }
        });

    }
}
