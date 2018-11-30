package com.oembedler.moon.graphql.boot.error;

import graphql.GraphQLError;

import java.lang.reflect.Method;
import java.util.Optional;

interface GraphQLErrorFactory {

    Optional<Class<? extends Throwable>> mostConcrete(Throwable t);

    GraphQLError create(Throwable t);

    static GraphQLErrorFactory withReflection(Object object, Method method) {
        return new ReflectiveGraphQLErrorFactory(object, method);
    }

}
