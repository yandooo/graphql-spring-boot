package graphql.kickstart.graphql.annotations;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testing mutation resolver registration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "mutation-test"})
public class GraphQLAnnotationsMutationTest {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Test
    @DisplayName("Assert that mutation resolver is properly registered.")
    void testMutationResolver() throws IOException {
        // WHEN
        final GraphQLResponse actual = graphQLTestTemplate.postForResource("mutations/test-mutation.graphql");
        // THEN
        assertThat(actual.get("$.data.performSomeOperation.testField")).isEqualTo("Test value");
    }
}
