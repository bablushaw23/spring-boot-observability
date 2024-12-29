package com.example.observability.order.service;

import com.example.observability.order.model.OrderPayment;
import com.example.observability.order.model.dto.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
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

    @Autowired
    RestTemplate restTemplate;

    public void processOrderPayments(Order order) {

        callAPI(getOrderPayment(order));

    }

    private void callAPI(OrderPayment orderPayment) {
        log.info("Calling payment service");
        restTemplate.postForEntity("http://localhost:8082/api/v1/payment/order-payment", orderPayment, String.class);
    }

    private OrderPayment getOrderPayment(Order order){
        // TODO: generate actual amount wrt to an inventory
        double amount = ThreadLocalRandom.current().nextDouble(10, 120);
        return new OrderPayment(order.id(),order.paymentToken(), amount);
    }
}
