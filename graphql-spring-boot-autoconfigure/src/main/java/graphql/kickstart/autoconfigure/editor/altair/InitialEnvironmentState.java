package graphql.kickstart.autoconfigure.editor.altair;

import java.util.Map;
import lombok.Data;

@Data
public class InitialEnvironmentState {

  private String id;
  private String title;
  private Map<String, String> variables;
}
