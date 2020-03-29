package graphql.kickstart.graphql.annotations.test.subscription;

import graphql.annotations.annotationTypes.GraphQLDataFetcher;
import graphql.annotations.annotationTypes.GraphQLField;
import graphql.kickstart.tools.GraphQLSubscriptionResolver;

public class TestSubscription implements GraphQLSubscriptionResolver {

    @GraphQLField
    @GraphQLDataFetcher(TestDataFetcher.class)
    public static String testSubscription() {
        return null;
    }
}
