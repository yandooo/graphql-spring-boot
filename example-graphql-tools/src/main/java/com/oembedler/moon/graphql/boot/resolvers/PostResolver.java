package com.oembedler.moon.graphql.boot.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
class PostResolver implements GraphQLResolver<Post> {

    public List<Comment> getComments(Post post) {
        return Collections.emptyList();
    }

}
