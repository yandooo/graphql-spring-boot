package com.oembedler.moon.playground.boot.properties;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class PlaygroundCdn {

    private boolean enabled;
    @NotEmpty
    private String version = "latest";
}
