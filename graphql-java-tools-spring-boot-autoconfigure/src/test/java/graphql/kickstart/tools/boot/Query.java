package graphql.kickstart.tools.boot;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

/**
 * @author Andrew Potter
 */
@Component
public class Query implements GraphQLQueryResolver {
    public String echo(String string) {
        return string;
    }
}
