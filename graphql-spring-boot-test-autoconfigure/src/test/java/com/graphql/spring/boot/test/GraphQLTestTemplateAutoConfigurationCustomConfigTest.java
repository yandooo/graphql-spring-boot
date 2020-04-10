package com.graphql.spring.boot.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles({"test", "custom-servlet-mapping"})
@DisplayName("Testing auto-configuration of the GraphQLTestTemplate bean / custom servlet endpoint.")
public class GraphQLTestTemplateAutoConfigurationCustomConfigTest extends GraphQLTestAutoConfigurationTestBase {

    @Test
    @DisplayName("GraphQLTestTemplate bean should work properly.")
    void shouldProvideGraphQLTestTemplateBean() throws IOException {
        assertThatTestTemplateAutoConfigurationWorksCorrectly();
        assertThat(ReflectionTestUtils.getField(applicationContext.getBean(GraphQLTestTemplate.class),
            "graphqlMapping"))
            .as("Should use the configured servlet path.")
            .isEqualTo("/myCustomGraphQLEndpoint");
    }
}
