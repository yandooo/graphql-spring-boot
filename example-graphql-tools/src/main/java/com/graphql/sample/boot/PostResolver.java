package com.graphql.sample.boot;

import graphql.kickstart.tools.GraphQLResolver;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
class PostResolver implements GraphQLResolver<Post> {

  public List<Comment> getComments(Post post) {
    return Collections.emptyList();
  }

}
