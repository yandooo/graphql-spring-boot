package graphql.kickstart.graphql.annotations.exceptions;

public class MultipleQueryResolversException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MultipleQueryResolversException() {
        super("Multiple query resolvers provided. GraphQL Annotations allows only one resolver.");
    }
}
