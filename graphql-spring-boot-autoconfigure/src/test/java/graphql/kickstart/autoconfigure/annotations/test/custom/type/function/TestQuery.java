package graphql.kickstart.autoconfigure.annotations.test.custom.type.function;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.kickstart.autoconfigure.annotations.GraphQLQueryResolver;

@GraphQLQueryResolver
public class TestQuery {

  @GraphQLField
  public static Foo foo() {
    return new Foo();
  }
}
