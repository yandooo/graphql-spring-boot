package com.graphql.spring.boot.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GraphQLTestAutoConfigurationTestBase {

    static final String FOO = "foo";

    @Autowired
    ApplicationContext applicationContext;

    void assertThatTestSubscriptionWorksCorrectly() {
        // GIVEN
        final GraphQLTestSubscription graphQLTestSubscription
            = applicationContext.getBean(GraphQLTestSubscription.class);
        // WHEN
        final GraphQLResponse graphQLResponse
            = graphQLTestSubscription.start("test-subscription.graphql").awaitAndGetNextResponse(1000);
        // THEN
        assertThat(graphQLResponse.get("$.data.testSubscription")).isEqualTo(FOO);
    }

    void assertThatTestTemplateAutoConfigurationWorksCorrectly() throws IOException {
        // GIVEN
        final GraphQLTestTemplate graphQLTestTemplate
            = applicationContext.getBean(GraphQLTestTemplate.class);
        // WHEN
        final GraphQLResponse graphQLResponse
            = graphQLTestTemplate.postForResource("test-query.graphql");
        // THEN
        assertThat(graphQLResponse.get("$.data.testQuery")).isEqualTo(FOO);
    }
}
