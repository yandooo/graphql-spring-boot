package graphql.kickstart.autoconfigure.annotations.test.mutation;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.kickstart.autoconfigure.annotations.GraphQLQueryResolver;

@GraphQLQueryResolver
public class DummyQuery {

  @GraphQLField
  public static String dummyQuery() {
    return "A GraphQL query is required.";
  }
}
