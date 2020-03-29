package graphql.kickstart.graphql.annotations.test.custom.annotation.processor;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.kickstart.tools.GraphQLQueryResolver;

public class TestQuery implements GraphQLQueryResolver {

    @GraphQLField
    public String someQuery() {
        return "some value";
    }
}
