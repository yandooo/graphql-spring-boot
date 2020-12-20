package graphql.servlet.examples.dataloader.requestscope;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CustomerRepository {

  private final Map<Integer, String> data;

  private CustomerRepository() {
    data = new HashMap<>();
    data.put(101, "Customer Name 1");
    data.put(102, "Customer Name 2");
    data.put(103, "Customer Name 3");
    data.put(104, "Customer Name 4");
    data.put(105, "Customer Name 5");
    data.put(106, "Customer Name 6");
  }

  public List getUserNamesForIds(List<Integer> customerIds) {
    return customerIds.parallelStream().map(data::get).collect(Collectors.toList());
  }

  public void updateUsernameForId(Integer customerId, String newName) {
    data.put(customerId, newName);
  }
}
