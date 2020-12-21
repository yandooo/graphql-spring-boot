package graphql.kickstart.graphql.annotations.exceptions;

public class MissingQueryResolverException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public MissingQueryResolverException() {
    super(
        "No query resolver provided. When using GraphQL Annotations, one query resolver must be provided.");
  }
}
