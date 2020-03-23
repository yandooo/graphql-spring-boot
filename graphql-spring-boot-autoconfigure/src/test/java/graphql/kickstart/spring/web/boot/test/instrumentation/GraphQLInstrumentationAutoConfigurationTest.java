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
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

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

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void noDefaultInstrumentations() {
        load(DefaultConfiguration.class);

        this.getContext().getBean(Instrumentation.class);
    }

    @Test
    public void tracingInstrumentationEnabled() {
        load(DefaultConfiguration.class, "graphql.servlet.tracing-enabled=true");

        Assert.assertNotNull(this.getContext().getBean(TracingInstrumentation.class));
    }

    @Test
    public void maxQueryComplexityEnabled() {
        load(DefaultConfiguration.class, "graphql.servlet.maxQueryComplexity=10");

        Assert.assertNotNull(this.getContext().getBean(MaxQueryComplexityInstrumentation.class));
    }

    @Test
    public void maxQueryDepthEnabled() {
        load(DefaultConfiguration.class, "graphql.servlet.maxQueryDepth=10");

        Assert.assertNotNull(this.getContext().getBean(MaxQueryDepthInstrumentation.class));
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void actuatorMetricsEnabledAndTracingEnabled() {
        load(DefaultConfiguration.class, "graphql.servlet.tracing-enabled=true", "graphql.servlet.actuator-metrics=true");

        Assert.assertNotNull(this.getContext().getBean(TracingInstrumentation.class));
        Assert.assertNotNull(this.getContext().getBean(MetricsInstrumentation.class));
        this.getContext().getBean(TracingNoResolversInstrumentation.class);
    }

    @Test
    public void tracingInstrumentationDisabledAndMetricsEnabled() {
        load(DefaultConfiguration.class, "graphql.servlet.tracing-enabled=false", "graphql.servlet.actuator-metrics=true");

        Assert.assertNotNull(this.getContext().getBean(MetricsInstrumentation.class));
        Assert.assertNotNull(this.getContext().getBean(TracingNoResolversInstrumentation.class));
    }

    @Test
    public void tracingMetricsWithTracingDisabled() {
        load(DefaultConfiguration.class, "graphql.servlet.tracing-enabled='metrics-only'", "graphql.servlet.actuator-metrics=true");

        Assert.assertNotNull(this.getContext().getBean("metricsInstrumentation"));
        Assert.assertNotNull(this.getContext().getBean("tracingInstrumentation"));
    }

    @Test
    public void actuatorMetricsEnabled() {
        load(DefaultConfiguration.class, "graphql.servlet.actuator-metrics=true");

        Assert.assertNotNull(this.getContext().getBean(MetricsInstrumentation.class));
        Assert.assertNotNull(this.getContext().getBean(TracingNoResolversInstrumentation.class));
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void tracingInstrumentationEnabledAndMetricsDisabled() {
        load(DefaultConfiguration.class, "graphql.servlet.tracing-enabled=true", "graphql.servlet.actuator-metrics=false");

        Assert.assertNotNull(this.getContext().getBean(TracingInstrumentation.class));
        this.getContext().getBean(MetricsInstrumentation.class);
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void tracingInstrumentationDisabledAndMetricsDisabled() {
        load(DefaultConfiguration.class, "graphql.servlet.tracing-enabled=false", "graphql.servlet.actuator-metrics=false");

        this.getContext().getBean(MetricsInstrumentation.class);
        this.getContext().getBean(TracingNoResolversInstrumentation.class);
        this.getContext().getBean(TracingInstrumentation.class);
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void actuatorMetricsDisabled() {
        load(DefaultConfiguration.class, "graphql.servlet.actuator-metrics=false");

        this.getContext().getBean(MetricsInstrumentation.class);
    }
}
