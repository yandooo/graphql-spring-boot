package graphql.kickstart.altair.boot;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

class PropertyGroupReader {

  private final Environment environment;
  private final String prefix;
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

  @SuppressWarnings("unchecked")
  private Stream<EnumerablePropertySource<Object>> streamOfPropertySources() {
    if (environment instanceof ConfigurableEnvironment) {
      Iterator<PropertySource<?>> iterator = ((ConfigurableEnvironment) environment)
          .getPropertySources().iterator();
      Iterable<PropertySource<?>> iterable = () -> iterator;
      return StreamSupport.stream(iterable.spliterator(), false)
          .filter(EnumerablePropertySource.class::isInstance)
          .map(it -> (EnumerablePropertySource<Object>) it);
    }
    return Stream.empty();
  }

  private String withoutPrefix(String key) {
    return key.replace(prefix, "");
  }

  private boolean isWanted(String key) {
    return key.startsWith(prefix);
  }

  private void add(EnumerablePropertySource<Object> propertySource, String key) {
    props.put(withoutPrefix(key), propertySource.getProperty(key));
  }

}

