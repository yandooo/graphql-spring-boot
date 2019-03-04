package com.oembedler.moon.altair.boot;

        import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
        import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
        import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author Moncef AOUDIA
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(DispatcherServlet.class)
public class AltairAutoConfiguration {
    @Bean
    @ConditionalOnProperty(value = "altair.enabled", havingValue = "true", matchIfMissing = true)
    AltairController altairController() {
        return new AltairController();
    }
}
