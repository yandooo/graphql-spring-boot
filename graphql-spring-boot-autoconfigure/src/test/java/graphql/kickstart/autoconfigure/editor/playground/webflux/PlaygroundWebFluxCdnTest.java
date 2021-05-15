package graphql.kickstart.autoconfigure.editor.playground.webflux;

import graphql.kickstart.autoconfigure.editor.playground.PlaygroundTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = PlaygroundWebFluxTestConfig.class)
@AutoConfigureWebTestClient
@ActiveProfiles("playground")
@TestPropertySource("classpath:application-playground-cdn-test.properties")
class PlaygroundWebFluxCdnTest extends PlaygroundWebFluxResourcesTestBase {

  @Test
  void shouldLoadLatestVersionFromCdn() {
    testPlaygroundResources(
        PlaygroundTestHelper.DEFAULT_CSS_CDN_PATH,
        PlaygroundTestHelper.DEFAULT_SCRIPT_CDN_PATH,
        PlaygroundTestHelper.DEFAULT_FAVICON_CDN_PATH,
        PlaygroundTestHelper.DEFAULT_LOGO_CDN_PATH);
  }
}
