package graphql.kickstart.spring.error;

import static java.util.Collections.singletonList;

import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.GraphQLException;
import graphql.SerializationError;
import graphql.kickstart.execution.error.DefaultGraphQLErrorHandler;
import graphql.kickstart.execution.error.GenericGraphQLError;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
class GraphQLErrorFromExceptionHandler extends DefaultGraphQLErrorHandler {

  private List<GraphQLErrorFactory> factories;

  GraphQLErrorFromExceptionHandler(List<GraphQLErrorFactory> factories) {
    this.factories = factories;
  }

  protected List<GraphQLError> filterGraphQLErrors(List<GraphQLError> errors) {
    return errors.stream().map(this::transform).flatMap(Collection::stream).collect(Collectors.toList());
  }

  private Collection<GraphQLError> transform(GraphQLError error) {
    return extractException(error).map(this::transform)
        .orElse(singletonList(new GenericGraphQLError(error.getMessage())));
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

  private Collection<GraphQLError> transform(Throwable throwable) {
    Map<Class<? extends Throwable>, GraphQLErrorFactory> applicables = new HashMap<>();
    factories.forEach(factory -> factory.mostConcrete(throwable).ifPresent(t -> applicables.put(t, factory)));
    return applicables.keySet().stream()
        .min(new ThrowableComparator())
        .map(applicables::get)
        .map(factory -> factory.create(throwable))
        .orElse(singletonList(new ThrowableGraphQLError(throwable)));
  }

}
