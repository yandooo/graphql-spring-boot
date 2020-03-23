package graphql.kickstart.playground.boot.properties.settings;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PlaygroundEditorTheme {
    @JsonProperty("light")
    LIGHT,
    @JsonProperty("dark")
    DARK
}
