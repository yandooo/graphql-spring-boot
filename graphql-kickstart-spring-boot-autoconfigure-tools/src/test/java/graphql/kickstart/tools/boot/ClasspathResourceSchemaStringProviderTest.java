package graphql.kickstart.tools.boot;

import static org.assertj.core.api.Assertions.assertThat;

import graphql.kickstart.tools.GraphQLQueryResolver;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;

public class ClasspathResourceSchemaStringProviderTest extends AbstractAutoConfigurationTest {

  private ClasspathResourceSchemaStringProvider schemaStringProvider;

  public ClasspathResourceSchemaStringProviderTest() {
    super(GraphQLJavaToolsAutoConfiguration.class);
  }

  @BeforeEach
  public void setup() {
    System.setProperty("graphql.tools.schemaLocationPattern", "graphql/*.gqls");
  }

  @AfterEach
  public void clear() {
    System.clearProperty("graphql.tools.schemaLocationPattern");
  }

  @Test
  public void schemaStrings() throws IOException {
    load(BaseConfiguration.class);
    schemaStringProvider = getContext().getBean(ClasspathResourceSchemaStringProvider.class);

    List<String> schemaStrings = schemaStringProvider.schemaStrings();
    assertThat(schemaStrings).hasSize(1);
    assertThat(schemaStrings.get(0)).contains("schemaLocationTest");
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
