package com.oembedler.moon.playground.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlaygroundTestConfig.class)
@AutoConfigureMockMvc
public class PlaygroundCSRFTest {

    private static final String CSRF_ATTRIBUTE_NAME = "_csrf";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldLoadCSRFData() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(get(PlaygroundTestHelper.DEFAULT_PLAYGROUND_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(CSRF_ATTRIBUTE_NAME))
                .andReturn();

        assertThat(mvcResult.getModelAndView()).isNotNull();
        assertThat(mvcResult.getModelAndView().getModel()).isNotNull();
        assertThat(mvcResult.getModelAndView().getModel().get(CSRF_ATTRIBUTE_NAME)).isInstanceOf(CsrfToken.class);
    }
}
