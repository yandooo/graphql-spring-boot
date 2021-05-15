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
@TestPropertySource("classpath:application-playground-custom-static-path.properties")
class PlaygroundCustomStaticPathTest extends PlaygroundResourcesTestBase {

  @Test
  void shouldLoadStaticResourcesFromCustomPath() throws Exception {
    testPlaygroundResources(
        PlaygroundTestHelper.CUSTOM_LOCAL_CSS_URL,
        PlaygroundTestHelper.CUSTOM_LOCAL_SCRIPT_URL,
        PlaygroundTestHelper.CUSTOM_LOCAL_FAVICON_URL,
        PlaygroundTestHelper.CUSTOM_LOCAL_LOGO_URL);
  }
}
