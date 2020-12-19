package com.graphql.spring.boot.test.assertions;

import com.graphql.spring.boot.test.GraphQLResponse;
import org.assertj.core.api.AbstractBooleanAssert;

public class GraphQLBooleanAssert extends AbstractBooleanAssert<GraphQLBooleanAssert>
    implements GraphQLResponseAssertion {

    private final GraphQLResponse graphQlResponse;

    public GraphQLBooleanAssert(final GraphQLResponse graphQLResponse, final Boolean actual) {
        super(actual, GraphQLBooleanAssert.class);
        this.graphQlResponse = graphQLResponse;
    }

    @Override
    public GraphQLResponse and() {
        return graphQlResponse;
    }
}
