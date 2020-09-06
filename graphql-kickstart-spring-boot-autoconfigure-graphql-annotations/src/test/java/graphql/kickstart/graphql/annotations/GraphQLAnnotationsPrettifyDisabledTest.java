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

@DisplayName("Test prettify settings (disabled)")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "prettify-disabled-test"})
public class GraphQLAnnotationsPrettifyDisabledTest {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Test
    @DisplayName("If disabled, fields should not be prettified.")
    void testDefaultPrettifySettings() throws IOException {
        // WHEN
        final GraphQLResponse graphQLResponse
            = graphQLTestTemplate.postForResource("queries/test-not-prettified-query.graphql");
        // THEN
        assertThat(graphQLResponse.get("$.data.getSomeValue")).isEqualTo("some value");
    }
}
