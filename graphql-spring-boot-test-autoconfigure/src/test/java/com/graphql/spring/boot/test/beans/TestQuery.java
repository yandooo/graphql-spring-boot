package com.graphql.spring.boot.test.beans;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Service;

@Service
public class TestQuery implements GraphQLQueryResolver {

  public String testQuery() {
    return "foo";
  }
}
