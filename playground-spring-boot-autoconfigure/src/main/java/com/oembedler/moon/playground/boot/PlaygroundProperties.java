package com.oembedler.moon.playground.boot;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oembedler.moon.playground.boot.settings.PlaygroundSettings;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaygroundProperties {

    @NotEmpty
    @JsonProperty
    private String endpoint = "/graphql";

    @NotEmpty
    @JsonProperty
    private String subscriptionEndpoint = "/subscriptions";

    private boolean cdnEnabled;

    @NotEmpty
    private String cdnVersion = "latest";

    private String pageTitle = "Playground";

    @JsonProperty
    private PlaygroundSettings settings;
}
