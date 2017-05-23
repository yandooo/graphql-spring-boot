package org.springframework.web.servlet.config.annotation;

import org.springframework.web.cors.CorsConfiguration;

import java.util.Map;

/**
 * Why is all of the CORS stuff protected?
 */
public class CorsRegistryWorkaround {
    public static Map<String, CorsConfiguration> getCorsConfiguration(String antPathMatcher) {
        CorsRegistry registry = new CorsRegistry();
        registry.addMapping(antPathMatcher);
        return registry.getCorsConfigurations();
    }
}
