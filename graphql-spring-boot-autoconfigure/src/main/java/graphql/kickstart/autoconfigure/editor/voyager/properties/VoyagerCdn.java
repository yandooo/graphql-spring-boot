package graphql.kickstart.autoconfigure.editor.voyager.properties;

import lombok.Data;

@Data
public class VoyagerCdn {

  private boolean enabled;
  private String version = "latest";
}
