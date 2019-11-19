package graphql.kickstart.spring;

import graphql.kickstart.execution.GraphQLRequest;
import graphql.kickstart.execution.input.GraphQLBatchedInvocationInput;
import graphql.kickstart.execution.input.GraphQLSingleInvocationInput;
import java.util.Collection;
import java.util.function.Supplier;
import org.springframework.web.server.ServerWebExchange;

public class DefaultGraphQLSpringInvocationInputFactory implements GraphQLSpringInvocationInputFactory {

  private Supplier<GraphQLSpringContextBuilder> contextBuilderSupplier = () -> (DefaultGraphQLSpringContext::new);
  private Supplier<GraphQLSpringRootObjectBuilder> rootObjectBuilderSupplier = () -> (serverWebExchange -> new Object());

  @Override
  public GraphQLSingleInvocationInput create(GraphQLRequest graphQLRequest, ServerWebExchange serverWebExchange) {
    return new GraphQLSingleInvocationInput(
        graphQLRequest,
        null,
        contextBuilderSupplier.get().build(serverWebExchange),
        rootObjectBuilderSupplier.get().build(serverWebExchange)
    );
  }

  @Override
  public GraphQLBatchedInvocationInput create(Collection<GraphQLRequest> graphQLRequests,
      ServerWebExchange serverWebExchange) {
    throw new UnsupportedOperationException("Batch queries not suppoprted yet");
  }

}
