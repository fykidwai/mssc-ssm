package io.github.fykidwai.msscssm.services;

import static org.assertj.core.api.Assertions.setPrintAssertionsDescription;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.transaction.annotation.Transactional;

import io.github.fykidwai.msscssm.domain.Payment;
import io.github.fykidwai.msscssm.domain.PaymentEvent;
import io.github.fykidwai.msscssm.domain.PaymentState;
import io.github.fykidwai.msscssm.repository.PaymentRepository;

@SpringBootTest
class PaymentServiceImplTest {

    static {
        setPrintAssertionsDescription(true);
    }

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    private Payment payment;

    @BeforeEach
    void setUp() throws Exception {
        payment = Payment.builder().amount(BigDecimal.valueOf(12.99)).build();
    }

    @Test
    @Transactional
    final void testPreAuth() {
        final Payment savedPayment = paymentService.newPayment(payment);

        System.out.println("Should be NEW");
        System.out.println(savedPayment.getPaymentState());

        final StateMachine<PaymentState, PaymentEvent> sm = paymentService.preAuth(savedPayment.getId());

        final Payment preAuthedPayment = paymentRepository.getOne(savedPayment.getId());

        System.out.println("Should be PRE_AUTH");
        System.out.println(sm.getState().getId());

        System.out.println(preAuthedPayment);
    }

}
