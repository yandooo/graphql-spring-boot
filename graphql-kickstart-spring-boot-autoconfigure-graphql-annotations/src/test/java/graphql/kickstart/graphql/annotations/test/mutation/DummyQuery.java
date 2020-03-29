package graphql.kickstart.graphql.annotations.test.mutation;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.kickstart.tools.GraphQLQueryResolver;

public class DummyQuery implements GraphQLQueryResolver {

    @GraphQLField
    public static String dummyQuery() {
        return "A GraphQL query is required.";
    }
}
