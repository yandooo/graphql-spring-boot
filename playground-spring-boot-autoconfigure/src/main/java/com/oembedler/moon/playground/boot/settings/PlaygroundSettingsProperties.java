package com.oembedler.moon.playground.boot.settings;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "playground.settings")
@JsonAutoDetect(fieldVisibility =  JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaygroundSettingsProperties {

    @JsonProperty
    @JsonUnwrapped(prefix = "editor.")
    private PlaygroundEditorSettings editor;

    @JsonProperty
    @JsonUnwrapped(prefix = "prettier.")
    private PlaygroundPrettierSettings prettier;

    @JsonProperty
    @JsonUnwrapped(prefix = "request.")
    private PlaygroundRequestSettings request;

    @JsonProperty
    @JsonUnwrapped(prefix = "schema.")
    private PlaygroundSchemaSettings schema;

    @JsonProperty
    @JsonUnwrapped(prefix = "tracing.")
    private PlaygroundTracingSettings tracing;
}
