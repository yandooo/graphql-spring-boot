package graphql.kickstart.autoconfigure.tools;

import graphql.kickstart.tools.GraphQLQueryResolver;

/** @author Andrew Potter */
public class Query implements GraphQLQueryResolver {

  public String echo(String string) {
    return string;
  }
}
