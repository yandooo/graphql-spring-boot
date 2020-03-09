package com.oembedler.moon.voyager.boot;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Guilherme Blanco
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(value = "voyager.enabled", havingValue = "true", matchIfMissing = true)
public class VoyagerAutoConfiguration
{
    @Bean
    VoyagerController voyagerController() {
        return new VoyagerController();
    }

    @Bean
    VoyagerIndexHtmlTemplate voyagerIndexHtmlTemplate() {
        return new VoyagerIndexHtmlTemplate();
    }
}
