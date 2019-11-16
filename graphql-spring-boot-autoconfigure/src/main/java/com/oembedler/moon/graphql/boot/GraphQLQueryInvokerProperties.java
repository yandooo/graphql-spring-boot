package com.oembedler.moon.graphql.boot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "graphql.query-invoker")
public class GraphQLQueryInvokerProperties {

    private boolean transactional;
}
