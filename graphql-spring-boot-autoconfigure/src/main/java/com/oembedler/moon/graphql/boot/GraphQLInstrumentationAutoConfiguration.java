package com.oembedler.moon.graphql.boot;

import com.oembedler.moon.graphql.boot.metrics.MetricsInstrumentation;
import com.oembedler.moon.graphql.boot.metrics.TracingNoResolversInstrumentation;
import graphql.analysis.MaxQueryComplexityInstrumentation;
import graphql.analysis.MaxQueryDepthInstrumentation;
import graphql.execution.instrumentation.tracing.TracingInstrumentation;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.simple.SimpleMetricsExportAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Marcel Overdijk
 */
@Configuration
@AutoConfigureAfter({MetricsAutoConfiguration.class, SimpleMetricsExportAutoConfiguration.class})
@ConditionalOnProperty(value = "graphql.servlet.enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({GraphQLServletProperties.class})
public class GraphQLInstrumentationAutoConfiguration {

    @Value("${graphql.servlet.maxQueryComplexity:#{null}}")
    private Integer maxQueryComplexity;

    @Value("${graphql.servlet.maxQueryDepth:#{null}}")
    private Integer maxQueryDepth;

    @Value("${graphql.servlet.tracing-enabled:#{false}}")
    private Boolean tracingEnabled;

    @Bean
    @ConditionalOnMissingBean({TracingInstrumentation.class, MetricsInstrumentation.class})
    @ConditionalOnProperty(value = "graphql.servlet.tracing-enabled", havingValue = "true")
    public TracingInstrumentation tracingInstrumentation() {
        return new TracingInstrumentation();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "graphql.servlet.tracing-enabled", havingValue = "false")
    public TracingNoResolversInstrumentation tracingNoResolversInstrumentation() {
        return new TracingNoResolversInstrumentation();
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

    @Bean
    @ConditionalOnProperty(value = "graphql.servlet.actuator-metrics", havingValue = "true")
    @ConditionalOnBean({MeterRegistry.class, TracingInstrumentation.class})
    @ConditionalOnMissingBean
    public MetricsInstrumentation metricsInstrumentation(MeterRegistry meterRegistry) {
        return new MetricsInstrumentation(meterRegistry, tracingEnabled);
    }

}
