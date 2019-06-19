package com.oembedler.moon.playground.boot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlaygroundTestConfig.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-playground-cdn-custom-version-test.properties")
public class PlaygroundCdnCustomVersionTest extends PlaygroundResourcesTestBase {

    private final String CSS_CDN_PATH
            = "https://cdn.jsdelivr.net/npm/graphql-playground-react@1.7.10/build/static/css/index.css";
    private final String SCRIPT_CDN_PATH
            = "https://cdn.jsdelivr.net/npm/graphql-playground-react@1.7.10/build/static/js/middleware.js";
    private final String FAVICON_CDN_PATH
            = "https://cdn.jsdelivr.net/npm/graphql-playground-react@1.7.10/build/favicon.png";
    private final String LOGO_CDN_PATH
            = "https://cdn.jsdelivr.net/npm/graphql-playground-react@1.7.10/build/logo.png";

    @Test
    public void shouldLoadSpecifiedVersionFromCdn() throws Exception {
        testPlaygroundResources(CSS_CDN_PATH, SCRIPT_CDN_PATH, FAVICON_CDN_PATH, LOGO_CDN_PATH);
    }
}
