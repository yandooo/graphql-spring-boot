package com.oembedler.moon.playground.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oembedler.moon.playground.boot.settings.PlaygroundSettingsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(DispatcherServlet.class)
@EnableConfigurationProperties(PlaygroundSettingsProperties.class)
public class PlaygroundAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = "playground.enabled", havingValue = "true", matchIfMissing = true)
    PlaygroundController playgroundController(final PlaygroundSettingsProperties playgroundSettingsProperties,
            final ObjectMapper objectMapper) {
        return new PlaygroundController(playgroundSettingsProperties, objectMapper);
    }
}
