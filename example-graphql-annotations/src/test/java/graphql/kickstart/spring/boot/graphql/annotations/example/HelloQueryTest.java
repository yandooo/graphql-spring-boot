package graphql.kickstart.spring.boot.graphql.annotations.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testing the \"hello\" query.")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloQueryTest {

    private static final String HELLO_DATA_FIELD = "$.data.hello";
    private static final String HELLO_GRAPHQL_RESOURCE = "hello.graphql";

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should work without parameter, returning \"Hello, World!\"")
    void testHelloQueryWithoutArgument() throws IOException {
        // GIVEN
        final String actual = graphQLTestTemplate.postForResource(HELLO_GRAPHQL_RESOURCE).get(HELLO_DATA_FIELD);
        // THEN
        assertThat(actual).isEqualTo("Hello, World!");
    }

    @Test
    @DisplayName("Should work with parameter, using the provided name in the hello message.")
    void testHelloQueryWithArgument() throws IOException {
        // GIVEN
        final String name = "Johnny";
        final ObjectNode params = objectMapper.createObjectNode();
        params.put("who", name);
        // WHEN
        final String actual = graphQLTestTemplate.perform(HELLO_GRAPHQL_RESOURCE, params).get(HELLO_DATA_FIELD);
        // THEN
        assertThat(actual).isEqualTo(String.format("Hello, %s!", name));
    }
}
