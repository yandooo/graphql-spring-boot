package graphql.kickstart.autoconfigure.annotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeast;

import com.graphql.spring.boot.test.GraphQLTestTemplate;
import graphql.kickstart.autoconfigure.annotations.test.custom.relay.CustomRelay;
import graphql.schema.GraphQLSchema;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("Testing custom relay configuration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"annotations", "test", "custom-relay-test"})
class GraphQLAnnotationsCustomRelayTest {

  @SpyBean private CustomRelay customRelay;

  @Autowired private GraphQLSchema schema;

  @Autowired private GraphQLTestTemplate graphQLTestTemplate;

  @Test
  @DisplayName("Assert that custom relay is used.")
  void testCustomRelay() throws IOException {
    // WHEN
    graphQLTestTemplate.postForResource("annotations/queries/test-paginated-query.graphql");
    // THEN
    then(customRelay).should(atLeast(1)).edgeType(any(), any(), any(), any());
  }
}
