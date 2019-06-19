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
@TestPropertySource("classpath:application-playground-custom-static-path.properties")
public class PlaygroundCustomStaticPathTest extends PlaygroundResourcesTestBase {

    private static final String CUSTOM_CSS_URL = "/custom-static-path/static/css/index.css";
    private static final String CUSTOM_SCRIPT_URL = "/custom-static-path/static/js/middleware.js";
    private static final String CUSTOM_FAVICON_URL = "/custom-static-path/favicon.png";
    private static final String CUSTOM_LOGO_URL = "/custom-static-path/logo.png";

    @Test
    public void shouldLoadStaticResourcesFromCustomPath() throws Exception {
        testPlaygroundResources(CUSTOM_CSS_URL, CUSTOM_SCRIPT_URL, CUSTOM_FAVICON_URL, CUSTOM_LOGO_URL);
    }
}
