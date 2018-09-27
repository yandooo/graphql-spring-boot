package graphql.servlet.examples.dataloader.requestscope;

import graphql.servlet.GraphQLContext;
import graphql.servlet.GraphQLContextBuilder;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.HandshakeRequest;
import java.util.concurrent.CompletableFuture;

@Component
public class CustomGraphQLContextBuilder implements GraphQLContextBuilder {

    private final CustomerRepository customerRepository;

    public CustomGraphQLContextBuilder(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public GraphQLContext build(HttpServletRequest req) {
        GraphQLContext context = new GraphQLContext(req);
        context.setDataLoaderRegistry(buildDataLoaderRegistry());

        return context;
    }

    @Override
    public GraphQLContext build() {
        GraphQLContext context = new GraphQLContext();
        context.setDataLoaderRegistry(buildDataLoaderRegistry());

        return context;
    }

    @Override
    public GraphQLContext build(HandshakeRequest request) {
        GraphQLContext context = new GraphQLContext(request);
        context.setDataLoaderRegistry(buildDataLoaderRegistry());

        return context;
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