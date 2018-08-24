package com.oembedler.moon.graphql.boot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static graphql.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"graphql.servlet.mapping=/test"})
public class GraphQLServletPropertiesTest {

    @Autowired
    private GraphQLServletProperties properties;

    @Test
    public void contains_custom_servlet_endpoint() {
        String mapping = properties.getMapping();
        assertNotNull(mapping);
        assertEquals("/test", mapping);
    }

}
