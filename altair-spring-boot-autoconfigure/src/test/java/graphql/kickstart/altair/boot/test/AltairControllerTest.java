package graphql.kickstart.altair.boot.test;

import graphql.kickstart.altair.boot.AltairAutoConfiguration;
import graphql.kickstart.altair.boot.AltairController;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * @author Andrew Potter
 */
public class AltairControllerTest extends AbstractAutoConfigurationTest {

    public AltairControllerTest() {
        super(AnnotationConfigWebApplicationContext.class, AltairAutoConfiguration.class);
    }

    @Configuration
    @PropertySource("classpath:enabled-config.properties")
    static class EnabledConfiguration {
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            return new PropertySourcesPlaceholderConfigurer();
        }
    }

    @Configuration
    @PropertySource("classpath:disabled-config.properties")
    static class DisabledConfiguration {
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            return new PropertySourcesPlaceholderConfigurer();
        }
    }

    @Test
    public void altairLoads() {
        load(EnabledConfiguration.class);

        Assert.assertNotNull(this.getContext().getBean(AltairController.class));
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void altairDoesNotLoad() {
        load(DisabledConfiguration.class);

        this.getContext().getBean(AltairController.class);
    }
}
