package graphql.kickstart.autoconfigure.editor.altair;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Data
@ConfigurationProperties("graphql.altair.options")
public class AltairOptions {

  @JsonProperty("endpointURL")
  private String endpointUrl = "/graphql";

  private String subscriptionsEndpoint = "/subscriptions";
  private String initialQuery;
  private String initialVariables;
  private String initialPreRequestScript;
  private String initialPostRequestScript;
  private Map<String, String> initialHeaders;
  @NestedConfigurationProperty InitialEnvironments initialEnvironments;
  private String instanceStorageNamespace;
  @NestedConfigurationProperty InitialSettings initialSettings;
  private String initialSubscriptionsProvider;
  private Map<String, String> initialSubscriptionsPayload;
  private Boolean preserveState = true;
  private String initialHttpMethod;
}
