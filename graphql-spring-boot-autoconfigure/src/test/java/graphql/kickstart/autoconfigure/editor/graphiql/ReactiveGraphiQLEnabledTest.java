package graphql.kickstart.autoconfigure.editor.graphiql;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@Slf4j
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = {GraphiQLAutoConfiguration.class, WebFluxAutoConfiguration.class},
    properties = {"spring.main.web-application-type=reactive"})
@TestPropertySource("classpath:enabled-config.properties")
class ReactiveGraphiQLEnabledTest {

  @Autowired private ApplicationContext applicationContext;
  @Autowired private WebTestClient webTestClient;

  @Test
  void graphiqlLoads() {
    assertThat(applicationContext.getBean(ReactiveGraphiQLController.class)).isNotNull();
  }

  @Test
  void graphiqlShouldBeAvailableAtDefaultEndpoint() throws Exception {
    final String responseBody =
        webTestClient
            .get()
            .uri("/graphiql")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(String.class)
            .returnResult()
            .getResponseBody();
    assertThat(responseBody).isNotNull();
    final Document document = Jsoup.parse(responseBody);
    log.info("{}", responseBody);
    assertThat(document.select("head title").text()).isEqualTo("GraphiQL");
  }
}
