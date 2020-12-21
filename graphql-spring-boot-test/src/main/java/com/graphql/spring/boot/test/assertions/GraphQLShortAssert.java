package com.graphql.spring.boot.test.assertions;

import com.graphql.spring.boot.test.GraphQLResponse;
import lombok.EqualsAndHashCode;
import org.assertj.core.api.AbstractShortAssert;

@EqualsAndHashCode(callSuper = true)
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
