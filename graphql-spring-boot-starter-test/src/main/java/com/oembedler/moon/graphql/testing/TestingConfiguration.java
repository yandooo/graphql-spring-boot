package com.oembedler.moon.graphql.testing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestingConfiguration {

  @Bean
  public GraphQLTestUtils graphQLTestUtils(){
    return new GraphQLTestUtils();
  }
}
