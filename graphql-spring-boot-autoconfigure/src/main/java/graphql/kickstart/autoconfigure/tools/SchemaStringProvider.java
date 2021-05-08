package graphql.kickstart.autoconfigure.tools;

import java.io.IOException;
import java.util.List;

public interface SchemaStringProvider {

  List<String> schemaStrings() throws IOException;
}
