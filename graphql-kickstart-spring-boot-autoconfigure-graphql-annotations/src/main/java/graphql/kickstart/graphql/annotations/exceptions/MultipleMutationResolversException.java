package graphql.kickstart.graphql.annotations.exceptions;

public class MultipleMutationResolversException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MultipleMutationResolversException() {
        super("Multiple mutation resolvers provided. GraphQL Annotations allows only one resolver.");
    }
}
