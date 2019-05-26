package com.oembedler.moon.playground.boot.settings;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PlaygroundEditorCursorShape {
    @JsonProperty("line")
    LINE,
    @JsonProperty("block")
    BLOCK,
    @JsonProperty("underline")
    UNDERLINE
}
