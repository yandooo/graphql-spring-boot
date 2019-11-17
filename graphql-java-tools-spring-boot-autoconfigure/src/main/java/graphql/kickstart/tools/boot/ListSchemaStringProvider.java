package graphql.kickstart.tools.boot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListSchemaStringProvider implements SchemaStringProvider {

  private final List<String> schemaStrings;

  public ListSchemaStringProvider() {
    schemaStrings = new ArrayList<>();
  }

  public void add(String schemaString) {
    schemaStrings.add(schemaString);
  }

  @Override
  public List<String> schemaStrings() throws IOException {
    return schemaStrings;
  }
}
