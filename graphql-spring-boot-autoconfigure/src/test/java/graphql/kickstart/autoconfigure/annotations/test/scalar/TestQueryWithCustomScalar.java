package graphql.kickstart.autoconfigure.annotations.test.scalar;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.kickstart.autoconfigure.annotations.GraphQLQueryResolver;
import java.util.UUID;

@GraphQLQueryResolver
public class TestQueryWithCustomScalar {

  @GraphQLField
  public static UUID randomUUID() {
    return UUID.randomUUID();
  }
}
