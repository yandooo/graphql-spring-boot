package graphql.servlet.examples.dataloader.requestscope;

import graphql.servlet.context.DefaultGraphQLContext;
import graphql.servlet.context.DefaultGraphQLServletContext;
import graphql.servlet.context.DefaultGraphQLWebSocketContext;
import graphql.servlet.context.GraphQLContext;
import graphql.servlet.context.GraphQLContextBuilder;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import java.util.concurrent.CompletableFuture;

@Component
public class CustomGraphQLContextBuilder implements GraphQLContextBuilder {

    private final CustomerRepository customerRepository;

    public CustomGraphQLContextBuilder(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public GraphQLContext build(HttpServletRequest req, HttpServletResponse response) {
        return DefaultGraphQLServletContext.createServletContext(buildDataLoaderRegistry(), null).with(req).with(response).build();
    }

    @Override
    public GraphQLContext build() {
        return new DefaultGraphQLContext(buildDataLoaderRegistry(), null);
    }

    @Override
    public GraphQLContext build(Session session, HandshakeRequest request) {
        return DefaultGraphQLWebSocketContext.createWebSocketContext(buildDataLoaderRegistry(), null).with(session).with(request).build();
    }

    private DataLoaderRegistry buildDataLoaderRegistry() {
        DataLoaderRegistry dataLoaderRegistry = new DataLoaderRegistry();
        dataLoaderRegistry.register("customerDataLoader",
                new DataLoader<Integer, String>(customerIds ->
                        CompletableFuture.supplyAsync(() ->
                                customerRepository.getUserNamesForIds(customerIds))));
        return dataLoaderRegistry;
    }
}
