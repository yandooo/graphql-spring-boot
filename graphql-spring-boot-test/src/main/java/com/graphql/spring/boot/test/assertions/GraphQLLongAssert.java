package com.graphql.spring.boot.test.assertions;

import com.graphql.spring.boot.test.GraphQLResponse;
import lombok.EqualsAndHashCode;
import org.assertj.core.api.AbstractLongAssert;

@EqualsAndHashCode(callSuper = true)
public class GraphQLLongAssert extends AbstractLongAssert<GraphQLLongAssert>
    implements GraphQLResponseAssertion {

  private final GraphQLResponse graphQlResponse;

  public GraphQLLongAssert(final GraphQLResponse graphQLResponse, final Long actual) {
    super(actual, GraphQLLongAssert.class);
    this.graphQlResponse = graphQLResponse;
  }

  @Override
  public GraphQLResponse and() {
    return graphQlResponse;
  }
}
