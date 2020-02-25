package com.oembedler.moon.graphql.boot.resolvers;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

@Component
class Query implements GraphQLQueryResolver {

    public String hello() {
        return "Hello world!";
    }

}
