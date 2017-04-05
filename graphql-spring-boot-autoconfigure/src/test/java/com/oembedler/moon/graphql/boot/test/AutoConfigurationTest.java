package com.oembedler.moon.graphql.boot.test;

import org.junit.After;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Andrew Potter
 */
public abstract class AutoConfigurationTest {

    protected AnnotationConfigApplicationContext context;
    private Class<?> autoConfiguration;

    protected AutoConfigurationTest(Class<?> autoConfiguration) {
        this.autoConfiguration = autoConfiguration;
    }

    @After
    public void tearDown() {
        if (this.context != null) {
            this.context.close();
        }
    }

    protected void load(Class<?> config, String... environment) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

        if (environment != null && environment.length > 0) {
            EnvironmentTestUtils.addEnvironment(applicationContext, environment);
        }

        applicationContext.register(config);
        applicationContext.register(autoConfiguration);
        applicationContext.refresh();

        this.context = applicationContext;
    }
}
