package graphql.kickstart.graphql.annotations.test.query;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLName;
import graphql.annotations.annotationTypes.GraphQLNonNull;
import graphql.kickstart.tools.GraphQLQueryResolver;

import java.util.Optional;

public class TestQuery implements GraphQLQueryResolver {

    @GraphQLField
    @GraphQLNonNull
    public static String hello(final @GraphQLName("who") String who) {
        return String.format("Hello, %s!", Optional.ofNullable(who).orElse("World"));
    }
}
