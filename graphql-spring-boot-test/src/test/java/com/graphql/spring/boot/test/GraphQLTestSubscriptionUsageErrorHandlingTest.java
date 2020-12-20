package com.graphql.spring.boot.test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testing if usage errors are properly handled.")
class GraphQLTestSubscriptionUsageErrorHandlingTest extends GraphQLTestSubscriptionTestBase {

  @Test
  @DisplayName("Should raise an assertion error if init is called after init.")
  void shouldRaiseAssertionErrorIfInitAfterInit() {
    assertThatExceptionOfType(AssertionError.class)
        .isThrownBy(() -> graphQLTestSubscription.init().init());
  }

  @Test
  @DisplayName("Should raise an assertion error if awaitAndGet times out.")
  void shouldRaiseAssertionErrorIfAwaitAndGetTimesOut() {
    assertThatExceptionOfType(AssertionError.class)
        .isThrownBy(() -> graphQLTestSubscription
            .start(SUBSCRIPTION_THAT_TIMES_OUT_RESOURCE)
            .awaitAndGetNextResponse(TIMEOUT));
  }

  @Test
  @DisplayName("Should raise an assertion error if awaitAndGet methods are called before start.")
  void shouldRaiseAssertionErrorIfGettingResponseWithoutStart() {
    assertThatExceptionOfType(AssertionError.class)
        .isThrownBy(() -> graphQLTestSubscription.init().awaitAndGetNextResponse(TIMEOUT));
  }

  @Test
  @DisplayName("Should raise an assertion error if stop is called before init.")
  void shouldRaiseAssertionErrorIfStopWithoutStart() {
    assertThatExceptionOfType(AssertionError.class)
        .isThrownBy(() -> graphQLTestSubscription.stop());
  }

  @Test
  @DisplayName("Should raise an assertion error if stop is called after stop.")
  void shouldRaiseAssertionErrorIfStopAfterStop() {
    assertThatExceptionOfType(AssertionError.class)
        .isThrownBy(() -> graphQLTestSubscription.start(TIMER_SUBSCRIPTION_RESOURCE).stop().stop());
  }

  @Test
  @DisplayName("Should raise an assertion error if awaitAndGet methods are called after stop.")
  void shouldRaiseAssertionErrorIfGettingResponseAfterStop() {
    assertThatExceptionOfType(AssertionError.class)
        .isThrownBy(() -> graphQLTestSubscription
            .start(TIMER_SUBSCRIPTION_RESOURCE)
            .stop()
            .awaitAndGetNextResponse(TIMEOUT));
  }

  @Test
  @DisplayName("Should raise an assertion error if the provided GraphQL resource could not be found.")
  void shouldRaiseAssertionErrorIfGraphQLResourceCouldNotBeFound() {
    assertThatExceptionOfType(AssertionError.class)
        .isThrownBy(() -> graphQLTestSubscription.start("non-existing-file.graphql"));
  }
}
