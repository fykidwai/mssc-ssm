package io.github.fykidwai.msscssm.config;

import java.util.Random;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import io.github.fykidwai.msscssm.domain.PaymentEvent;
import io.github.fykidwai.msscssm.domain.PaymentState;
import io.github.fykidwai.msscssm.services.PaymentServiceImpl;

@Component
public class AuthAction implements Action<PaymentState, PaymentEvent> {

    @Override
    public void execute(final StateContext<PaymentState, PaymentEvent> context) {
        System.out.println("Authorized was called");
        if (new Random().nextInt(10) < 8) {
            System.out.println("AUTH APPROVED..!!!");
            context.getStateMachine().sendEvent(
                MessageBuilder.withPayload(PaymentEvent.AUTH_APPROVED).setHeader(PaymentServiceImpl.PAYMENT_ID_HEADER,
                    context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER)).build());
        } else {
            System.out.println("AUTH DECLINED... NO CREDIT..!!!!");
            context.getStateMachine().sendEvent(
                MessageBuilder.withPayload(PaymentEvent.AUTH_DECLINED).setHeader(PaymentServiceImpl.PAYMENT_ID_HEADER,
                    context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER)).build());
        }
    }

}
