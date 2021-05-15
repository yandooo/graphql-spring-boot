package graphql.kickstart.autoconfigure.annotations.test.prettify;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.kickstart.annotations.GraphQLQueryResolver;

@GraphQLQueryResolver
public class PrettifyQuery {

  @GraphQLField
  public static String getSomeValue() {
    return "some value";
  }
}
