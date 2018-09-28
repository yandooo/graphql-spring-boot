package graphql.servlet.examples.dataloader.requestscope;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import graphql.servlet.GraphQLContext;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class CustomerResolver implements GraphQLResolver<Customer> {

    public CompletableFuture<String> getName(Customer customer, DataFetchingEnvironment dfe) {
        final DataLoader<Integer, String> dataloader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader("customerDataLoader");

        return dataloader.load(customer.getCustomerId());
    }
}
