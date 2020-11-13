package com.graphql.spring.boot.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Test error message handling.")
public class GraphQLTestSubscriptionErrorTest extends GraphQLTestSubscriptionTestBase {

    @Test
    @DisplayName("Should handle error messages.")
    void shouldHandleErrorMessages() {
        // WHEN - THEN
        graphQLTestSubscription.start(SUBSCRIPTION_THAT_THROWS_EXCEPTION)
            .awaitAndGetNextResponse(TIMEOUT)
            .assertThatDataField().isNotPresent()
            .and()
            .assertThatListOfErrors().isNotEmpty();
    }
}
