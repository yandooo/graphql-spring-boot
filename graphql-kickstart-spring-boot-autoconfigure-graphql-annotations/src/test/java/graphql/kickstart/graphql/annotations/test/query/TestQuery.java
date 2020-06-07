package graphql.kickstart.graphql.annotations.test.query;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLName;
import graphql.annotations.annotationTypes.GraphQLNonNull;
import graphql.kickstart.graphql.annotations.GraphQLQueryResolver;

import java.util.Optional;

@GraphQLQueryResolver
public class TestQuery {

    @GraphQLField
    @GraphQLNonNull
    public static String hello(final @GraphQLName("who") String who) {
        return String.format("Hello, %s!", Optional.ofNullable(who).orElse("World"));
    }
}
