package com.oembedler.moon.playground.boot.settings;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PlaygroundRequestIncludeCredentials {
    @JsonProperty("omit")
    OMIT,
    @JsonProperty("include")
    INCLUDE,
    @JsonProperty("same-origin")
    SAME_ORIGIN
}
