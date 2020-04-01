package graphql.kickstart.spring.boot.graphql.annotations.example;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestSubscription;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TimerSubscriptionTest {

    @Autowired
    private GraphQLTestSubscription graphQLTestSubscription;

    @Test
    @DisplayName("Should return the number of elapsed seconds since subscription started.")
    void testTimerSubscription() {
        // WHEN
        final List<GraphQLResponse> graphQLResponses = graphQLTestSubscription.start("timer.graphql")
            .awaitAndGetNextResponses(2900, 2);
        // THEN
        assertThat(graphQLResponses)
            .extracting(graphQLResponse -> graphQLResponse.get("$.data.timer", Long.class))
            .containsExactly(1L, 2L);
    }
}
