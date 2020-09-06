package graphql.kickstart.graphql.annotations.test.directive;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.kickstart.graphql.annotations.GraphQLQueryResolver;

@GraphQLQueryResolver
public class TestQueryWithDirective {

    @GraphQLField
    @UpperCaseDirective
    public static String queryWithDirective() {
        return "this should be uppercase";
    }
}
