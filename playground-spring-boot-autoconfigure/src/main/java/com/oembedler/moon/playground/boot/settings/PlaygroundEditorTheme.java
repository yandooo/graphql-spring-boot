package com.oembedler.moon.playground.boot.settings;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PlaygroundEditorTheme {
    @JsonProperty("light")
    LIGHT,
    @JsonProperty("dark")
    DARK
}
