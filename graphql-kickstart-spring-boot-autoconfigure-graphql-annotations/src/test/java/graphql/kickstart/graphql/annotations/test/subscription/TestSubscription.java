package graphql.kickstart.graphql.annotations.test.subscription;

import graphql.annotations.annotationTypes.GraphQLDataFetcher;
import graphql.annotations.annotationTypes.GraphQLField;
import graphql.kickstart.graphql.annotations.GraphQLSubscriptionResolver;

@GraphQLSubscriptionResolver
public class TestSubscription {

  @GraphQLField
  @GraphQLDataFetcher(TestDataFetcher.class)
  public static String testSubscription() {
    return null;
  }
}
