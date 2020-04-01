package com.graphql.spring.boot.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Queue;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testing reset")
public class GraphQLTestSubscriptionResetTest extends GraphQLTestSubscriptionTestBase {

    @Test
    @DisplayName("Should work if subscription was not yet started.")
    void shouldWorkIfSubscriptionWasNotStarted() {
        // WHEN
        final int firstId = getSubscriptionId();
        graphQLTestSubscription.reset();
        // THEN
        assertThatSubscriptionWasReset();
        assertThatExistingIdWasRetained(firstId);
    }

    @Test
    @DisplayName("Should work if subscription is still active.")
    void shouldWorkIfSubscriptionIsStillActive() {
        // GIVEN
        final int firstId = getSubscriptionId();
        graphQLTestSubscription.start(TIMER_SUBSCRIPTION_RESOURCE);
        // WHEN
        graphQLTestSubscription.reset();
        // THEN
        assertThatSubscriptionWasReset();
        assertThatNewIdWasGenerated(firstId);
    }

    @Test
    @DisplayName("Should work if subscription was stopped.")
    void shouldWorkIfSubscriptionWasAlreadyStopped() {
        // GIVEN
        final int firstId = getSubscriptionId();
        graphQLTestSubscription.start(TIMER_SUBSCRIPTION_RESOURCE).stop();
        // WHEN
        graphQLTestSubscription.reset();
        // THEN
        assertThatSubscriptionWasReset();
        assertThatNewIdWasGenerated(firstId);
    }

    @Test
    @DisplayName("Should allow starting a new subscription after reset.")
    void shouldAllowStartingNewSubscriptionAfterReset() {
        // GIVEN
        graphQLTestSubscription.start(TIMER_SUBSCRIPTION_RESOURCE);
        // WHEN
        graphQLTestSubscription.reset();
        // THEN
        graphQLTestSubscription.start(TIMER_SUBSCRIPTION_RESOURCE);
    }

    private void assertThatSubscriptionWasReset() {
        assertThat(graphQLTestSubscription.isInitialized()).isFalse();
        assertThat(graphQLTestSubscription.isAcknowledged()).isFalse();
        assertThat(graphQLTestSubscription.isStarted()).isFalse();
        assertThat(graphQLTestSubscription.isStopped()).isFalse();
        assertThat((Queue<?>) ReflectionTestUtils.getField(graphQLTestSubscription, GraphQLTestSubscription.class,
            "responses")).isEmpty();
        assertThat(graphQLTestSubscription.getSession()).isNull();
    }

    private void assertThatNewIdWasGenerated(int previousId) {
        assertThat(getSubscriptionId()).isEqualTo(previousId + 1);
    }

    private void assertThatExistingIdWasRetained(int previousId) {
        assertThat(getSubscriptionId()).isEqualTo(previousId);
    }

    private int getSubscriptionId() {
        return (int) ReflectionTestUtils.getField(graphQLTestSubscription, GraphQLTestSubscription.class, "id");
    }
}
