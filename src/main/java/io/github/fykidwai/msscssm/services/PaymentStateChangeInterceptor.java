package io.github.fykidwai.msscssm.services;

import java.util.Optional;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import io.github.fykidwai.msscssm.domain.Payment;
import io.github.fykidwai.msscssm.domain.PaymentEvent;
import io.github.fykidwai.msscssm.domain.PaymentState;
import io.github.fykidwai.msscssm.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentStateChangeInterceptor extends StateMachineInterceptorAdapter<PaymentState, PaymentEvent> {

    private final PaymentRepository paymentRepository;

    @Override
    public void preStateChange(final State<PaymentState, PaymentEvent> state, final Message<PaymentEvent> message,
        final Transition<PaymentState, PaymentEvent> transition,
        final StateMachine<PaymentState, PaymentEvent> stateMachine) {

        Optional.ofNullable(message)
            .ifPresent(msg -> Optional
                .ofNullable(Long.class.cast(msg.getHeaders().getOrDefault(PaymentServiceImpl.PAYMENT_ID_HEADER, -1L)))
                .ifPresent(paymentId -> {
                    final Payment payment = paymentRepository.getOne(paymentId);
                    payment.setPaymentState(state.getId());
                    paymentRepository.save(payment);
                }));
    }

}
