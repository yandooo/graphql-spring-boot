package graphql.kickstart.autoconfigure.editor.altair;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Data
public class InitialEnvironments {

  @NestedConfigurationProperty InitialEnvironmentState base;
  @NestedConfigurationProperty List<InitialEnvironmentState> subEnvironments;
}
