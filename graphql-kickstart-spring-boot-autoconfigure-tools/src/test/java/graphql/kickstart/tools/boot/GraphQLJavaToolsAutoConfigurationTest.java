package graphql.kickstart.tools.boot;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.coxautodev.graphql.tools.SchemaParserDictionary;
import graphql.schema.GraphQLSchema;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author <a href="mailto:java.lang.RuntimeException@gmail.com">oEmbedler Inc.</a>
 */
public class GraphQLJavaToolsAutoConfigurationTest extends AbstractAutoConfigurationTest {

  public GraphQLJavaToolsAutoConfigurationTest() {
    super(GraphQLJavaToolsAutoConfiguration.class);
  }

  @Test
  public void appContextLoads() {
    load(BaseConfiguration.class);

    Assert.assertNotNull(this.getContext().getBean(GraphQLSchema.class));
  }

  @Test
  public void schemaWithInterfaceLoads() {
    load(InterfaceConfiguration.class);

    Assert.assertNotNull(this.getContext().getBean(GraphQLSchema.class));
  }

  @Configuration
  static class BaseConfiguration {
    @Bean
    public Query query() {
      return new Query();
    }
  }

  @Configuration
  static class InterfaceConfiguration {

    @Bean
    public SchemaStringProvider schemaStringProvider() {
      ListSchemaStringProvider schemaStringProvider = new ListSchemaStringProvider();
      schemaStringProvider.add("type Query {"
          + "  theInterface:Interface!"
          + "} "
          + "interface Interface {"
          + "  method:String!"
          + "}"
          + "type Implementation implements Interface {"
          + "  method:String!"
          + "}");
      return schemaStringProvider;
    }

    @Bean
    public Query query() {
      return new Query();
    }

    @Bean
    public SchemaParserDictionary schemaParserDictionary() {
      return new SchemaParserDictionary().add(Implementation.class);
    }

    interface Interface {

      String method();
    }

    class Query implements GraphQLQueryResolver {

      Interface theInterface() {
        return new Implementation();
      }

      Implementation theImplementation() {
        return new Implementation();
      }
    }

    class Implementation implements Interface {

      public String method() {
        return "method";
      }
    }
  }


}
