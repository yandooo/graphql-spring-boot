package graphql.kickstart.autoconfigure.editor.playground.properties.settings;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PlaygroundRequestIncludeCredentials {
  @JsonProperty("omit")
  OMIT,
  @JsonProperty("include")
  INCLUDE,
  @JsonProperty("same-origin")
  SAME_ORIGIN
}
