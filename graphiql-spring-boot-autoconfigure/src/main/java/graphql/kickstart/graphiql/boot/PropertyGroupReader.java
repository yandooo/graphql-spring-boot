package graphql.kickstart.graphiql.boot;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class PropertyGroupReader {

    private Environment environment;
    private String prefix;
    private Properties props;

    PropertyGroupReader(Environment environment, String prefix) {
        this.environment = Objects.requireNonNull(environment);
        this.prefix = Optional.ofNullable(prefix).orElse("");
    }

    Properties load() {
        if (props == null) {
            props = new Properties();
            loadProps();
        }
        return props;
    }

    private void loadProps() {
        streamOfPropertySources().forEach(propertySource ->
                Arrays.stream(propertySource.getPropertyNames())
                        .filter(this::isWanted)
                        .forEach(key -> add(propertySource, key)));
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

    private String withoutPrefix(String key) {
        return key.replace(prefix, "");
    }

    private boolean isWanted(String key) {
        return key.startsWith(prefix);
    }

    private Object add(EnumerablePropertySource propertySource, String key) {
        return props.put(withoutPrefix(key), propertySource.getProperty(key));
    }

}

