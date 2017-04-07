package com.embedler.moon.graphql.boot.sample;

import com.oembedler.moon.graphql.engine.relay.ConnectionObjectType;
import com.oembedler.moon.graphql.engine.relay.EdgeObjectType;
import com.oembedler.moon.graphql.engine.relay.PageInfoObjectType;
import graphql.relay.Base64;
import graphql.relay.ConnectionCursor;
import graphql.relay.DefaultConnectionCursor;
import graphql.schema.DataFetchingEnvironment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:java.lang.RuntimeException@gmail.com">oEmbedler Inc.</a>
 */
public class SimpleListConnection {

    private static final String DUMMY_CURSOR_PREFIX = "simple-cursor";
    private List<?> data = new ArrayList<Object>();

    public SimpleListConnection(List<?> data) {
        this.data = data;
    }

    public <T extends EdgeObjectType> T createEdgeObject() {
        return (T) new EdgeObjectType();
    }

    public <T extends ConnectionObjectType> T createConnectionObject() {
        return (T) new ConnectionObjectType();
    }

    private List<EdgeObjectType> buildEdges() {
        List<EdgeObjectType> edges = new ArrayList<>();
        int ix = 0;
        for (Object object : data) {
            EdgeObjectType edge = createEdgeObject();
            edge.setNode(object);
            edge.setCursor(createCursor(ix++));
            edges.add(edge);
        }
        return edges;
    }

    public <T extends ConnectionObjectType> T get(DataFetchingEnvironment environment) {

        List<EdgeObjectType> edges = buildEdges();

        int afterOffset = getOffsetFromCursor(environment.<String>getArgument("after"), -1);
        int begin = Math.max(afterOffset, -1) + 1;
        int beforeOffset = getOffsetFromCursor(environment.<String>getArgument("before"), edges.size());
        int end = Math.min(beforeOffset, edges.size());

        edges = edges.subList(begin, end);
        if (edges.size() == 0) {
            return emptyConnection();
        }

        Integer first = environment.<Integer>getArgument("first");
        Integer last = environment.<Integer>getArgument("last");

        String firstPresliceCursor = edges.get(0).getCursor();
        String lastPresliceCursor = edges.get(edges.size() - 1).getCursor();

        if (first != null) {
            edges = edges.subList(0, first <= edges.size() ? first : edges.size());
        }
        if (last != null) {
            edges = edges.subList(edges.size() - last, edges.size());
        }

        if (edges.size() == 0) {
            return emptyConnection();
        }

        EdgeObjectType firstEdge = edges.get(0);
        EdgeObjectType lastEdge = edges.get(edges.size() - 1);

        PageInfoObjectType pageInfo = new PageInfoObjectType();
        pageInfo.setStartCursor(firstEdge.getCursor());
        pageInfo.setEndCursor(lastEdge.getCursor());
        pageInfo.setHasPreviousPage(!firstEdge.getCursor().equals(firstPresliceCursor));
        pageInfo.setHasNextPage(!lastEdge.getCursor().equals(lastPresliceCursor));

        ConnectionObjectType connection = createConnectionObject();
        connection.setEdges(edges);
        connection.setPageInfo(pageInfo);

        return (T) connection;
    }

    private <T extends ConnectionObjectType> T emptyConnection() {
        ConnectionObjectType connection = createConnectionObject();
        connection.setPageInfo(new PageInfoObjectType());
        return (T) connection;
    }

    public ConnectionCursor cursorForObjectInConnection(Object object) {
        int index = data.indexOf(object);
        String cursor = createCursor(index);
        return new DefaultConnectionCursor(cursor);
    }

    private int getOffsetFromCursor(String cursor, int defaultValue) {
        if (cursor == null) return defaultValue;
        String string = Base64.fromBase64(cursor);
        return Integer.parseInt(string.substring(DUMMY_CURSOR_PREFIX.length()));
    }

    private String createCursor(int offset) {
        String string = Base64.toBase64(DUMMY_CURSOR_PREFIX + Integer.toString(offset));
        return string;
    }

}