package graphql.kickstart.graphql.annotations.test.subscription;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.kickstart.graphql.annotations.GraphQLQueryResolver;

@GraphQLQueryResolver
public class DummyQuery {

    @GraphQLField
    public static String dummyQuery() {
        return "A GraphQL query is required.";
    }
}
