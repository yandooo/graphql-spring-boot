package com.oembedler.moon.graphiql.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class PropsLoader {

    private static final String GRAPHIQL_PROPS_PREFIX = "graphiql.props.";
    private static final String GRAPHIQL_PROPS_RESOURCES_PREFIX = GRAPHIQL_PROPS_PREFIX + "resources.";
    private static final String GRAPHIQL_PROPS_VALUES_PREFIX = GRAPHIQL_PROPS_PREFIX + "values.";

    private Environment environment;

    PropsLoader(Environment environment) {
        this.environment = environment;
    }

    String load() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode props = objectMapper.createObjectNode();

        streamOfPropertySources().forEach(propertySource ->
                Arrays.stream(propertySource.getPropertyNames())
                        .filter(this::isPropValue)
                        .forEach(key -> props.put(toPropKey(key), (String) propertySource.getProperty(key))));

        loadPropFromResource("defaultQuery").ifPresent(it -> props.put("defaultQuery", it));
        loadPropFromResource("query").ifPresent(it -> props.put("query", it));
        loadPropFromResource("variables").ifPresent(it -> props.put("variables", it));
        return objectMapper.writeValueAsString(props);
    }

    private Stream<EnumerablePropertySource> streamOfPropertySources() {
        if (environment instanceof ConfigurableEnvironment) {
            Iterator<PropertySource<?>> iterator = ((ConfigurableEnvironment) environment).getPropertySources().iterator();
            Iterable<PropertySource<?>> iterable = () -> iterator;
            return StreamSupport.stream(iterable.spliterator(), false)
                    .filter(EnumerablePropertySource.class::isInstance)
                    .map(EnumerablePropertySource.class::cast);
        }
        return Stream.empty();
    }

    private String toPropKey(String key) {
        return key.replace(GRAPHIQL_PROPS_VALUES_PREFIX, "");
    }

    private boolean isPropValue(String key) {
        return key.startsWith(GRAPHIQL_PROPS_VALUES_PREFIX);
    }

    private Optional<String> loadPropFromResource(String prop) throws IOException {
        String property = GRAPHIQL_PROPS_RESOURCES_PREFIX + prop;
        if (environment.containsProperty(property)) {
            String location = environment.getProperty(property);
            Resource resource = new ClassPathResource(location);
            return Optional.of(loadResource(resource));
        }
        return Optional.empty();
    }

    private String loadResource(Resource resource) throws IOException {
        try (InputStream inputStream = resource.getInputStream()) {
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        }
    }

}
