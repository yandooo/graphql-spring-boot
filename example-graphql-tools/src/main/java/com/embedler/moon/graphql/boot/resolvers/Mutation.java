package com.embedler.moon.graphql.boot.resolvers;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import java.time.LocalDateTime;
import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class Mutation implements GraphQLMutationResolver {


  public static class PostInput {
    private LocalDateTime date;

    public void setDate(LocalDateTime date) {
      this.date = date;
    }

    public LocalDateTime getDate() {
      return date;
    }
  }

  public Post createPost(PostInput postInput){
    Post post = new Post(new Random().nextLong());
    post.setDate(postInput.getDate());
    return post;
  }
}
