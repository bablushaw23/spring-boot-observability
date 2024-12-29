package com.example.observability.payment;

import com.example.observability.payment.model.Order;
import com.example.observability.payment.model.OrderPayment;
import com.example.observability.payment.model.OrderStatus;
import com.example.observability.payment.service.OrderUpdateService;
import com.example.observability.payment.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@Slf4j
public class OrderEventHandler {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PaymentService paymentService;

    @Autowired
    OrderUpdateService orderUpdateService;

//    @KafkaListener(topics = "order-events")
    @PostMapping("/process-payment")
    public void listen(@RequestBody OrderPayment orderPayment) {
//        try {
            log.info("Received order event: {}", orderPayment);

            // Process payment
            paymentService.processPayment(orderPayment);
            throw new RuntimeException("Inensionally thrown");
            // Update order status to PROCESSING
//            orderUpdateService.updateOrderStatus(new Order(orderPayment.id(), OrderStatus.PAYMENT_VERIFIED));
//        } catch (Exception e) {
//            log.error("Failed to process order event", e);
//        }
    }
}
