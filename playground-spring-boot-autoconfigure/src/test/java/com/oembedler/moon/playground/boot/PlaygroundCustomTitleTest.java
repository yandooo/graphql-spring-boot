package com.oembedler.moon.playground.boot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.oembedler.moon.playground.boot.PlaygroundTestHelper.assertTitle;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlaygroundTestConfig.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-playground-custom-title.properties")
public class PlaygroundCustomTitleTest {

    private static final String CUSTOM_TITLE = "My CustomTest Title";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldUseTheCustomPageTitle() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(get(PlaygroundTestHelper.DEFAULT_PLAYGROUND_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(model().attribute(PlaygroundTestHelper.PAGE_TITLE_FIELD_NAME, CUSTOM_TITLE))
                .andReturn();

        final Document document = Jsoup.parse(mvcResult.getResponse().getContentAsString());
        assertTitle(document, CUSTOM_TITLE);
    }
}
