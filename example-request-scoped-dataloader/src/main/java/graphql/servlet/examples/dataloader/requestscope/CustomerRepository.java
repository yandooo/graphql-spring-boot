package graphql.servlet.examples.dataloader.requestscope;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CustomerRepository {

    private final Map<Integer, String> data;

    private CustomerRepository() {
        data = new HashMap<Integer, String>();
        data.put(Integer.valueOf(101), "Customer Name 1");
        data.put(Integer.valueOf(102), "Customer Name 2");
        data.put(Integer.valueOf(103), "Customer Name 3");
        data.put(Integer.valueOf(104), "Customer Name 4");
        data.put(Integer.valueOf(105), "Customer Name 5");
        data.put(Integer.valueOf(106), "Customer Name 6");
    }

    public List getUserNamesForIds(List<Integer> customerIds) {
        return customerIds.parallelStream().map(data::get).collect(Collectors.toList());
    }

    public void updateUsernameForId(Integer customerId, String newName) {
        data.put(customerId, newName);
    }
}
