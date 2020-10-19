package com.graphql.spring.boot.test;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class GraphQLTestErrorToStringTest {

    private static Stream<Arguments> toStringTestArguments() {
        return Stream.of(
            Arguments.of(
                new GraphQLTestError(null, null, null, null, null),
                "<Unspecified error>: <error message not provided>"
            ),
            Arguments.of(
                new GraphQLTestError("Test message", null, null, null, null),
                "<Unspecified error>: Test message"
            ),
            Arguments.of(
                new GraphQLTestError("Test message", null, ErrorType.DataFetchingException, null, null),
                "DataFetchingException: Test message"
            ),
            Arguments.of(
                new GraphQLTestError("Test message", Collections.emptyList(), ErrorType.DataFetchingException, null, null),
                "DataFetchingException: Test message"
            ),
            Arguments.of(
                new GraphQLTestError("Test message", Collections.singletonList(new SourceLocation(1, 2)),
                    ErrorType.DataFetchingException, null, null),
                "DataFetchingException: Test message at line 1, column 2 in unnamed/unspecified source"
            ),
            Arguments.of(
                new GraphQLTestError("Test message", Collections.singletonList(new SourceLocation(1, 2)),
                    ErrorType.DataFetchingException, Collections.emptyList(), null),
                "DataFetchingException: Test message at line 1, column 2 in unnamed/unspecified source"
            ),
            Arguments.of(
                new GraphQLTestError("Test message", Collections.singletonList(new SourceLocation(1, 2)),
                    ErrorType.DataFetchingException, Arrays.asList("path", "to", "error"), null),
                "DataFetchingException: Test message at line 1, column 2 in unnamed/unspecified source. Selection path: path/to/error"
            ),
            Arguments.of(
                new GraphQLTestError("Test message", Collections.singletonList(new SourceLocation(1, 2, "test.graphql")),
                    ErrorType.DataFetchingException, Arrays.asList("path", "to", "error"), null),
                "DataFetchingException: Test message at line 1, column 2 in test.graphql. Selection path: path/to/error"
            ),
            Arguments.of(
                new GraphQLTestError("Test message", Collections.singletonList(new SourceLocation(1, 2)),
                    ErrorType.DataFetchingException, Arrays.asList("path", 123, "error"), null),
                "DataFetchingException: Test message at line 1, column 2 in unnamed/unspecified source. Selection path: path[123]/error"
            ),
            Arguments.of(
                new GraphQLTestError("Test message", Collections.singletonList(new SourceLocation(1, 2)),
                    ErrorType.DataFetchingException, Arrays.asList("path", 123, "error"), Collections.singletonMap("please ignore", "this")),
                "DataFetchingException: Test message at line 1, column 2 in unnamed/unspecified source. Selection path: path[123]/error"
            )
        );
    }

    @ParameterizedTest(name = "{1}")
    @MethodSource("toStringTestArguments")
    void testGraphQLTestErrorToString(final GraphQLError error, final String expectedToString) {
        assertThat(error.toString()).isEqualTo(expectedToString);
    }
}
