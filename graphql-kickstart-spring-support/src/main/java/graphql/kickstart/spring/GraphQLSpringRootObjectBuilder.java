package graphql.kickstart.spring;

import org.springframework.web.server.ServerWebExchange;

interface GraphQLSpringRootObjectBuilder {

  Object build(ServerWebExchange serverWebExchange);

}
