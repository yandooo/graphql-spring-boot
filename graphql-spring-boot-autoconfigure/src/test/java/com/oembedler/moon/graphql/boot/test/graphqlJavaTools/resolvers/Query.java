package com.oembedler.moon.graphql.boot.test.graphqlJavaTools.resolvers;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import org.springframework.stereotype.Component;

/**
 * @author Andrew Potter
 */
@Component
public class Query implements GraphQLRootResolver {
    public String echo(String string) {
        return string;
    }
}
