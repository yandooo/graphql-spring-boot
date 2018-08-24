package com.oembedler.moon.graphql.boot.resolvers;

public class Post {

    private Long id;
    private String text;

    Post(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
    }
}
