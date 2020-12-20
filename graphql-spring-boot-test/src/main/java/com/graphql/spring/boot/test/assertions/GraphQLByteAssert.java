package com.graphql.spring.boot.test.assertions;

import com.graphql.spring.boot.test.GraphQLResponse;
import org.assertj.core.api.AbstractByteAssert;

public class GraphQLByteAssert extends AbstractByteAssert<GraphQLByteAssert>
    implements GraphQLResponseAssertion {

  private final GraphQLResponse graphQlResponse;

  public GraphQLByteAssert(final GraphQLResponse graphQLResponse, final Byte actual) {
    super(actual, GraphQLByteAssert.class);
    this.graphQlResponse = graphQLResponse;
  }

  @Override
  public GraphQLResponse and() {
    return graphQlResponse;
  }
}
