package graphql.kickstart.spring;

import org.springframework.web.server.ServerWebExchange;

interface GraphQLSpringContextBuilder {

  GraphQLSpringContext build(ServerWebExchange serverWebExchange);

}
