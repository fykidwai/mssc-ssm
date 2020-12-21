package io.github.fykidwai.msscssm.config;

import java.util.EnumSet;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import io.github.fykidwai.msscssm.domain.PaymentEvent;
import io.github.fykidwai.msscssm.domain.PaymentState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@EnableStateMachineFactory
@Configuration
public class StateMachineConfig extends StateMachineConfigurerAdapter<PaymentState, PaymentEvent> {

    private final Action<PaymentState, PaymentEvent> authAction;
    private final Action<PaymentState, PaymentEvent> preAuthAction;
    private final Guard<PaymentState, PaymentEvent> paymentIdGuard;
    private final Action<PaymentState, PaymentEvent> preAuthApprovedAction;
    private final Action<PaymentState, PaymentEvent> preAuthDeclinedAction;
    private final Action<PaymentState, PaymentEvent> authDeclinedAction;
    private final Action<PaymentState, PaymentEvent> authApprovedAction;

    @Override
    public void configure(final StateMachineStateConfigurer<PaymentState, PaymentEvent> states) throws Exception {
        states.withStates().initial(PaymentState.NEW).states(EnumSet.allOf(PaymentState.class)).end(PaymentState.AUTH)
            .end(PaymentState.PRE_AUTH_ERROR).end(PaymentState.AUTH_ERROR);
    }

    @Override
    public void configure(final StateMachineTransitionConfigurer<PaymentState, PaymentEvent> transitions) throws Exception {
        transitions.withExternal().source(PaymentState.NEW).target(PaymentState.NEW).event(PaymentEvent.PRE_AUTHORIZE)
            .action(preAuthAction).guard(paymentIdGuard)

            .and().withExternal().source(PaymentState.NEW).target(PaymentState.PRE_AUTH)
            .event(PaymentEvent.PRE_AUTH_APPROVED).action(preAuthApprovedAction)

            .and().withExternal().source(PaymentState.NEW).target(PaymentState.PRE_AUTH_ERROR)
            .event(PaymentEvent.PRE_AUTH_DECLINED).action(preAuthDeclinedAction)

            .and().withExternal().source(PaymentState.PRE_AUTH).target(PaymentState.PRE_AUTH).event(PaymentEvent.AUTHORIZE)
            .action(authAction)

            .and().withExternal().source(PaymentState.PRE_AUTH).target(PaymentState.AUTH).event(PaymentEvent.AUTH_APPROVED)
            .action(authApprovedAction)

            .and().withExternal().source(PaymentState.PRE_AUTH).target(PaymentState.AUTH_ERROR)
            .event(PaymentEvent.AUTH_DECLINED).action(authDeclinedAction);
    }

    @Override
    public void configure(final StateMachineConfigurationConfigurer<PaymentState, PaymentEvent> config) throws Exception {
        final StateMachineListenerAdapter<PaymentState, PaymentEvent> adapter = new StateMachineListenerAdapter<>() {

            @Override
            public void stateChanged(final State<PaymentState, PaymentEvent> from,
                final State<PaymentState, PaymentEvent> to) {
                log.info("State changed from: {}, to: {}", from, to);
            }
        };

        config.withConfiguration().listener(adapter);
    }

    /*
     * private Guard<PaymentState, PaymentEvent> paymentIdGuard() { return context ->
     * context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER) != null; } private Action<PaymentState, PaymentEvent>
     * preAuthAction() { return context -> { System.out.println("PreAuth was called"); if (new Random().nextInt(10) < 8) {
     * System.out.println("PreAuth APPROVED..!!!"); context.getStateMachine()
     * .sendEvent(MessageBuilder.withPayload(PaymentEvent.PRE_AUTH_APPROVED) .setHeader(PaymentServiceImpl.PAYMENT_ID_HEADER,
     * context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER)) .build()); } else {
     * System.out.println("PreAuth DECLINED... NO CREDIT..!!!!"); context.getStateMachine()
     * .sendEvent(MessageBuilder.withPayload(PaymentEvent.PRE_AUTH_DECLINED) .setHeader(PaymentServiceImpl.PAYMENT_ID_HEADER,
     * context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER)) .build()); } }; } private Action<PaymentState,
     * PaymentEvent> authAction() { return context -> { System.out.println("Authorized was called"); if (new
     * Random().nextInt(10) < 8) { System.out.println("AUTH APPROVED..!!!"); context.getStateMachine().sendEvent(
     * MessageBuilder.withPayload(PaymentEvent.AUTH_APPROVED).setHeader(PaymentServiceImpl.PAYMENT_ID_HEADER,
     * context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER)).build()); } else {
     * System.out.println("AUTH DECLINED... NO CREDIT..!!!!"); context.getStateMachine().sendEvent(
     * MessageBuilder.withPayload(PaymentEvent.AUTH_DECLINED).setHeader(PaymentServiceImpl.PAYMENT_ID_HEADER,
     * context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER)).build()); } }; }
     */
}
