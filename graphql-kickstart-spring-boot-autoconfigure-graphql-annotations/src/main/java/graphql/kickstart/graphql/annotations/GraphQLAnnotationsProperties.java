package graphql.kickstart.graphql.annotations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
@ConfigurationProperties(prefix = "graphql.annotations")
public class GraphQLAnnotationsProperties {

    /**
     * The base package where GraphQL definitions (resolvers, types etc.) are searched for.
     */
    private @NotBlank String basePackage;

    /**
     * Set if fields should be globally prettified (removes get/set/is prefixes from names). Defaults to true.
     */
    @Builder.Default
    private boolean alwaysPrettify = true;
}
