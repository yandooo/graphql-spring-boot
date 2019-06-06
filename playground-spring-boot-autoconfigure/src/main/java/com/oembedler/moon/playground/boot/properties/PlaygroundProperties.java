package com.oembedler.moon.playground.boot.properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PlaygroundProperties {

    @NotEmpty
    private String endpoint = "/graphql";

    @NotEmpty
    private String subscriptionEndpoint = "/subscriptions";

    @JsonIgnore
    private PlaygroundCdn cdn = new PlaygroundCdn();

    @JsonIgnore
    private String pageTitle = "Playground";

    private PlaygroundSettings settings;

    private Map<String, String> headers = Collections.emptyMap();

    private List<PlaygroundTab> tabs = Collections.emptyList();
}
