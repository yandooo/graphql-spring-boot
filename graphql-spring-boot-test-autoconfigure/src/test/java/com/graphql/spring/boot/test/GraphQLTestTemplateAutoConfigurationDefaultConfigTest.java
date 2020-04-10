package com.graphql.spring.boot.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@DisplayName("Testing auto-configuration of the GraphQLTestTemplate bean / default servlet endpoint.")
public class GraphQLTestTemplateAutoConfigurationDefaultConfigTest extends GraphQLTestAutoConfigurationTestBase {

    @Test
    @DisplayName("GraphQLTestTemplate bean should work properly.")
    void shouldProvideGraphQLTestTemplateBean() throws IOException {
        assertThatTestTemplateAutoConfigurationWorksCorrectly();
    }
}
