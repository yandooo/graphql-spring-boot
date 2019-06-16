package com.oembedler.moon.playground.boot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlaygroundTestConfig.class, properties = "graphql.playground.enabled=false")
@AutoConfigureMockMvc
public class PlaygroundDisabledTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void playgroundShouldNotLoadIfDisabled() {
        applicationContext.getBean(PlaygroundController.class);
    }

    @Test
    public void playgroundEndpointShouldNotExist() throws Exception {
        mockMvc.perform(get(PlaygroundTestHelper.DEFAULT_PLAYGROUND_ENDPOINT)).andExpect(status().isNotFound());
    }
}
