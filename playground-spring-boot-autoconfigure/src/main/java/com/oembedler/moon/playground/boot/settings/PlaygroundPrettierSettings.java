package com.oembedler.moon.playground.boot.settings;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaygroundPrettierSettings {

    private Integer printWidth;
    private Integer tabWidth;
    private Boolean useTabs;
}
