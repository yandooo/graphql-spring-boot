package graphql.kickstart.graphql.annotations.test.prettify;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.kickstart.graphql.annotations.GraphQLQueryResolver;

@GraphQLQueryResolver
public class PrettifyQuery {

  @GraphQLField
  public static String getSomeValue() {
    return "some value";
  }
}
