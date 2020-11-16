package graphql.kickstart.playground.boot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PlaygroundTestConfig.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-playground-cdn-test.properties")
public class PlaygroundCdnTest extends PlaygroundResourcesTestBase {

    private final String CSS_CDN_PATH
            = "https://cdn.jsdelivr.net/npm/graphql-playground-react@latest/build/static/css/index.css";
    private final String SCRIPT_CDN_PATH
            = "https://cdn.jsdelivr.net/npm/graphql-playground-react@latest/build/static/js/middleware.js";
    private final String FAVICON_CDN_PATH
            = "https://cdn.jsdelivr.net/npm/graphql-playground-react@latest/build/favicon.png";
    private final String LOGO_CDN_PATH
            = "https://cdn.jsdelivr.net/npm/graphql-playground-react@latest/build/logo.png";

    @Test
    public void shouldLoadLatestVersionFromCdn() throws Exception {
        testPlaygroundResources(CSS_CDN_PATH, SCRIPT_CDN_PATH, FAVICON_CDN_PATH, LOGO_CDN_PATH);
    }
}
