package com.oembedler.moon.playground.boot.settings;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaygroundPrettierSettings {

    @Min(1)
    private Integer printWidth;
    @Min(1)
    private Integer tabWidth;
    private Boolean useTabs;
}
