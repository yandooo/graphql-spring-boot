package com.oembedler.moon.playground.boot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.oembedler.moon.playground.boot.settings.PlaygroundSettings;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaygroundProperties {

    @NotEmpty
    private String endpoint = "/graphql";

    @NotEmpty
    private String subscriptionEndpoint = "/subscriptions";

    @JsonIgnore
    private boolean cdnEnabled;

    @NotEmpty
    @JsonIgnore
    private String cdnVersion = "latest";

    @JsonIgnore
    private String pageTitle = "Playground";

    private PlaygroundSettings settings;

    private List<PlaygroundTab> tabs;
}
