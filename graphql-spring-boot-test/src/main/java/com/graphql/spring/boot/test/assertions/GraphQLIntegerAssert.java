package com.graphql.spring.boot.test.assertions;

import com.graphql.spring.boot.test.GraphQLResponse;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.AbstractLongAssert;

public class GraphQLIntegerAssert extends AbstractIntegerAssert<GraphQLIntegerAssert>
    implements GraphQLResponseAssertion {

    private final GraphQLResponse graphQlResponse;

    public GraphQLIntegerAssert(final GraphQLResponse graphQLResponse, final Integer actual) {
        super(actual, GraphQLIntegerAssert.class);
        this.graphQlResponse = graphQLResponse;
    }

    @Override
    public GraphQLResponse and() {
        return graphQlResponse;
    }
}
