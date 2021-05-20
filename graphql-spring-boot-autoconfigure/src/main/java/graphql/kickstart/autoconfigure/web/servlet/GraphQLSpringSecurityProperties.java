package graphql.kickstart.autoconfigure.web.servlet;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("graphql.spring.security")
public class GraphQLSpringSecurityProperties {

  private boolean delegateSecurityContext = true;
}
