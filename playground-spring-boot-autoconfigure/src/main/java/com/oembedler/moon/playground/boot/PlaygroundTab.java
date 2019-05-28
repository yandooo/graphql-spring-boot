package com.oembedler.moon.playground.boot;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PlaygroundTab {

    /**
     * The GraphQL endpoint for this tab. If not set, the default GraphQL endpoint will be used.
     */
    private String endpoint;

    /**
     * The GraphQL query (operation) to be initially displayed on the tab.
     */
    private String query;

    /**
     * The name of the tab.
     */
    private String name;

    /**
     * The query variables. It should be a serialized JSON.
     */
    private String variables;

    /**
     * The list of responses to be displayed under "responses". Should be a list of serialized JSONs.
     */
    private List<String> responses;

    /**
     * HTTP headers. Key-value pairs expected.
     */
    private Map<String, String> headers;
}
