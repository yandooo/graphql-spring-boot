package graphql.kickstart.graphql.annotations.test.directive;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.kickstart.tools.GraphQLQueryResolver;

public class TestQueryWithDirective implements GraphQLQueryResolver {

    @GraphQLField
    @UpperCaseDirective
    public static String queryWithDirective() {
        return "this should be uppercase";
    }
}
