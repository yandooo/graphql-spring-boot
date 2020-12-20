package graphql.kickstart.graphql.annotations;

import static org.assertj.core.api.Assertions.assertThat;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import graphql.kickstart.graphql.annotations.test.interfaces.Car;
import graphql.kickstart.graphql.annotations.test.interfaces.Truck;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("Testing interface handling.")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "interface-test"})
class GraphQLInterfaceQueryTest {

  @Autowired
  private GraphQLTestTemplate graphQLTestTemplate;

  @Test
  @DisplayName("Assert that GraphQL interfaces and their implementations are registered correctly.")
  void testInterfaceQuery() throws IOException {
    // WHEN
    final GraphQLResponse actual = graphQLTestTemplate
        .postForResource("queries/test-interface-query.graphql");
    // THEN
    assertThat(actual.get("$.data.vehicles[0]", Car.class))
        .usingRecursiveComparison().ignoringAllOverriddenEquals()
        .isEqualTo(Car.builder().numberOfSeats(4).registrationNumber("ABC-123").build());
    assertThat(actual.get("$.data.vehicles[1]", Truck.class))
        .usingRecursiveComparison().ignoringAllOverriddenEquals()
        .isEqualTo(Truck.builder().cargoWeightCapacity(12).registrationNumber("CBA-321").build());
  }
}

