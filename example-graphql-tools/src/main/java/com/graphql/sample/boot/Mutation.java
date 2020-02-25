package com.graphql.sample.boot;

import graphql.kickstart.tools.GraphQLMutationResolver;
import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class Mutation implements GraphQLMutationResolver {

  public Post createPost(String text){
    Post post = new Post(new Random().nextLong());
    post.setText(text);
    return post;
  }
}
