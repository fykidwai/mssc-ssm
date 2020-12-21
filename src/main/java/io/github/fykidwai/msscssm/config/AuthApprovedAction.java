package io.github.fykidwai.msscssm.config;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import io.github.fykidwai.msscssm.domain.PaymentEvent;
import io.github.fykidwai.msscssm.domain.PaymentState;

@Component
public class AuthApprovedAction implements Action<PaymentState, PaymentEvent> {

    @Override
    public void execute(final StateContext<PaymentState, PaymentEvent> context) {
        System.out.println("Sending Notification of Auth APPROVED");

    }

}
