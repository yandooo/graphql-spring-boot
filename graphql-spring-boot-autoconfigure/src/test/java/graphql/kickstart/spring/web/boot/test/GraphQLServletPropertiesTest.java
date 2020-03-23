package graphql.kickstart.spring.web.boot.test;

import graphql.kickstart.spring.web.boot.GraphQLServletProperties;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Andrew Potter
 */
public class GraphQLServletPropertiesTest {

    @Test
    public void getServletMappingReturnsAppropriateWildcardValue() {
        verifyServletMapping("/graphql", "/graphql/*");
        verifyServletMapping("/graphql/", "/graphql/*");
        verifyServletMapping("/graphql/*", "/graphql/*");
        verifyServletMapping("/graphql/**", "/graphql/*");
    }

    @Test
    public void getCorsMappingReturnsAppropriateWildcardValue() {
        verifyCorsMapping("/graphql", "/graphql/**");
        verifyCorsMapping("/graphql/", "/graphql/**");
        verifyCorsMapping("/graphql/*", "/graphql/**");
        verifyCorsMapping("/graphql/**", "/graphql/**");
    }

    private void verifyCorsMapping(String mapping, String expected) {
        GraphQLServletProperties servletProperties = new GraphQLServletProperties();
        servletProperties.setMapping(mapping);

        assertEquals(String.format("Expected mapping '%s' to return cors mapping '%s'", mapping, expected), servletProperties.getCorsMapping(), expected);
    }

    private void verifyServletMapping(String mapping, String expected) {
        GraphQLServletProperties servletProperties = new GraphQLServletProperties();
        servletProperties.setMapping(mapping);

        assertEquals(String.format("Expected mapping '%s' to return servlet mapping '%s'", mapping, expected), servletProperties.getServletMapping(), expected);
    }
}
