package com.oembedler.moon.playground.boot.settings;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaygroundSchemaSettings {

    private Boolean disableComments;

    @JsonUnwrapped(prefix = "polling.")
    private PlaygroundSchemaPollingSettings polling;
}
