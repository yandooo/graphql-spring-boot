package graphql.kickstart.autoconfigure.web.servlet;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "graphql.servlet.async")
public class AsyncServletProperties {

  private boolean enabled = true;
  private long timeout = 30000;
  private boolean delegateSecurityContext = true;
  private Threads threads = new Threads();

  @Data
  static class Threads {
    private int min = 10;
    private int max = 200;
    private String namePrefix = "graphql-exec-";
  }
}
