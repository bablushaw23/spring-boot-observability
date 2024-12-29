package com.example.observability.order.service;

import com.example.observability.order.model.OrderPayment;
import com.example.observability.order.model.dto.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class OrderPaymentService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void processOrderPayments(Order order) {

        // Serialize the Order object to JSON
        OrderPayment orderPayment = getOrderPayment(order);
//            String orderJson = objectMapper.writeValueAsString(orderPayment);

        // Send the JSON payload to Kafka
//            kafkaTemplate.send("order-events", String.valueOf(order.id()), orderJson);
        log.info("Pretending to send to kafka:{}", orderPayment);
//            throw new RuntimeException("Failed to connect to kafka");
        callPaymentService(orderPayment);

    }

    private void callPaymentService(OrderPayment orderPayment) {
        String uri="http://localhost:8082/api/v1/payment/process-payment";
        RestTemplate restTemplate= new RestTemplate();
        try {
            String orderJson = objectMapper.writeValueAsString(orderPayment);
            ResponseEntity<String> response = restTemplate.postForEntity(uri, orderPayment, String.class);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private OrderPayment getOrderPayment(Order order) {
        // TODO: generate actual amount wrt to an inventory
        double amount = ThreadLocalRandom.current().nextDouble(10, 120);
        return new OrderPayment(order.id(), order.paymentToken(), amount);
    }
}
