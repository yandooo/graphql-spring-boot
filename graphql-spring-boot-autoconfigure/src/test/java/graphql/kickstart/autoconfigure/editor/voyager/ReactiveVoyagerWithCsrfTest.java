package graphql.kickstart.autoconfigure.editor.voyager;

import static org.assertj.core.api.Assertions.assertThat;

import graphql.kickstart.autoconfigure.PermitAllWebFluxSecurity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@Import(PermitAllWebFluxSecurity.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = {ReactiveVoyagerAutoConfiguration.class, ReactiveSecurityAutoConfiguration.class},
    properties = {"graphql.voyager.enabled=true", "spring.main.web-application-type=reactive"})
@AutoConfigureWebTestClient
@ActiveProfiles("voyager")
class ReactiveVoyagerWithCsrfTest {

  @Autowired private WebTestClient webTestClient;

  @Test
  void shouldLoadCSRFData() {
    final String responseBody =
        webTestClient
            .get()
            .uri("/voyager")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(String.class)
            .returnResult()
            .getResponseBody();

    final Document document = Jsoup.parse(responseBody);
    final String script = document.body().select("body script").dataNodes().get(0).getWholeData();
    assertThat(script)
        .contains("let csrf = {\"")
        .contains("\"token\":\"")
        .contains("\"parameterName\":\"_csrf\"")
        .contains("\"headerName\":\"X-CSRF-TOKEN\"");
  }
}
