package com.graphql.spring.boot.test;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.coxautodev.graphql.tools.SchemaParserDictionary;
import com.coxautodev.graphql.tools.SchemaParserOptions;
import com.graphql.spring.boot.test.GraphQLTestAutoConfiguration;
import com.oembedler.moon.graphql.boot.*;
import graphql.execution.ExecutionStrategy;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.preparsed.PreparsedDocumentProvider;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaParser;
import graphql.servlet.*;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.servlet.MultipartConfigElement;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

/**
 * Annotation that can be specified on a test class in combination with {@code @RunWith(SpringRunner.class)}
 * for running tests that focus <strong>only</strong> on GraphQL components.
 * <p>
 * Provides the following features over the regular <strong>Spring TestContext Framework</strong>:
 * <ul>
 * <li>By default a fully running web server will be started listening on a
 * {@link org.springframework.boot.test.context.SpringBootTest.WebEnvironment#RANDOM_PORT random} port.</li>
 * <li>Disables full auto-configuration and instead apply only configuration relevant to
 * GraphQL tests (i.e. beans required to create the GraphQL servlet but not {@code @Component},
 * {@code @Service} or {@code @Repository} beans). See {@link #includeFilters()} for a complete list of classes
 * that are included.</li>
 * <li>Sets the active profile to <strong>test</strong>.</li>
 * <li>Registers a {@link com.graphql.spring.boot.test.GraphQLTestTemplate GraphQLTestTemplate} bean for use
 * in GraphQL tests that are using a fully running web server.</li>
 * <li>Typically {@code GraphQLTest} is used in combination with
 * {@link org.springframework.boot.test.mock.mockito.MockBean @MockBean} or
 * {@link org.springframework.context.annotation.Import @Import} to create any collaborators required by your
 * {@code GraphQLResolver} beans.</li>
 * </ul>
 * <p>
 * If you are looking to load your full application configuration and use
 * {@link com.graphql.spring.boot.test.GraphQLTestTemplate GraphQLTestTemplate} you should consider
 * {@link SpringBootTest @SpringBootTest} rather thatn this annotation.
 *
 * @author Michiel Oliemans
 * @since 5.0.2
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@OverrideAutoConfiguration(enabled = false)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles
@ComponentScan
@ImportAutoConfiguration
public @interface GraphQLTest {

    String[] value() default {};

    @AliasFor(
            annotation = ActiveProfiles.class
    )
    String[] profiles() default {"test"};

    @AliasFor(
            annotation = SpringBootTest.class
    )
    SpringBootTest.WebEnvironment webEnvironment() default SpringBootTest.WebEnvironment.RANDOM_PORT;

    @AliasFor(
            annotation = ImportAutoConfiguration.class
    )
    Class<?>[] classes() default {
            ServletWebServerFactoryAutoConfiguration.class,
            GraphQLJavaToolsAutoConfiguration.class,
            GraphQLWebAutoConfiguration.class,
            GraphQLTestAutoConfiguration.class,
            PropertySourcesPlaceholderConfigurer.class,
            WebSocketServletAutoConfiguration.class
    };

    @AliasFor(
            annotation = ComponentScan.class
    )
    ComponentScan.Filter[] includeFilters() default {
            @ComponentScan.Filter(type = ASSIGNABLE_TYPE, classes = {
                    SchemaParser.class,
                    GraphQLResolver.class,
                    SchemaParserDictionary.class,
                    GraphQLScalarType.class,
                    SchemaParserOptions.class,
                    GraphQLSchema.class,
                    GraphQLSchemaProvider.class,
                    GraphQLServletListener.class,
                    Instrumentation.class,
                    GraphQLErrorHandler.class,
                    ExecutionStrategy.class,
                    GraphQLContextBuilder.class,
                    GraphQLRootObjectBuilder.class,
                    ObjectMapperConfigurer.class,
                    PreparsedDocumentProvider.class,
                    CorsFilter.class,
                    ExecutionStrategyProvider.class,
                    GraphQLInvocationInputFactory.class,
                    GraphQLQueryInvoker.class,
                    GraphQLObjectMapper.class,
                    SimpleGraphQLHttpServlet.class,
                    GraphQLWebsocketServlet.class,
                    ServerEndpointExporter.class,
                    MultipartConfigElement.class
            })
    };

}
