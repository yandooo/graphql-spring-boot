package com.oembedler.moon.playground.boot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlaygroundAutoConfiguration.class, properties = "playground.enabled=false")
public class PlaygroundDisabledTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void playgroundShouldNotLoadIfDisabled() {
        applicationContext.getBean(PlaygroundController.class);
    }
}
