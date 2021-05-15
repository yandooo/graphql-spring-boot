package graphql.kickstart.autoconfigure.editor.altair;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

@Data
@ConfigurationProperties("graphql.altair.resources")
public class AltairResources {

  private String initialQuery;
  private String initialVariables;
  private String initialPreRequestScript;
  private String initialPostRequestScript;

  public void load(AltairOptions options) {
    loadResource(initialQuery).ifPresent(options::setInitialQuery);
    loadResource(initialVariables).ifPresent(options::setInitialVariables);
    loadResource(initialPreRequestScript).ifPresent(options::setInitialPreRequestScript);
    loadResource(initialPostRequestScript).ifPresent(options::setInitialPostRequestScript);
  }

  private Optional<String> loadResource(String property) {
    return Optional.ofNullable(property).map(ClassPathResource::new).map(this::loadResource);
  }

  @SneakyThrows
  private String loadResource(Resource resource) {
    try (InputStream inputStream = resource.getInputStream()) {
      return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
    }
  }
}
