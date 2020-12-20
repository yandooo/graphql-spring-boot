package graphql.kickstart.graphql.annotations.test.custom.annotation.processor;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.kickstart.graphql.annotations.GraphQLQueryResolver;

@GraphQLQueryResolver
public class TestQuery {

  @GraphQLField
  public String someQuery() {
    return "some value";
  }
}
