package io.github.fykidwai.msscssm.config;

import java.util.EnumSet;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import io.github.fykidwai.msscssm.domain.PaymentEvent;
import io.github.fykidwai.msscssm.domain.PaymentState;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableStateMachineFactory
@Configuration
public class StateMachineConfig extends StateMachineConfigurerAdapter<PaymentState, PaymentEvent> {

    @Override
    public void configure(final StateMachineStateConfigurer<PaymentState, PaymentEvent> states) throws Exception {
        states.withStates().initial(PaymentState.NEW).states(EnumSet.allOf(PaymentState.class)).end(PaymentState.AUTH)
            .end(PaymentState.PRE_AUTH_ERROR).end(PaymentState.AUTH_ERROR);
    }

    @Override
    public void configure(final StateMachineTransitionConfigurer<PaymentState, PaymentEvent> transitions) throws Exception {
        transitions.withExternal().source(PaymentState.NEW).target(PaymentState.NEW).event(PaymentEvent.PRE_AUTHORIZE)

            .and().withExternal().source(PaymentState.NEW).target(PaymentState.PRE_AUTH)
            .event(PaymentEvent.PRE_AUTH_APPROVED)

            .and().withExternal().source(PaymentState.NEW).target(PaymentState.PRE_AUTH_ERROR)
            .event(PaymentEvent.PRE_AUTH_DECLINED);
    }

    @Override
    public void configure(final StateMachineConfigurationConfigurer<PaymentState, PaymentEvent> config) throws Exception {
        final StateMachineListenerAdapter<PaymentState, PaymentEvent> adapter =
            new StateMachineListenerAdapter<>() {

                @Override
                public void stateChanged(final State<PaymentState, PaymentEvent> from,
                    final State<PaymentState, PaymentEvent> to) {
                    log.info("State changed from: {}, to: {}", from, to);
                }
            };

        config.withConfiguration().listener(adapter);
    }

}
