package com.oembedler.moon.playground.boot;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(DispatcherServlet.class)
public class PlaygroundAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = "playground.enabled", havingValue = "true", matchIfMissing = true)
    PlaygroundController altairController() {
        return new PlaygroundController();
    }
}
