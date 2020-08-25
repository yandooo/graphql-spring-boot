package com.graphql.spring.boot.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.beans.FooBar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GraphQLTestTemplateIntegrationTest {

    private static final String SIMPLE_TEST_QUERY = "simple-test-query.graphql";
    private static final String SIMPLE_TEST_QUERY_WITH_FRAGMENTS = "simple-test-query-with-fragments.graphql";
    private static final String TEST_FRAGMENT_FILE = "foo-bar-fragment.graphql";
    private static final String QUERY_WITH_VARIABLES = "query-with-variables.graphql";
    private static final String MULTIPLE_QUERIES = "multiple-queries.graphql";

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private GraphQLTestTemplate graphQLTestTemplate;

    @BeforeEach
    void setUp() {
        graphQLTestTemplate = new GraphQLTestTemplate(resourceLoader, testRestTemplate, "/graphql", objectMapper);
    }

    @Test
    @DisplayName("Test postForResource with only the GraphQL resource provided.")
    void testPostForResource() throws IOException {
        graphQLTestTemplate.postForResource(SIMPLE_TEST_QUERY)
            .assertThatNoErrorsArePresent()
            .assertThatField("$.data.otherQuery").asString().isEqualTo("TEST");
    }

    @Test
    @DisplayName("Test postForResource with fragments.")
    void testPostForResourceWithFragments() throws IOException {
        graphQLTestTemplate.postForResource(SIMPLE_TEST_QUERY_WITH_FRAGMENTS,
            Collections.singletonList(TEST_FRAGMENT_FILE))
            .assertThatNoErrorsArePresent()
            .assertThatField("$.data.fooBar").as(FooBar.class)
                .usingRecursiveComparison()
                .ignoringAllOverriddenEquals()
                .isEqualTo(FooBar.builder().foo("FOO").bar("BAR").build());
    }

    @Test
    @DisplayName("Test perform with variables.")
    void testPerformWithVariables() throws IOException {
        // GIVEN
        final String inputString = "test-input-string";
        final ObjectNode variables = objectMapper.createObjectNode();
        variables.put("input", inputString);
        // WHEN - THEN
        graphQLTestTemplate.perform(QUERY_WITH_VARIABLES, variables)
            .assertThatNoErrorsArePresent()
            .assertThatField("$.data.queryWithVariables").asString().isEqualTo(inputString);
    }

    @Test
    @DisplayName("Test perform with operation name.")
    void testPerformWithOperationName() throws IOException {
        // WHEN - THEN
        graphQLTestTemplate.perform(MULTIPLE_QUERIES, "testQuery1")
            .assertThatNoErrorsArePresent()
            .assertThatField("$.data.dummy").asBoolean().isTrue();
        graphQLTestTemplate.perform(MULTIPLE_QUERIES, "testQuery2")
            .assertThatNoErrorsArePresent()
            .assertThatField("$.data.otherQuery").asString().isEqualTo("TEST");
    }
}
