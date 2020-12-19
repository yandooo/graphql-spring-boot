package graphql.kickstart.graphql.annotations.exceptions;

public class MultipleSubscriptionResolversException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MultipleSubscriptionResolversException() {
        super("Multiple subscription resolvers provided. GraphQL Annotations allows only one resolver.");
    }
}
