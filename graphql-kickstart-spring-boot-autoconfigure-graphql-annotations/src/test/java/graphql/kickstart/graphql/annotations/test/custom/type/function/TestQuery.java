package graphql.kickstart.graphql.annotations.test.custom.type.function;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.kickstart.graphql.annotations.GraphQLQueryResolver;

@GraphQLQueryResolver
public class TestQuery {

  @GraphQLField
  public static Foo foo() {
    return new Foo();
  }
}
