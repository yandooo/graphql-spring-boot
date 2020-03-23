package graphql.kickstart.spring.web.boot;

import graphql.kickstart.spring.web.boot.metrics.MetricsInstrumentation;
import graphql.kickstart.spring.web.boot.metrics.TracingNoResolversInstrumentation;
import graphql.kickstart.spring.web.boot.metrics.WebsocketMetrics;
import graphql.analysis.MaxQueryComplexityInstrumentation;
import graphql.analysis.MaxQueryDepthInstrumentation;
import graphql.execution.instrumentation.tracing.TracingInstrumentation;
import graphql.servlet.GraphQLWebsocketServlet;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.simple.SimpleMetricsExportAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Marcel Overdijk
 */
@Configuration
@ConditionalOnClass(MetricsAutoConfiguration.class)
@AutoConfigureAfter({MetricsAutoConfiguration.class, SimpleMetricsExportAutoConfiguration.class,
    GraphQLWebsocketAutoConfiguration.class})
@ConditionalOnProperty(value = "graphql.servlet.enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({GraphQLServletProperties.class})
public class GraphQLInstrumentationAutoConfiguration {

  @Value("${graphql.servlet.maxQueryComplexity:#{null}}")
  private Integer maxQueryComplexity;

  @Value("${graphql.servlet.maxQueryDepth:#{null}}")
  private Integer maxQueryDepth;

  @Value("${graphql.servlet.tracing-enabled:'false'}")
  private String tracingEnabled;

  @Bean
  @ConditionalOnMissingBean({TracingInstrumentation.class, MetricsInstrumentation.class})
  @ConditionalOnExpression("${graphql.servlet.tracing-enabled:'false'}.equals('metrics-only')"
      + "|| ${graphql.servlet.tracing-enabled:'false'}.equals(true)")
  public TracingInstrumentation tracingInstrumentation() {
    return new TracingInstrumentation();
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnExpression("${graphql.servlet.actuator-metrics:false}"
      + "&& ${graphql.servlet.tracing-enabled:'false'}.toString().equals('false')")
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
    return new MetricsInstrumentation(meterRegistry, Boolean.TRUE.toString().equals(tracingEnabled));
  }

  @Bean
  @ConditionalOnProperty(value = "graphql.servlet.actuator-metrics", havingValue = "true")
  @ConditionalOnBean({MeterRegistry.class, GraphQLWebsocketServlet.class})
  @ConditionalOnMissingBean
  public WebsocketMetrics websocketMetrics(MeterRegistry meterRegistry, GraphQLWebsocketServlet websocketServlet) {
    return new WebsocketMetrics(meterRegistry, websocketServlet);
  }

}
