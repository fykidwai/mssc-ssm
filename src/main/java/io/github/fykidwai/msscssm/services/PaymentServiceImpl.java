package io.github.fykidwai.msscssm.services;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import io.github.fykidwai.msscssm.domain.Payment;
import io.github.fykidwai.msscssm.domain.PaymentEvent;
import io.github.fykidwai.msscssm.domain.PaymentState;
import io.github.fykidwai.msscssm.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final StateMachineFactory<PaymentState, PaymentEvent> factory;

    @Override
    public Payment newPayment(final Payment payment) {
        payment.setPaymentState(PaymentState.NEW);
        return paymentRepository.save(payment);
    }

    @Override
    public StateMachine<PaymentState, PaymentEvent> preAuth(final Long paymentId) {
        final StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);
        return null;
    }

    @Override
    public StateMachine<PaymentState, PaymentEvent> authorizePayment(final Long paymentId) {
        final StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);
        return null;
    }

    @Override
    public StateMachine<PaymentState, PaymentEvent> declineAuth(final Long paymentId) {
        final StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);
        return null;
    }

    private StateMachine<PaymentState, PaymentEvent> build(final Long paymentId) {
        // Fetch Payment info from db
        final Payment payment = paymentRepository.getOne(paymentId);

        // Get StateMachine object
        final StateMachine<PaymentState, PaymentEvent> sm = factory.getStateMachine(Long.toString(payment.getId()));

        // Stop the state machine and reset so as to set only the state from the entity object
        sm.stop();
        sm.getStateMachineAccessor().doWithAllRegions(
            sma -> sma.resetStateMachine(new DefaultStateMachineContext<>(payment.getPaymentState(), null, null, null)));

        // Start the state machine and return
        sm.start();

        return sm;
    }

}
