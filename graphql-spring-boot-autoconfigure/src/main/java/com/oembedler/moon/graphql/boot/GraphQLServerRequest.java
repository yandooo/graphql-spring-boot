package com.oembedler.moon.graphql.boot;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:java.lang.RuntimeException@gmail.com">oEmbedler Inc.</a>
 */
public class GraphQLServerRequest {
    private String query;
    private Map<String, Object> variables = new HashMap<>();

    public GraphQLServerRequest() {
    }

    public GraphQLServerRequest(String query) {
        this.query = query;
    }

    public GraphQLServerRequest(String query, Map<String, Object> variables) {
        this.query = query;
        this.variables = variables;
    }

    public String getQuery() {
        return query;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }
}
