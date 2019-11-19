package com.oembedler.moon.graphql.boot.error;

import static java.util.Collections.singletonList;

import graphql.GraphQLError;
import graphql.kickstart.execution.error.GenericGraphQLError;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
class ReflectiveGraphQLErrorFactory implements GraphQLErrorFactory {

  private final boolean singularReturnType;
  private Object object;
  private Method method;
  private Throwables throwables;

  ReflectiveGraphQLErrorFactory(Object object, Method method) {
    this.object = object;
    this.method = method;
    singularReturnType = GraphQLError.class.isAssignableFrom(method.getReturnType());

    throwables = new Throwables(method.getAnnotation(ExceptionHandler.class).value());
  }

  @Override
  public Optional<Class<? extends Throwable>> mostConcrete(Throwable t) {
    return throwables.mostConcrete(t);
  }

  @Override
  public Collection<GraphQLError> create(Throwable t) {
    try {
      method.setAccessible(true);
      if (singularReturnType) {
        return singletonList((GraphQLError) method.invoke(object, t));
      }
      return (Collection<GraphQLError>) method.invoke(object, t);
    } catch (IllegalAccessException | InvocationTargetException e) {
      log.error("Cannot create GraphQLError from throwable {}", t.getClass().getSimpleName(), e);
      return singletonList(new GenericGraphQLError(t.getMessage()));
    }
  }

}
