package com.graphql.spring.boot.test.assertions;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.jayway.jsonpath.PathNotFoundException;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.fail;

/**
 * Provides fluent assertions for a field (specified by a json path) of the GraphQL response.
 */
@RequiredArgsConstructor
public class GraphQLFieldAssert implements GraphQLResponseAssertion {

    private final GraphQLResponse graphQLResponse;
    private final String jsonPath;

    /**
     * Asserts that the field specified by the provided JSON path is not present in the response.
     * @throws AssertionError if the field is present in the response
     * @return self
     */
    public GraphQLFieldAssert isNotPresent() {
        try {
            graphQLResponse.getRaw(jsonPath);
            fail("Expected that field %s is not present.", jsonPath);
        } catch (PathNotFoundException e) {
            // test passed
        }
        return this;
    }

    /**
     * Asserts that the field specified by the provided JSON path is not present in the response or its value is null.
     * @throws AssertionError if the field is present in the response and its value is not null.
     * @return self
     */
    public GraphQLFieldAssert isNotPresentOrNull() {
        try {
            if (nonNull(graphQLResponse.getRaw(jsonPath))) {
                fail("Expected field %s to be null or not present.", jsonPath);
            }
        } catch (PathNotFoundException e) {
            // test passed
        }
        return this;
    }

    /**
     * Asserts that the field specified by the provided JSON path is null.
     * @throws AssertionError if the field is not present in the response or its value is not null.
     * @return self
     */
    public GraphQLFieldAssert isNull() {
        try {
            if(nonNull(graphQLResponse.getRaw(jsonPath))) {
                fail("Expected field %s to be null.", jsonPath);
            }
        } catch (PathNotFoundException e) {
            fail(String.format("Expected field %s to be present.", jsonPath), e);
        }
        return this;
    }

    /**
     * Asserts that the field specified by the provided JSON path is not null.
     * @throws AssertionError if the field is not present in the response or its value is null.
     * @return self
     */
    public GraphQLFieldAssert isNotNull() {
        try {
            if (Objects.isNull(graphQLResponse.getRaw(jsonPath))) {
                fail("Expected field %s to be non-null.", jsonPath);
            }
        } catch (PathNotFoundException e) {
            fail(String.format("Expected field %s to be present.", jsonPath), e);
        }
        return this;
    }

    /**
     * Returns an assertion for the content of the field as {@link BigDecimal}.
     * @return a {@link GraphQLBigDecimalAssert} instance
     * @throws AssertionError if the path does not exist or the content could not be converted to {@link BigDecimal}
     */
    public GraphQLBigDecimalAssert asBigDecimal() {
        return new GraphQLBigDecimalAssert(graphQLResponse, getFieldAs(BigDecimal.class));
    }

    /**
     * Returns an assertion for the content of the field as {@link BigInteger}.
     * @return a {@link GraphQLBigIntegerAssert} instance
     * @throws AssertionError if the path does not exist or the content could not be converted to {@link BigInteger}
     */
    public GraphQLBigIntegerAssert asBigInteger() {
        return new GraphQLBigIntegerAssert(graphQLResponse, getFieldAs(BigInteger.class));
    }

    /**
     * Returns an assertion for the content of the field as {@link Long}.
     * @return a {@link GraphQLLongAssert} instance
     * @throws AssertionError if the path does not exist or the content could not be converted to {@link Long}
     */
    public GraphQLLongAssert asLong() {
        return new GraphQLLongAssert(graphQLResponse, getFieldAs(Long.class));
    }

    /**
     * Returns an assertion for the content of the field as {@link Integer}.
     * @return a {@link GraphQLIntegerAssert} instance
     * @throws AssertionError if the path does not exist or the content could not be converted to {@link Integer}
     */
    public GraphQLIntegerAssert asInteger() {
        return new GraphQLIntegerAssert(graphQLResponse, getFieldAs(Integer.class));
    }

    /**
     * Returns an assertion for the content of the field as {@link Short}.
     * @return a {@link GraphQLShortAssert} instance
     * @throws AssertionError if the path does not exist or the content could not be converted to {@link Short}
     */
    public GraphQLShortAssert asShort() {
        return new GraphQLShortAssert(graphQLResponse, getFieldAs(Short.class));
    }

    /**
     * Returns an assertion for the content of the field as {@link Byte}.
     * @return a {@link GraphQLByteAssert} instance
     * @throws AssertionError if the path does not exist or the content could not be converted to {@link Byte}
     */
    public GraphQLByteAssert asByte() {
        return new GraphQLByteAssert(graphQLResponse, getFieldAs(Byte.class));
    }

    /**
     * Returns an assertion for the content of the field as {@link Boolean}.
     * @return a {@link GraphQLBooleanAssert} instance
     * @throws AssertionError if the path does not exist or the content could not be converted to {@link Boolean}
     */
    public GraphQLBooleanAssert asBoolean() {
        return new GraphQLBooleanAssert(graphQLResponse, getFieldAs(Boolean.class));
    }

    /**
     * Returns an assertion for the content of the field as {@link String}.
     * @return a {@link GraphQLStringAssert} instance
     * @throws AssertionError if the path does not exist or the content could not be converted to {@link String}
     */
    public GraphQLStringAssert asString() {
        return new GraphQLStringAssert(graphQLResponse, getFieldAs(String.class));
    }

    /**
     * Returns an assertion for the content of the field as an instance of the specified class.
     * @return a {@link GraphQLGenericObjectAssert} instance
     * @throws AssertionError if the path does not exist or the content could not be converted to the specified class
     */
    public <T> GraphQLGenericObjectAssert<T> as(final Class<T> clazz) {
        return new GraphQLGenericObjectAssert<>(graphQLResponse, getFieldAs(clazz));
    }

    /**
     * Returns an assertion for the content of the field as list of objects.
     * @param elementClass the type of objects in the list
     * @return a {@link GraphQLGenericObjectAssert} instance
     * @throws AssertionError if the path does not exist or the content could not be converted to the specified class
     */
    public <T> GraphQLListAssert<T> asListOf(final Class<T> elementClass) {
        return new GraphQLListAssert<>(graphQLResponse, getFieldAsList(elementClass));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GraphQLResponse and() {
        return graphQLResponse;
    }

    private <T> T getFieldAs(final Class<T> targetClass) {
        try {
            return graphQLResponse.get(jsonPath, targetClass);
        } catch (PathNotFoundException e) {
            fail(String.format("Expected field %s to be present.", jsonPath), e);
            return null;
        } catch (IllegalArgumentException e) {
            fail(String.format("Expected that content of field %s can be converted to %s.", jsonPath, targetClass), e);
            return null;
        }
    }

    private <T> List<T> getFieldAsList(final Class<T> targetClass) {
        try {
            return graphQLResponse.getList(jsonPath, targetClass);
        } catch (PathNotFoundException e) {
            fail(String.format("Expected field %s to be present.", jsonPath), e);
            return null;
        } catch (IllegalArgumentException e) {
            fail(String.format("Expected that content of field %s can be converted to %s.", jsonPath, targetClass), e);
            return null;
        }
    }
}
