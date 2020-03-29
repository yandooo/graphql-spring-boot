package graphql.kickstart.graphql.annotations.test.scalar;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.kickstart.tools.GraphQLQueryResolver;

import java.util.UUID;

public class TestQueryWithCustomScalar implements GraphQLQueryResolver {

    @GraphQLField
    public static UUID randomUUID() {
        return UUID.randomUUID();
    }
}
