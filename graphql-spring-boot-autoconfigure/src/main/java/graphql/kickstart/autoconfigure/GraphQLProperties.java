package graphql.kickstart.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("graphql")
public class GraphQLProperties {

  private SchemaStrategy schemaStrategy = SchemaStrategy.TOOLS;
}
