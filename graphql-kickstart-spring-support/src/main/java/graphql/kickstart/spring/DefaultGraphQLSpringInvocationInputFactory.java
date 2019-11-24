package graphql.kickstart.spring;

import graphql.kickstart.execution.GraphQLRequest;
import graphql.kickstart.execution.input.GraphQLBatchedInvocationInput;
import graphql.kickstart.execution.input.GraphQLSingleInvocationInput;
import java.util.Collection;
import java.util.function.Supplier;
import org.springframework.web.server.ServerWebExchange;

public class DefaultGraphQLSpringInvocationInputFactory implements GraphQLSpringInvocationInputFactory {

  private Supplier<GraphQLSpringContextBuilder> contextBuilderSupplier = () -> (GraphQLSpringServerWebExchangeContext::new);
  private Supplier<GraphQLSpringRootObjectBuilder> rootObjectBuilderSupplier = () -> (serverWebExchange -> new Object());

  public DefaultGraphQLSpringInvocationInputFactory(
      GraphQLSpringContextBuilder contextBuilder,
      GraphQLSpringRootObjectBuilder rootObjectBuilder
  ) {
    if (contextBuilder != null) {
      contextBuilderSupplier = () -> contextBuilder;
    }
    if (rootObjectBuilder != null) {
      rootObjectBuilderSupplier = () -> rootObjectBuilder;
    }
  }

  public DefaultGraphQLSpringInvocationInputFactory(
      Supplier<GraphQLSpringContextBuilder> contextBuilderSupplier,
      Supplier<GraphQLSpringRootObjectBuilder> rootObjectBuilderSupplier
  ) {
    if (contextBuilderSupplier != null) {
      this.contextBuilderSupplier = contextBuilderSupplier;
    }
    if (rootObjectBuilderSupplier != null) {
      this.rootObjectBuilderSupplier = rootObjectBuilderSupplier;
    }
  }

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

  protected Supplier<GraphQLSpringContextBuilder> getContextBuilderSupplier() {
    return contextBuilderSupplier;
  }

  protected Supplier<GraphQLSpringRootObjectBuilder> getRootObjectBuilderSupplier() {
    return rootObjectBuilderSupplier;
  }

}
