package graphql.kickstart.graphql.annotations.test.scalar;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.kickstart.graphql.annotations.GraphQLQueryResolver;

import java.util.UUID;

@GraphQLQueryResolver
public class TestQueryWithCustomScalar {

    @GraphQLField
    public static UUID randomUUID() {
        return UUID.randomUUID();
    }
}
