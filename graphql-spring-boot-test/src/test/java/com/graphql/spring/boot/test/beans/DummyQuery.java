package com.graphql.spring.boot.test.beans;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Service;

@Service
public class DummyQuery implements GraphQLQueryResolver {

    public boolean dummy() {
        return true;
    }
}
