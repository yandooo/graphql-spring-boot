package com.graphql.spring.boot.test.assertions;

import com.graphql.spring.boot.test.GraphQLResponse;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.AbstractShortArrayAssert;
import org.assertj.core.api.AbstractShortAssert;

public class GraphQLShortAssert extends AbstractShortAssert<GraphQLShortAssert>
    implements GraphQLResponseAssertion {

    private final GraphQLResponse graphQlResponse;

    public GraphQLShortAssert(final GraphQLResponse graphQLResponse, final Short actual) {
        super(actual, GraphQLShortAssert.class);
        this.graphQlResponse = graphQLResponse;
    }

    @Override
    public GraphQLResponse and() {
        return graphQlResponse;
    }
}
