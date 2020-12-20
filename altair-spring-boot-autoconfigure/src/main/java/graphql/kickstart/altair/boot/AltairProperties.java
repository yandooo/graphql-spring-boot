package graphql.kickstart.altair.boot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("altair")
public class AltairProperties {

  private boolean enabled = true;
  private Endpoint endpoint = new Endpoint();
  private Static STATIC = new Static();
  private Cdn cdn = new Cdn();
  private String pageTitle = "Altair";
  private String mapping = "/altair";

  @Data
  static class Endpoint {

    private String graphql = "/graphql";
    private String subscriptions = "/subscriptions";
  }

  @Data
  static class Static {

    private String basePath = "";
  }

  @Data
  static class Cdn {

    private boolean enabled = false;
    private String version = "2.4.11";
  }

}
