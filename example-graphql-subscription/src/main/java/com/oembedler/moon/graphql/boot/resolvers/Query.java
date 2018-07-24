package com.oembedler.moon.graphql.boot.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

@Component
class Query implements GraphQLQueryResolver {

    public String hello() {
        return "Hello world!";
    }

}
