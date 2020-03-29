package graphql.kickstart.graphql.annotations.test.prettify;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.kickstart.tools.GraphQLQueryResolver;

public class PrettifyQuery implements GraphQLQueryResolver {

    @GraphQLField
    public static String getSomeValue() {
        return "some value";
    }
}
