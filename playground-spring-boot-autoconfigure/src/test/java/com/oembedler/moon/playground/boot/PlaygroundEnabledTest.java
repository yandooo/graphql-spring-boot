package com.oembedler.moon.playground.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PlaygroundAutoConfiguration.class, ObjectMapper.class},
        properties = "graphql.playground.enabled=true")
public class PlaygroundEnabledTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void playgroundLoads() {
        assertThat(applicationContext.getBean(PlaygroundController.class)).isNotNull();
    }
}
