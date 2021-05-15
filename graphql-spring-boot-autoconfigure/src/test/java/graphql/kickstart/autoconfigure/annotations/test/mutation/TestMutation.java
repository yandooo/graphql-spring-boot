package graphql.kickstart.autoconfigure.annotations.test.mutation;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.kickstart.annotations.GraphQLMutationResolver;
import graphql.kickstart.autoconfigure.annotations.test.mutation.model.TestModel;

@GraphQLMutationResolver
public class TestMutation {

  @GraphQLField
  public static TestModel performSomeOperation() {
    return new TestModel("Test value");
  }

  @GraphQLField
  public static TestModel performSomeOperationWithArgument(TestModel input) {
    return new TestModel("Test value");
  }
}
