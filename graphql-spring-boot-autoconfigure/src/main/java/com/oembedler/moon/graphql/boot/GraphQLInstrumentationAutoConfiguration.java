package com.oembedler.moon.graphql.boot;

import graphql.analysis.MaxQueryComplexityInstrumentation;
import graphql.analysis.MaxQueryDepthInstrumentation;
import graphql.execution.instrumentation.tracing.TracingInstrumentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Marcel Overdijk
 */
@Configuration
@ConditionalOnProperty(value = "graphql.servlet.enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({GraphQLServletProperties.class})
public class GraphQLInstrumentationAutoConfiguration {

    @Value("${graphql.servlet.maxQueryComplexity:#{null}}")
    private Integer maxQueryComplexity;

    @Value("${graphql.servlet.maxQueryDepth:#{null}}")
    private Integer maxQueryDepth;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "graphql.servlet.tracing-enabled", havingValue = "true")
    public TracingInstrumentation tracingInstrumentation() {
        return new TracingInstrumentation();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "graphql.servlet.max-query-complexity")
    public MaxQueryComplexityInstrumentation maxQueryComplexityInstrumentation() {
        return new MaxQueryComplexityInstrumentation(maxQueryComplexity);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "graphql.servlet.max-query-depth")
    public MaxQueryDepthInstrumentation maxQueryDepthInstrumentation() {
        return new MaxQueryDepthInstrumentation(maxQueryDepth);
    }
}
