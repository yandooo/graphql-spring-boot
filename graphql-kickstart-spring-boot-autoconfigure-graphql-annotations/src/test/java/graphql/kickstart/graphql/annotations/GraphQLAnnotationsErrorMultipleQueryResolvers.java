package graphql.kickstart.graphql.annotations;

import graphql.kickstart.graphql.annotations.exceptions.MultipleQueryResolversException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContextException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("Test exception if multiple query resolvers are defined.")
public class GraphQLAnnotationsErrorMultipleQueryResolvers {

    @Test
    @DisplayName("Assert that MultipleQueryResolversException is thrown when multiple query resolvers are found.")
    public void testMultipleQueryResolversExceptionIsThrown() {
        // GIVEN
        final SpringApplication app = new SpringApplication(TestApplication.class);
        app.setAdditionalProfiles("test", "test-multiple-query-resolvers-exception");
        // WHEN - THEN
        assertThatExceptionOfType(ApplicationContextException.class)
            .isThrownBy(app::run)
            .withRootCauseExactlyInstanceOf(MultipleQueryResolversException.class);
    }
}
