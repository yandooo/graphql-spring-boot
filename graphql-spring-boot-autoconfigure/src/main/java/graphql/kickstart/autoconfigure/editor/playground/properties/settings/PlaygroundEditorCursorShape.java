package graphql.kickstart.autoconfigure.editor.playground.properties.settings;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PlaygroundEditorCursorShape {
  @JsonProperty("line")
  LINE,
  @JsonProperty("block")
  BLOCK,
  @JsonProperty("underline")
  UNDERLINE
}
