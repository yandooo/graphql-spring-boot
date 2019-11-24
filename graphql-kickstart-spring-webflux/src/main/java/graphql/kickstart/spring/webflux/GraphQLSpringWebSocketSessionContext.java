package graphql.kickstart.spring.webflux;

import graphql.kickstart.execution.context.GraphQLContext;
import org.springframework.web.reactive.socket.WebSocketSession;

interface GraphQLSpringWebSocketSessionContext extends GraphQLContext {

  WebSocketSession getWebSocketSession();

}
