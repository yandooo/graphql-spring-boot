package com.graphql.spring.boot.test;

import com.graphql.spring.boot.test.assertions.GraphQLLongAssert;
import com.graphql.spring.boot.test.assertions.GraphQLFieldAssert;
import com.jayway.jsonpath.PathNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;

public class GraphQLFieldAssertAsLongTest extends GraphQLFieldAssertTestBase {

    @Test
    @DisplayName("Should return a long assertion (value at specific path is valid long value).")
    void shouldReturnLongAssertIfFieldIsNonNull() {
        // GIVEN
        final Long value = 123L;
        given(graphQLResponse.get(MOCK_PATH, Long.class)).willReturn(value);
        final GraphQLFieldAssert graphQLFieldAssert = new GraphQLFieldAssert(graphQLResponse, MOCK_PATH);
        // WHEN
        final GraphQLLongAssert actual = graphQLFieldAssert.asLong();
        // THEN
        assertThat(actual).isNotNull();
        assertThat(actual.and()).isSameAs(graphQLResponse);
        assertThat(actual.isEqualByComparingTo(value).and()).isSameAs(graphQLResponse);
        assertThat(actual).extracting("actual").isSameAs(value);
    }

    @Test
    @DisplayName("Should return a long assertion (value at specific path is null).")
    void shouldReturnLongAssertIfFieldIsNull() {
        // GIVEN
        given(graphQLResponse.get(MOCK_PATH, Long.class)).willReturn(null);
        final GraphQLFieldAssert graphQLFieldAssert = new GraphQLFieldAssert(graphQLResponse, MOCK_PATH);
        // WHEN
        final GraphQLLongAssert actual = graphQLFieldAssert.asLong();
        // THEN
        assertThat(actual).isNotNull();
        assertThat(actual.and()).isSameAs(graphQLResponse);
        assertThat(actual).extracting("actual").isSameAs(null);
    }

    @Test
    @DisplayName("Should fail if the value at the provided path is missing.")
    void shouldFailIfPathNotFound(final @Mock PathNotFoundException pathNotFoundException) {
        // GIVEN
        given(graphQLResponse.get(MOCK_PATH, Long.class)).willThrow(pathNotFoundException);
        final GraphQLFieldAssert graphQLFieldAssert = new GraphQLFieldAssert(graphQLResponse, MOCK_PATH);
        // WHEN - THEN
        assertThatExceptionOfType(AssertionError.class)
            .isThrownBy(graphQLFieldAssert::asLong)
            .withMessage("Expected field %s to be present.", MOCK_PATH)
            .withCause(pathNotFoundException);
    }

    @Test
    @DisplayName("Should fail if the value at the provided path cannot be converted.")
    void shouldFailIfIsNotNull(final @Mock IllegalArgumentException illegalArgumentException) {
        // GIVEN
        given(graphQLResponse.get(MOCK_PATH, Long.class)).willThrow(illegalArgumentException);
        final GraphQLFieldAssert graphQLFieldAssert = new GraphQLFieldAssert(graphQLResponse, MOCK_PATH);
        // WHEN - THEN
        assertThatExceptionOfType(AssertionError.class)
            .isThrownBy(graphQLFieldAssert::asLong)
            .withMessage("Expected that content of field %s can be converted to %s.", MOCK_PATH,
                Long.class)
            .withCause(illegalArgumentException);
    }
}
