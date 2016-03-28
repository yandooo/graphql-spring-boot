package com.oembedler.moon.graphql.boot;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:java.lang.RuntimeException@gmail.com">oEmbedler Inc.</a>
 */
public class GraphQLServerResult {
    private List<Map<String, String>> errors;
    private Map<String, Object> data;

    public GraphQLServerResult() {
    }

    public GraphQLServerResult(Map<String, Object> data) {
        this(Collections.EMPTY_LIST, data);
    }

    public GraphQLServerResult(List<Map<String, String>> errors) {
        this(errors, Collections.EMPTY_MAP);
    }

    public GraphQLServerResult(List<Map<String, String>> errors, Map<String, Object> data) {
        this.errors = errors;
        this.data = data;
    }

    public List<Map<String, String>> getErrors() {
        return errors;
    }

    public void setErrors(List<Map<String, String>> errors) {
        this.errors = errors;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
