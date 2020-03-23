package graphql.kickstart.playground.boot.properties;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PlaygroundCdn {

    private boolean enabled;
    @NotBlank
    private String version = "latest";
}
