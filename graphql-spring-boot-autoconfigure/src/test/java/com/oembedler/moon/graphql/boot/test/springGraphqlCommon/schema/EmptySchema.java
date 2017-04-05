package com.oembedler.moon.graphql.boot.test.springGraphqlCommon.schema;

import com.oembedler.moon.graphql.engine.stereotype.GraphQLObject;
import com.oembedler.moon.graphql.engine.stereotype.GraphQLSchema;
import com.oembedler.moon.graphql.engine.stereotype.GraphQLSchemaQuery;

/**
 * @author <a href="mailto:java.lang.RuntimeException@gmail.com">oEmbedler Inc.</a>
 */
@GraphQLSchema("EmptySchema")
public class EmptySchema {

    @GraphQLSchemaQuery
    private Root root;

    @GraphQLObject
    public static class Root {

    }
}
