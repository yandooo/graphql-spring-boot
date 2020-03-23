package graphql.kickstart.playground.boot.properties;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PlaygroundStaticPathSettings {

    @NotBlank
    private String base = "/vendor/playground";
}
