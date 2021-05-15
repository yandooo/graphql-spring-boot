package graphql.kickstart.autoconfigure.editor.voyager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import graphql.kickstart.autoconfigure.editor.voyager.properties.VoyagerCdn;
import graphql.kickstart.autoconfigure.editor.voyager.properties.VoyagerDisplayOptions;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

@Data
@ConfigurationProperties(prefix = "graphql.voyager")
@Validated
public class VoyagerPropertiesConfiguration {

  private boolean enabled = false;

  private String endpoint = "/graphql";

  private String pageTitle = "Voyager";

  private String basePath = "/";

  @NestedConfigurationProperty @JsonIgnore private VoyagerCdn cdn = new VoyagerCdn();

  @NestedConfigurationProperty @JsonIgnore
  private VoyagerDisplayOptions displayOptions = new VoyagerDisplayOptions();

  @JsonIgnore private boolean hideDocs;

  @JsonIgnore private boolean hideSettings;
}
