package graphql.kickstart.graphql.annotations.test.mutation;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.kickstart.graphql.annotations.GraphQLMutationResolver;
import graphql.kickstart.graphql.annotations.test.mutation.model.TestModel;

@GraphQLMutationResolver
public class TestMutation {

    @GraphQLField
    public static TestModel performSomeOperation() {
        return new TestModel("Test value");
    }
}
