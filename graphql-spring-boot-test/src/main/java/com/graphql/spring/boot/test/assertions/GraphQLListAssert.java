package com.graphql.spring.boot.test.assertions;

import com.graphql.spring.boot.test.GraphQLResponse;
import org.assertj.core.api.FactoryBasedNavigableListAssert;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.ObjectAssertFactory;

import java.util.List;

public class GraphQLListAssert<T> extends FactoryBasedNavigableListAssert<
    GraphQLListAssert<T>,
    List<? extends T>,
    T,
    ObjectAssert<T>
    >
    implements GraphQLResponseAssertion {

    private final GraphQLResponse graphQlResponse;

    public GraphQLListAssert(final GraphQLResponse graphQLResponse, final List<T> actual) {
        super(actual, GraphQLListAssert.class, new ObjectAssertFactory<>());
        this.graphQlResponse = graphQLResponse;
    }

    @Override
    public GraphQLResponse and() {
        return graphQlResponse;
    }
}