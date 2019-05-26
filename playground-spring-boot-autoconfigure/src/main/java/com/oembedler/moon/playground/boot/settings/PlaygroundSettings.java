package com.oembedler.moon.playground.boot.settings;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaygroundSettings {

    @JsonUnwrapped(prefix = "editor.")
    private PlaygroundEditorSettings editor;

    @JsonUnwrapped(prefix = "prettier.")
    private PlaygroundPrettierSettings prettier;

    @JsonUnwrapped(prefix = "request.")
    private PlaygroundRequestSettings request;

    @JsonUnwrapped(prefix = "schema.")
    private PlaygroundSchemaSettings schema;

    @JsonUnwrapped(prefix = "tracing.")
    private PlaygroundTracingSettings tracing;
}
