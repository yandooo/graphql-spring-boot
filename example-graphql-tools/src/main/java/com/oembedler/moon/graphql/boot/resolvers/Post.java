package com.oembedler.moon.graphql.boot.resolvers;

class Post {

    private Long id;
    private String text;

    Post(Long id) {
        this.id = id;
    }

    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
    }
}
