package graphql.kickstart.autoconfigure.web.servlet;

import graphql.kickstart.servlet.AsyncTaskDecorator;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;

@Configuration
@AutoConfigureBefore(GraphQLWebAutoConfiguration.class)
@ConditionalOnClass(DefaultAuthenticationEventPublisher.class)
public class GraphQLWebSecurityAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public AsyncTaskDecorator delegatingSecurityContextAsyncTaskDecorator() {
    return new DelegatingSecurityContextAsyncTaskDecorator();
  }
}
