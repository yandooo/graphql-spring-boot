package graphql.kickstart.autoconfigure.editor.playground;

import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.kickstart.autoconfigure.editor.playground.properties.PlaygroundProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(PlaygroundProperties.class)
public class PlaygroundAutoConfiguration {

  @Bean
  @ConditionalOnProperty(
      value = "graphql.playground.enabled",
      havingValue = "true")
  public PlaygroundController playgroundController(
      final PlaygroundProperties playgroundProperties,
      final ObjectMapper objectMapper) {
    return new PlaygroundController(playgroundProperties, objectMapper);
  }
}
