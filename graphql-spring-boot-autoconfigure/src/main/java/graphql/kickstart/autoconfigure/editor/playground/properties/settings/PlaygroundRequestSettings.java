package graphql.kickstart.autoconfigure.editor.playground.properties.settings;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaygroundRequestSettings {

  private PlaygroundRequestIncludeCredentials credentials;
}
