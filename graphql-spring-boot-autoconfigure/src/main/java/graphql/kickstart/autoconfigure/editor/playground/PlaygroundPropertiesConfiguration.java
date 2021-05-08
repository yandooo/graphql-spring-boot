package graphql.kickstart.autoconfigure.editor.playground;

import graphql.kickstart.autoconfigure.editor.playground.properties.PlaygroundProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

@Data

@Validated
public class PlaygroundPropertiesConfiguration {

  @NestedConfigurationProperty private PlaygroundProperties playground = new PlaygroundProperties();
}
