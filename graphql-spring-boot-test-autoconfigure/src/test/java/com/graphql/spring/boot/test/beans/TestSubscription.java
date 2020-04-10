package com.graphql.spring.boot.test.beans;

import graphql.kickstart.tools.GraphQLSubscriptionResolver;
import io.reactivex.Flowable;
import org.springframework.stereotype.Service;

@Service
public class TestSubscription implements GraphQLSubscriptionResolver {

    public Flowable<String> testSubscription() {
        return Flowable.just("foo");
    }
}
