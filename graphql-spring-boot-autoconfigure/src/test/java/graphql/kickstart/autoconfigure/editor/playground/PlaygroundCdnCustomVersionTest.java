package graphql.kickstart.autoconfigure.editor.playground;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PlaygroundTestConfig.class)
@AutoConfigureMockMvc
@ActiveProfiles("playground")
@TestPropertySource("classpath:application-playground-cdn-custom-version-test.properties")
class PlaygroundCdnCustomVersionTest extends PlaygroundResourcesTestBase {

  @Test
  void shouldLoadSpecifiedVersionFromCdn() throws Exception {
    testPlaygroundResources(
        PlaygroundTestHelper.CUSTOM_VERSION_CSS_CDN_PATH,
        PlaygroundTestHelper.CUSTOM_VERSION_SCRIPT_CDN_PATH,
        PlaygroundTestHelper.CUSTOM_VERSION_FAVICON_CDN_PATH,
        PlaygroundTestHelper.CUSTOM_VERSION_LOGO_CDN_PATH);
  }
}
