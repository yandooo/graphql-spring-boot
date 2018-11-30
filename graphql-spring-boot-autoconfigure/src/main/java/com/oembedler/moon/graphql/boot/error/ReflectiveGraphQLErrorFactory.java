package com.oembedler.moon.graphql.boot.error;

import graphql.GraphQLError;
import graphql.servlet.GenericGraphQLError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

@Slf4j
class ReflectiveGraphQLErrorFactory implements GraphQLErrorFactory {

    private Object object;
    private Method method;
    private Throwables throwables;

    ReflectiveGraphQLErrorFactory(Object object, Method method) {
        this.object = object;
        this.method = method;

        throwables = new Throwables(method.getAnnotation(ExceptionHandler.class).value());
    }

    @Override
    public Optional<Class<? extends Throwable>> mostConcrete(Throwable t) {
        return throwables.mostConcrete(t);
    }

    @Override
    public GraphQLError create(Throwable t) {
        try {
            method.setAccessible(true);
            return (GraphQLError) method.invoke(object, t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("Cannot create GraphQLError from throwable {}", t.getClass().getSimpleName(), e);
            return new GenericGraphQLError(t.getMessage());
        }
    }

}
