package graphql.kickstart.tools.boot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import java.io.IOException;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Configuration;

public class ClasspathResourceSchemaStringProviderTest extends AbstractAutoConfigurationTest {

  private ClasspathResourceSchemaStringProvider schemaStringProvider;

  public ClasspathResourceSchemaStringProviderTest() {
    super(GraphQLJavaToolsAutoConfiguration.class);
  }

  @Before
  public void setup() {
    System.setProperty("graphql.tools.schemaLocationPattern", "graphql/*.gqls");
  }

  @After
  public void clear() {
    System.clearProperty("graphql.tools.schemaLocationPattern");
  }

  @Test
  public void schemaStrings() throws IOException {
    load(BaseConfiguration.class);
    schemaStringProvider = getContext().getBean(ClasspathResourceSchemaStringProvider.class);

    List<String> schemaStrings = schemaStringProvider.schemaStrings();
    assertEquals(1, schemaStrings.size());
    assertTrue(schemaStrings.get(0).contains("schemaLocationTest"));
  }

  @Configuration
  static class BaseConfiguration {

    public class Query implements GraphQLQueryResolver {

      String schemaLocationTest(String id) {
        return id;
      }
    }

  }
}
