package com.oembedler.moon.graphql.boot.error;

import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.GraphQLException;
import graphql.SerializationError;
import graphql.servlet.DefaultGraphQLErrorHandler;
import graphql.servlet.GenericGraphQLError;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
class GraphQLErrorFromExceptionHandler extends DefaultGraphQLErrorHandler {

    private List<GraphQLErrorFactory> factories;

    GraphQLErrorFromExceptionHandler(List<GraphQLErrorFactory> factories) {
        this.factories = factories;
    }

    protected List<GraphQLError> filterGraphQLErrors(List<GraphQLError> errors) {
        return errors.stream().map(this::transform).collect(Collectors.toList());
    }

    private GraphQLError transform(GraphQLError error) {
        return extractException(error).map(this::transform).orElse(defaultError(error.getMessage()));
    }

    private Optional<Throwable> extractException(GraphQLError error) {
        if (error instanceof ExceptionWhileDataFetching) {
            return Optional.of(((ExceptionWhileDataFetching) error).getException());
        } else if (error instanceof SerializationError) {
            return Optional.of(((SerializationError) error).getException());
        } else if (error instanceof GraphQLException) {
            return Optional.of((GraphQLException) error);
        }
        return Optional.empty();
    }

    private GraphQLError transform(Throwable throwable) {
        Map<Class<? extends Throwable>, GraphQLErrorFactory> applicables = new HashMap<>();
        factories.forEach(factory -> factory.mostConcrete(throwable).ifPresent(t -> applicables.put(t, factory)));
        return applicables.keySet().stream()
                .min(new ThrowableComparator())
                .map(applicables::get)
                .map(factory -> factory.create(throwable))
                .orElse(this.defaultError(throwable));
    }

    private GraphQLError defaultError(Throwable throwable) {
        return new ThrowableGraphQLError(throwable);
    }

    private GraphQLError defaultError(String message) {
        return new GenericGraphQLError(message);
    }

}
