package com.graphql.spring.boot.test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Testing awaitNoResponse methods")
class GraphQLSubscriptionTestAwaitNoAnswerTest extends GraphQLTestSubscriptionTestBase {

  @Test
  @DisplayName("Should succeed if no responses arrived / default stopAfter.")
  void shouldAwaitNoResponseSucceedIfNoResponsesArrivedDefaultStopAfter() {
    // WHEN - THEN
    graphQLTestSubscription
        .start(SUBSCRIPTION_THAT_TIMES_OUT_RESOURCE)
        .waitAndExpectNoResponse(TIMEOUT);
    assertThatSubscriptionWasStopped();
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  @DisplayName("Should succeed if no responses arrive.")
  void shouldAwaitNoResponseSucceedIfNoResponsesArrived(
      final boolean stopAfter
  ) {
    // WHEN - THEN
    graphQLTestSubscription
        .start(SUBSCRIPTION_THAT_TIMES_OUT_RESOURCE)
        .waitAndExpectNoResponse(TIMEOUT, stopAfter);
    assertThatSubscriptionStoppedStatusIs(stopAfter);
  }

  @Test
  @DisplayName("Should raise assertion error if any response arrived / default stop after.")
  void shouldRaiseAssertionErrorIfResponseArrivedDefaultStopAfter() {
    // WHEN - THEN
    graphQLTestSubscription.start(TIMER_SUBSCRIPTION_RESOURCE);
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> graphQLTestSubscription
        .waitAndExpectNoResponse(TIMEOUT));
    assertThatSubscriptionWasStopped();
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  @DisplayName("Should raise assertion error if any response arrived.")
  void shouldRaiseAssertionErrorIfResponseArrived(
      final boolean stopAfter
  ) {
    // WHEN - THEN
    graphQLTestSubscription.start(TIMER_SUBSCRIPTION_RESOURCE);
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> graphQLTestSubscription
        .waitAndExpectNoResponse(TIMEOUT, stopAfter));
    assertThatSubscriptionStoppedStatusIs(stopAfter);
  }
}
