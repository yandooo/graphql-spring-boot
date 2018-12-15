package com.oembedler.moon.graphql.boot.error;

import graphql.servlet.GraphQLErrorHandler;

import java.util.Objects;
import java.util.function.Supplier;

public class ErrorHandlerSupplier implements Supplier<GraphQLErrorHandler> {

    private GraphQLErrorHandler errorHandler;

    public ErrorHandlerSupplier(GraphQLErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    public GraphQLErrorHandler get() {
        return errorHandler;
    }

    public void setErrorHandler(GraphQLErrorHandler errorHandler) {
        this.errorHandler = Objects.requireNonNull(errorHandler);
    }

}
