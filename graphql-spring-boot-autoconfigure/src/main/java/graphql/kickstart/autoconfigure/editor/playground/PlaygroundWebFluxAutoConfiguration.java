package graphql.kickstart.autoconfigure.editor.playground;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@Import(PlaygroundWebFluxControllerAdvice.class)
@ConditionalOnClass(WebFluxConfigurer.class)
@ConditionalOnProperty(value = "graphql.playground.enabled", havingValue = "true")
@RequiredArgsConstructor
public class PlaygroundWebFluxAutoConfiguration implements WebFluxConfigurer {

  @Bean
  public RouterFunction<ServerResponse> playgroundStaticFilesRouter() {
    return RouterFunctions.resources(
        "/vendor/playground/**", new ClassPathResource("static/vendor/playground/"));
  }
}
