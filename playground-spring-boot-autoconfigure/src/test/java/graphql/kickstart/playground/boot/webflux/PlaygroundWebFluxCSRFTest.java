package graphql.kickstart.playground.boot.webflux;

import graphql.kickstart.playground.boot.PlaygroundTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = PlaygroundWebFluxTestConfig.class)
@AutoConfigureWebTestClient
public class PlaygroundWebFluxCSRFTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldLoadCSRFData() {
        // WHEN
        final byte[] actual = webTestClient.get().uri(PlaygroundTestHelper.DEFAULT_PLAYGROUND_ENDPOINT)
            .exchange()
            .expectStatus().isOk()
            .expectBody().returnResult().getResponseBody();
        // THEN
        assertThat(actual).isNotNull();
        assertThat(new String(actual, StandardCharsets.UTF_8))
            .contains("let csrf = {\"token\":")
            .contains("\"parameterName\":\"_csrf\",\"headerName\":\"X-CSRF-TOKEN\"}");
    }
}
