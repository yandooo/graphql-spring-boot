package com.oembedler.moon.graphql.boot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "graphql.tools")
class GraphQLToolsProperties {

    private String schemaLocationPattern = "**/*.graphqls";
    /** @deprecated Set graphql.tools.schema-parser-options.introspection-enabled instead */
    @Deprecated
    private boolean introspectionEnabled = true;
    private boolean useDefaultObjectmapper = true;

}
