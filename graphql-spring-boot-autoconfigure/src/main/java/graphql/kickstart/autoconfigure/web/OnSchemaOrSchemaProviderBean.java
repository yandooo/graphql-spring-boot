package graphql.kickstart.autoconfigure.web;

import graphql.kickstart.execution.config.GraphQLSchemaProvider;
import graphql.schema.GraphQLSchema;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

public class OnSchemaOrSchemaProviderBean extends AnyNestedCondition {

  public OnSchemaOrSchemaProviderBean() {
    super(ConfigurationPhase.REGISTER_BEAN);
  }

  @ConditionalOnBean(GraphQLSchema.class)
  static class OnSchema {}

  @ConditionalOnBean(GraphQLSchemaProvider.class)
  static class OnSchemaProvider {}
}
