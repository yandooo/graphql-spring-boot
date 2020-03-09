package com.oembedler.moon.voyager.boot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author Max David Günther
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnProperty(value = "voyager.enabled", havingValue = "true", matchIfMissing = true)
public class ReactiveVoyagerAutoConfiguration {

    @Value("${voyager.mapping:/voyager}")
    private String voyagerPath;

    @Bean
    ReactiveVoyagerController voyagerController() {
        return new ReactiveVoyagerController();
    }

    @Bean
    public RouterFunction<ServerResponse> voyagerStaticFilesRouter() {
        return RouterFunctions.resources(
        "/vendor/voyager/**",
                new ClassPathResource("static/vendor/voyager/")
        );
    }

    @Bean
    VoyagerIndexHtmlTemplate voyagerIndexHtmlTemplate() {
        return new VoyagerIndexHtmlTemplate();
    }
}
