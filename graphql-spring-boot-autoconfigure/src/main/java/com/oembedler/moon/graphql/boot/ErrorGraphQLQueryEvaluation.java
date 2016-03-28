package com.oembedler.moon.graphql.boot;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.List;

/**
 * @author <a href="mailto:java.lang.RuntimeException@gmail.com">oEmbedler Inc.</a>
 */
public class ErrorGraphQLQueryEvaluation implements GraphQLError {

    @Override
    public String getMessage() {
        return "Error evaluating GraphQL schema";
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.ValidationError;
    }
}
