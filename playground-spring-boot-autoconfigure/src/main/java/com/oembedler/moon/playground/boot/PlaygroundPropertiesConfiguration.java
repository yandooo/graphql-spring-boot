package com.oembedler.moon.playground.boot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@ConfigurationProperties(prefix = "graphql")
@Validated
public class PlaygroundPropertiesConfiguration {

    private PlaygroundProperties playground = new PlaygroundProperties();
}
