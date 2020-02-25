package graphql.servlet.examples.dataloader.requestscope;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.kickstart.execution.context.GraphQLContext;
import graphql.schema.DataFetchingEnvironment;
import java.util.concurrent.CompletableFuture;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

@Component
public class CustomerResolver implements GraphQLResolver<Customer> {

  public CompletableFuture<String> getName(Customer customer, DataFetchingEnvironment dfe) {
    final DataLoader<Integer, String> dataloader = ((GraphQLContext) dfe.getContext())
        .getDataLoaderRegistry().get()
        .getDataLoader("customerDataLoader");

    return dataloader.load(customer.getCustomerId());
  }

}
