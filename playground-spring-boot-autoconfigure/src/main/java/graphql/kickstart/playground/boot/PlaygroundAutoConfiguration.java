package graphql.kickstart.playground.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
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
@EnableConfigurationProperties(PlaygroundPropertiesConfiguration.class)
public class PlaygroundAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = "graphql.playground.enabled", havingValue = "true", matchIfMissing = true)
    PlaygroundController playgroundController(final PlaygroundPropertiesConfiguration playgroundPropertiesConfiguration,
            final ObjectMapper objectMapper) {
        return new PlaygroundController(playgroundPropertiesConfiguration, objectMapper);
    }
}
