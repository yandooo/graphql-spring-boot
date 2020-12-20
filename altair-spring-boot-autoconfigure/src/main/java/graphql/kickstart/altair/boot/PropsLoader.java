package graphql.kickstart.altair.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Properties;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

class PropsLoader {

  private static final String ALTAIR_PROPS_PREFIX = "altair.props.";
  private static final String ALTAIR_PROPS_RESOURCES_PREFIX = ALTAIR_PROPS_PREFIX + "resources.";
  private static final String ALTAIR_PROPS_VALUES_PREFIX = ALTAIR_PROPS_PREFIX + "values.";

  private Environment environment;

  PropsLoader(Environment environment) {
    this.environment = environment;
  }

  String load() throws IOException {
    PropertyGroupReader reader = new PropertyGroupReader(environment, ALTAIR_PROPS_VALUES_PREFIX);
    Properties props = reader.load();

    ObjectMapper objectMapper = new ObjectMapper();
    loadPropFromResource("defaultQuery").ifPresent(it -> props.put("defaultQuery", it));
    loadPropFromResource("query").ifPresent(it -> props.put("query", it));
    loadPropFromResource("variables").ifPresent(it -> props.put("variables", it));
    return objectMapper.writeValueAsString(props);
  }

  private Optional<String> loadPropFromResource(String prop) throws IOException {
    String property = ALTAIR_PROPS_RESOURCES_PREFIX + prop;
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
