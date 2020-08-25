package com.graphql.spring.boot.test.beans;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Service;

@Service
public class DummyQuery implements GraphQLQueryResolver {

    public boolean dummy() {
        return true;
    }

    public String otherQuery() {
        return "TEST";
    }

    public FooBar fooBar() {
        return FooBar.builder().bar("BAR").foo("FOO").build();
    }

    public String queryWithVariables(final String input) {
        return input;
    }
}
