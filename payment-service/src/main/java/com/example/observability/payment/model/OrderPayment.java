package com.example.observability.payment.model;

public record OrderPayment(Long id,
                           String paymentToken,
                           Double amount) {
}
