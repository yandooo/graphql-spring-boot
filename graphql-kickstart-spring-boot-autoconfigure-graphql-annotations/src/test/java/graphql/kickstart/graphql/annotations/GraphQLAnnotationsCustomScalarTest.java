package graphql.kickstart.graphql.annotations;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testing that custom scalar beans are properly registered.")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "custom-scalar-test"})
public class GraphQLAnnotationsCustomScalarTest {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Test
    @DisplayName("Assert that custom scalars work properly.")
    void testQueryWithCustomScalar() throws IOException {
        // WHEN
        final GraphQLResponse graphQLResponse
                = graphQLTestTemplate.postForResource("queries/test-custom-scalar-query.graphql");
        // THEN
        assertThat(graphQLResponse.get("$.data.randomUUID", UUID.class)).isNotNull();
    }
}
