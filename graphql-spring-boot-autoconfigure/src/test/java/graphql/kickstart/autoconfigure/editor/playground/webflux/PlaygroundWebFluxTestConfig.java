package graphql.kickstart.autoconfigure.editor.playground.webflux;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
// import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
// import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
// @EnableWebFluxSecurity
@SpringBootApplication(exclude = {WebMvcAutoConfiguration.class, SecurityAutoConfiguration.class})
class PlaygroundWebFluxTestConfig {

  //  @Bean
  //  public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) {
  //    return http.authorizeExchange().anyExchange().permitAll().and().build();
  //  }
}
