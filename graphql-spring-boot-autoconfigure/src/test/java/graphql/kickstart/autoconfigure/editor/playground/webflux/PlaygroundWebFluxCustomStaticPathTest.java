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
@TestPropertySource("classpath:application-playground-custom-static-path.properties")
class PlaygroundWebFluxCustomStaticPathTest extends PlaygroundWebFluxResourcesTestBase {

  @Test
  void shouldLoadStaticResourcesFromCustomPath() {
    testPlaygroundResources(
        PlaygroundTestHelper.CUSTOM_LOCAL_CSS_URL,
        PlaygroundTestHelper.CUSTOM_LOCAL_SCRIPT_URL,
        PlaygroundTestHelper.CUSTOM_LOCAL_FAVICON_URL,
        PlaygroundTestHelper.CUSTOM_LOCAL_LOGO_URL);
  }
}
