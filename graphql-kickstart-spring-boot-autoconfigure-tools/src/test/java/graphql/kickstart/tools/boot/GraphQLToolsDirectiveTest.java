package graphql.kickstart.tools.boot;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.GraphQLObjectType;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

public class GraphQLToolsDirectiveTest extends AbstractAutoConfigurationTest {

  public GraphQLToolsDirectiveTest() {
    super(GraphQLJavaToolsAutoConfiguration.class);
  }

  @AfterEach
  public void clear() {
    System.clearProperty("graphql.tools.schemaLocationPattern");
  }

  @Test
  public void directiveIsLoaded() {
    System.setProperty("graphql.tools.schemaLocationPattern",
        "graphql/schema-directive-test.graphql");
    load(BaseConfiguration.class);
  }

  @Configuration
  static class BaseConfiguration {

    @Bean
    public SchemaDirective uppercaseDirective() {
      return new SchemaDirective("uppercase", new SchemaDirectiveWiring() {
        @Override
        public GraphQLObjectType onObject(
            SchemaDirectiveWiringEnvironment<GraphQLObjectType> environment) {
          return null;
        }
      });
    }

    @Component
    public class Query implements GraphQLQueryResolver {

      String schemaLocationTest(String id) {
        return id;
      }
    }
  }
}
