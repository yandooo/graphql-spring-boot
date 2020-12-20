package graphql.kickstart.playground.boot.webflux;

import graphql.kickstart.playground.boot.PlaygroundTestHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class PlaygroundWebFluxResourcesTestBase {

    @Autowired
    private WebTestClient webTestClient;

    void testPlaygroundResources(
        final String cssUrl,
        final String scriptUrl,
        final String faviconUrl,
        final String logoUrl
    ) {
        // WHEN
        final byte[] actual = webTestClient.get().uri(PlaygroundTestHelper.DEFAULT_PLAYGROUND_ENDPOINT)
            .exchange()
            .expectStatus().isOk()
            .expectBody().returnResult().getResponseBody();

        // THEN
        assertThat(actual).isNotNull();
        final Document document = Jsoup.parse(new String(actual, StandardCharsets.UTF_8));
        PlaygroundTestHelper.assertStaticResources(document, cssUrl, scriptUrl, faviconUrl, logoUrl);
    }
}
