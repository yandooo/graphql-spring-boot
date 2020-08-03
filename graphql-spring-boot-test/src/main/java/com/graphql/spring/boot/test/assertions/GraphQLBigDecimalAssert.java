package com.graphql.spring.boot.test.assertions;

import com.graphql.spring.boot.test.GraphQLResponse;
import org.assertj.core.api.AbstractBigDecimalAssert;

import java.math.BigDecimal;

public class GraphQLBigDecimalAssert extends AbstractBigDecimalAssert<GraphQLBigDecimalAssert>
    implements GraphQLResponseAssertion {

    private final GraphQLResponse graphQlResponse;

    public GraphQLBigDecimalAssert(final GraphQLResponse graphQLResponse, final BigDecimal actual) {
        super(actual, GraphQLBigDecimalAssert.class);
        this.graphQlResponse = graphQLResponse;
    }

    @Override
    public GraphQLResponse and() {
        return graphQlResponse;
    }
}
