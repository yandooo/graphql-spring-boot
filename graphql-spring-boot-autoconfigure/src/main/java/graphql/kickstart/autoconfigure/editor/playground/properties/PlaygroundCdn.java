package graphql.kickstart.autoconfigure.editor.playground.properties;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PlaygroundCdn {

  private boolean enabled;
  @NotBlank private String version = "latest";
}
