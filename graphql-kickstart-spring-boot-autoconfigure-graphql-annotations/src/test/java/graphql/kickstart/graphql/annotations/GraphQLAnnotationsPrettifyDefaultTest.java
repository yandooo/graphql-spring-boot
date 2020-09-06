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

@DisplayName("Testing prettify settings (default)")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "prettify-default-test"})
public class GraphQLAnnotationsPrettifyDefaultTest {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Test
    @DisplayName("By default, it should always prettify fields.")
    void testDefaultPrettifySettings() throws IOException {
        // WHEN
        final GraphQLResponse graphQLResponse
            = graphQLTestTemplate.postForResource("queries/test-prettified-query.graphql");
        // THEN
        assertThat(graphQLResponse.get("$.data.someValue")).isEqualTo("some value");
    }
}
