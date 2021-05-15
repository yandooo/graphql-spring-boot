package graphql.kickstart.autoconfigure.annotations.test.extend.type;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.kickstart.annotations.GraphQLQueryResolver;
import graphql.kickstart.autoconfigure.annotations.test.extend.type.model.BaseType;

@GraphQLQueryResolver
public class TestQuery {

  @GraphQLField
  public static BaseType someValue() {
    return new BaseType("Test value");
  }
}
