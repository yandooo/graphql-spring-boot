package graphql.kickstart.autoconfigure.web.servlet;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("graphql.servlet.subscriptions.websocket")
class GraphQLSubscriptionWebsocketProperties {

  private String path = "/subscriptions";
}
