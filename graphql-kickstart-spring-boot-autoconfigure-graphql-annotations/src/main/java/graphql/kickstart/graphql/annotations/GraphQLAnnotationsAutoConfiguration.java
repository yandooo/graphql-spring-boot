package graphql.kickstart.graphql.annotations;

import graphql.annotations.AnnotationsSchemaCreator;
import graphql.annotations.annotationTypes.GraphQLTypeExtension;
import graphql.annotations.annotationTypes.directives.definition.GraphQLDirectiveDefinition;
import graphql.annotations.processor.GraphQLAnnotations;
import graphql.annotations.processor.typeFunctions.TypeFunction;
import graphql.kickstart.graphql.annotations.exceptions.MissingQueryResolverException;
import graphql.kickstart.graphql.annotations.exceptions.MultipleMutationResolversException;
import graphql.kickstart.graphql.annotations.exceptions.MultipleQueryResolversException;
import graphql.kickstart.graphql.annotations.exceptions.MultipleSubscriptionResolversException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.kickstart.tools.GraphQLSubscriptionResolver;
import graphql.kickstart.tools.boot.GraphQLJavaToolsAutoConfiguration;
import graphql.relay.Relay;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.ReflectionsException;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static graphql.annotations.AnnotationsSchemaCreator.newAnnotationsSchema;

@Configuration
@AutoConfigureBefore({GraphQLJavaToolsAutoConfiguration.class})
@EnableConfigurationProperties(GraphQLAnnotationsProperties.class)
@RequiredArgsConstructor
@Slf4j
public class GraphQLAnnotationsAutoConfiguration {

    private final GraphQLAnnotationsProperties graphQLAnnotationsProperties;
    private final Optional<Relay> relay;
    private final List<TypeFunction> typeFunctions;
    private final List<GraphQLScalarType> customScalarTypes;

    @Bean
    @ConditionalOnMissingBean
    public GraphQLAnnotations graphQLAnnotations() {
        return new GraphQLAnnotations();
    }

    @Bean
    public GraphQLSchema graphQLSchema(final GraphQLAnnotations graphQLAnnotations) {
        log.info("Using GraphQL Annotations library to build the schema. Schema definition files will be ignored.");
        log.info("GraphQL classes are searched in the following package (including subpackages): {}",
            graphQLAnnotationsProperties.getBasePackage());
        final AnnotationsSchemaCreator.Builder builder = newAnnotationsSchema();
        final Reflections reflections = new Reflections(graphQLAnnotationsProperties.getBasePackage());
        builder.setAlwaysPrettify(graphQLAnnotationsProperties.isAlwaysPrettify());
        setQueryResolverClass(builder, reflections);
        setMutationResolverClass(builder, reflections);
        setSubscriptionResolverClass(builder, reflections);
        getTypesAnnotatedWith(reflections, GraphQLDirectiveDefinition.class).forEach(directive -> {
           log.info("Registering directive {}", directive);
           builder.directive(directive);
        });
        getTypesAnnotatedWith(reflections, GraphQLTypeExtension.class).forEach(typeExtension -> {
           log.info("Registering type extension {}", typeExtension);
           builder.typeExtension(typeExtension);
        });
        typeFunctions.forEach(typeFunction -> {
            log.info("Registering type function {}", typeFunction.getClass());
            builder.typeFunction(typeFunction);
        });
        if (!customScalarTypes.isEmpty()) {
            builder.typeFunction(new GraphQLScalarTypeFunction(customScalarTypes));
        }
        if (graphQLAnnotations.getClass().equals(GraphQLAnnotations.class)) {
            log.info("Using default GraphQL Annotation processor.");
        } else {
            log.info("Using custom annotation process of type {}", graphQLAnnotations.getClass());
        }
        builder.setAnnotationsProcessor(graphQLAnnotations);
        relay.ifPresent(r -> {
            log.info("Registering relay {}", r.getClass());
            builder.setRelay(r);
        });
        return builder.build();
    }

    private void setSubscriptionResolverClass(
        final AnnotationsSchemaCreator.Builder builder,
        final Reflections reflections
    ) {
        final Set<Class<? extends GraphQLSubscriptionResolver>> subscriptionResolvers
            = getSubTypesOf(reflections, GraphQLSubscriptionResolver.class);
        if (subscriptionResolvers.size() > 1) {
            throw new MultipleSubscriptionResolversException();
        }
        subscriptionResolvers.stream().findFirst().ifPresent(subscriptionClass -> {
            log.info("Registering subscription resolver class: {}", subscriptionClass);
            builder.subscription(subscriptionClass);
        });
    }

    private void setMutationResolverClass(
        final AnnotationsSchemaCreator.Builder builder,
        final Reflections reflections
    ) {
        final Set<Class<? extends GraphQLMutationResolver>> mutationResolvers
            = getSubTypesOf(reflections, GraphQLMutationResolver.class);
        if (mutationResolvers.size() > 1) {
            throw new MultipleMutationResolversException();
        }
        mutationResolvers.stream().findFirst().ifPresent(mutationClass -> {
            log.info("Registering mutation resolver class: {}", mutationClass);
            builder.mutation(mutationClass);
        });
    }

    private void setQueryResolverClass(
        final AnnotationsSchemaCreator.Builder builder,
        final Reflections reflections
    ) {
        final Set<Class<? extends GraphQLQueryResolver>> queryResolvers
            = getSubTypesOf(reflections, GraphQLQueryResolver.class);
        if (queryResolvers.size() == 0) {
            throw new MissingQueryResolverException();
        }
        if (queryResolvers.size() > 1) {
            throw new MultipleQueryResolversException();
        }
        queryResolvers.stream().findFirst().ifPresent(queryClass -> {
            log.info("Registering query resolver class: {}", queryClass);
            builder.query(queryClass);
        });
    }

    /**
     * Workaround for a bug in Reflections - {@link Reflections#getTypesAnnotatedWith)} will throw a
     * {@link ReflectionsException} if there are no types with annotations in the specified package.
     * @param reflections the {@link Reflections} instance
     * @param annotation the annotation class
     * @return The set of classes annotated with the specified annotation, or an empty set if no annotated classes
     * found.
     */
    private Set<Class<?>> getTypesAnnotatedWith(
        final Reflections reflections,
        final Class<? extends Annotation> annotation
    ) {
        try {
            return reflections.getTypesAnnotatedWith(annotation);
        } catch (ReflectionsException e) {
            return Collections.emptySet();
        }
    }

    /**
     * Workaround for a bug in Reflections - {@link Reflections#getSubTypesOf(Class)} will throw a
     * {@link ReflectionsException} if there are no classes in the specified package.
     * @param reflections the {@link Reflections} instance
     * @param aClass a class
     * @return The set of classes that are subclasses of the specified class, or empty set if no annotations found.
     * @see <a href="https://github.com/ronmamo/reflections/issues/273">Issue #273</>
     */
    private <T> Set<Class<? extends T>> getSubTypesOf(
        final Reflections reflections,
        final Class<T> aClass
    ) {
        try {
            return reflections.getSubTypesOf(aClass);
        } catch (ReflectionsException e) {
            return Collections.emptySet();
        }
    }
}