package com.oembedler.moon.graphql.boot;

import graphql.schema.GraphQLSchema;
import graphql.servlet.config.GraphQLSchemaProvider;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

class OnSchemaOrSchemaProviderBean extends AnyNestedCondition {
    public OnSchemaOrSchemaProviderBean() {
        super(ConfigurationPhase.REGISTER_BEAN);
    }

    @ConditionalOnBean(GraphQLSchema.class)
    static class OnSchema {
    }

    @ConditionalOnBean(GraphQLSchemaProvider.class)
    static class OnSchemaProvider {
    }
}