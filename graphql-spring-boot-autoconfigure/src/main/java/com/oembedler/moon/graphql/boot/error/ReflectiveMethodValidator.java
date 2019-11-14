package com.oembedler.moon.graphql.boot.error;

import graphql.GraphQLError;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ReflectiveMethodValidator {

  static boolean isGraphQLExceptionHandler(Method method) {
    return method.isAnnotationPresent(ExceptionHandler.class) && (
        isGraphQLErrorReturnType(method) || hasGraphQLErrorCollectionReturnType(method)
    );
  }

  static private boolean isGraphQLErrorReturnType(Method method) {
    return GraphQLError.class.isAssignableFrom(method.getReturnType());
  }

  static private boolean hasGraphQLErrorCollectionReturnType(Method method) {
    if (Collection.class.isAssignableFrom(method.getReturnType())) {
      ParameterizedType collectionType = (ParameterizedType) method.getGenericReturnType();
      if (collectionType.getActualTypeArguments().length == 1) {
        return GraphQLError.class.isAssignableFrom((Class<?>) collectionType.getActualTypeArguments()[0]);
      }
    }
    return false;
  }

}
