package graphql.kickstart.graphql.annotations.test.mutation;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.kickstart.graphql.annotations.test.mutation.model.TestModel;
import graphql.kickstart.tools.GraphQLMutationResolver;

public class TestMutation implements GraphQLMutationResolver {

    @GraphQLField
    public static TestModel performSomeOperation() {
        return new TestModel("Test value");
    }
}
