package graphql.kickstart.spring.web.boot.test.instrumentation;

import graphql.kickstart.spring.web.boot.GraphQLInstrumentationAutoConfiguration;
import graphql.kickstart.spring.web.boot.metrics.MetricsInstrumentation;
import graphql.kickstart.spring.web.boot.metrics.TracingNoResolversInstrumentation;
import graphql.kickstart.spring.web.boot.test.AbstractAutoConfigurationTest;
import graphql.analysis.MaxQueryComplexityInstrumentation;
import graphql.analysis.MaxQueryDepthInstrumentation;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.tracing.TracingInstrumentation;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Marcel Overdijk
 */
public class GraphQLInstrumentationAutoConfigurationTest extends AbstractAutoConfigurationTest {

    public GraphQLInstrumentationAutoConfigurationTest() {
        super(AnnotationConfigWebApplicationContext.class, GraphQLInstrumentationAutoConfiguration.class);
    }

    @Configuration
    static class DefaultConfiguration {

        @Bean
        GraphQLSchema schema() {
            return GraphQLSchema.newSchema().query(GraphQLObjectType.newObject().name("Query").build()).build();
        }

        @Bean
        MeterRegistry meterRegistry() {
            return new SimpleMeterRegistry();
        }

    }

    @Test
    public void noDefaultInstrumentations() {
        load(DefaultConfiguration.class);

        assertThatExceptionOfType(NoSuchBeanDefinitionException.class)
            .isThrownBy(() -> this.getContext().getBean(Instrumentation.class));
    }

    @Test
    public void tracingInstrumentationEnabled() {
        load(DefaultConfiguration.class, "graphql.servlet.tracing-enabled=true");

        assertThat(this.getContext().getBean(TracingInstrumentation.class)).isNotNull();
    }

    @Test
    public void maxQueryComplexityEnabled() {
        load(DefaultConfiguration.class, "graphql.servlet.maxQueryComplexity=10");

        assertThat(this.getContext().getBean(MaxQueryComplexityInstrumentation.class)).isNotNull();
    }

    @Test
    public void maxQueryDepthEnabled() {
        load(DefaultConfiguration.class, "graphql.servlet.maxQueryDepth=10");

        assertThat(this.getContext().getBean(MaxQueryDepthInstrumentation.class)).isNotNull();
    }

    @Test
    public void actuatorMetricsEnabledAndTracingEnabled() {
        load(DefaultConfiguration.class, "graphql.servlet.tracing-enabled=true", "graphql.servlet.actuator-metrics=true");

        assertThat(this.getContext().getBean(MetricsInstrumentation.class)).isNotNull();
        assertThatExceptionOfType(NoSuchBeanDefinitionException.class)
            .isThrownBy(() -> this.getContext().getBean(TracingNoResolversInstrumentation.class));
    }

    @Test
    public void tracingInstrumentationDisabledAndMetricsEnabled() {
        load(DefaultConfiguration.class, "graphql.servlet.tracing-enabled=false", "graphql.servlet.actuator-metrics=true");

        assertThat(this.getContext().getBean(MetricsInstrumentation.class)).isNotNull();
        assertThat(this.getContext().getBean(TracingNoResolversInstrumentation.class)).isNotNull();
    }

    @Test
    public void tracingMetricsWithTracingDisabled() {
        load(DefaultConfiguration.class, "graphql.servlet.tracing-enabled='metrics-only'", "graphql.servlet.actuator-metrics=true");

        assertThat(this.getContext().getBean("metricsInstrumentation")).isNotNull();
        assertThat(this.getContext().getBean("tracingInstrumentation")).isNotNull();
    }

    @Test
    public void actuatorMetricsEnabled() {
        load(DefaultConfiguration.class, "graphql.servlet.actuator-metrics=true");

        assertThat(this.getContext().getBean(MetricsInstrumentation.class)).isNotNull();
        assertThat(this.getContext().getBean(TracingNoResolversInstrumentation.class)).isNotNull();
    }

    @Test
    public void tracingInstrumentationEnabledAndMetricsDisabled() {
        load(DefaultConfiguration.class, "graphql.servlet.tracing-enabled=true", "graphql.servlet.actuator-metrics=false");

        assertThat(this.getContext().getBean(TracingInstrumentation.class)).isNotNull();
        assertThatExceptionOfType(NoSuchBeanDefinitionException.class)
            .isThrownBy(() -> this.getContext().getBean(MetricsInstrumentation.class));
    }

    @Test
    public void tracingInstrumentationDisabledAndMetricsDisabled() {
        load(DefaultConfiguration.class, "graphql.servlet.tracing-enabled=false", "graphql.servlet.actuator-metrics=false");

        assertThatExceptionOfType(NoSuchBeanDefinitionException.class)
            .isThrownBy(() -> this.getContext().getBean(MetricsInstrumentation.class));
        assertThatExceptionOfType(NoSuchBeanDefinitionException.class)
            .isThrownBy(() ->this.getContext().getBean(TracingNoResolversInstrumentation.class));
        assertThatExceptionOfType(NoSuchBeanDefinitionException.class)
            .isThrownBy(() ->this.getContext().getBean(TracingInstrumentation.class));
    }

    @Test
    public void actuatorMetricsDisabled() {
        load(DefaultConfiguration.class, "graphql.servlet.actuator-metrics=false");

        assertThatExceptionOfType(NoSuchBeanDefinitionException.class)
            .isThrownBy(() ->this.getContext().getBean(MetricsInstrumentation.class));
    }
}
