package com.oembedler.moon.graphql.boot;

import graphql.kickstart.execution.context.ContextSetting;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static graphql.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"graphql.servlet.mapping=/test", "graphql.servlet.contextSetting=PER_REQUEST_WITH_INSTRUMENTATION"})
public class GraphQLServletPropertiesTest {

    @Autowired
    private GraphQLServletProperties properties;

    @Test
    public void contains_custom_servlet_endpoint() {
        String mapping = properties.getMapping();
        assertNotNull(mapping);
        assertEquals("/test", mapping);
    }

    @Test
    public void containsCorrectContextSetting() {
        ContextSetting contextSetting = properties.getContextSetting();
        assertEquals(ContextSetting.PER_REQUEST_WITH_INSTRUMENTATION, contextSetting);
    }
}
