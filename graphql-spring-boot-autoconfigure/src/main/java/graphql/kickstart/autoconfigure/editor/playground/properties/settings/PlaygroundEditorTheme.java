package graphql.kickstart.autoconfigure.editor.playground.properties.settings;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PlaygroundEditorTheme {
  @JsonProperty("light")
  LIGHT,
  @JsonProperty("dark")
  DARK
}
