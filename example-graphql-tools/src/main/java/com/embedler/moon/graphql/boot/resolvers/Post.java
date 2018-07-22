package com.embedler.moon.graphql.boot.resolvers;

import java.time.LocalDateTime;

class Post {

    private Long id;

    private LocalDateTime date;

    Post(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
