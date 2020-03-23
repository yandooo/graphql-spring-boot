package graphql.kickstart.spring.web.boot;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@Import(GraphQLServletProperties.class)
@EnableConfigurationProperties
public class TestAutoConfiguration {


}
