package graphql.kickstart.spring.boot.graphql.annotations.example.resolvers;

import graphql.annotations.annotationTypes.GraphQLDataFetcher;
import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLNonNull;
import graphql.kickstart.tools.GraphQLSubscriptionResolver;

/**
 * Same restrictions/considerations apply as for the query resolver.
 * @see Queries
 */
public class Subscriptions implements GraphQLSubscriptionResolver {

    /**
     * The subscription resolver should *not* return a {@link org.reactivestreams.Publisher} by itself. Instead,
     * it should provide a {@link GraphQLDataFetcher} that return the publisher, and this method should return the
     * actual type returned by this subscription.
     *
     * THe body of this class will be ignored, as the values from the data fetcher will be used. It is fine to return
     * null here, even if the resolver is marked as non-null.
     *
     * @return values emitted by the publisher provided by the data fetcher.
     */
    @GraphQLField
    @GraphQLNonNull
    @GraphQLDataFetcher(TimerFetcher.class)
    public static Long timer() {
        return null;
    }
}
