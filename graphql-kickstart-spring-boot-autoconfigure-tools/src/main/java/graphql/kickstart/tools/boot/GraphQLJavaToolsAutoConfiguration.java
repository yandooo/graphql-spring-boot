package graphql.kickstart.tools.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import graphql.kickstart.execution.config.GraphQLSchemaProvider;
import graphql.kickstart.tools.CoroutineContextProvider;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.kickstart.tools.ObjectMapperConfigurer;
import graphql.kickstart.tools.PerFieldObjectMapperProvider;
import graphql.kickstart.tools.SchemaParser;
import graphql.kickstart.tools.SchemaParserBuilder;
import graphql.kickstart.tools.SchemaParserDictionary;
import graphql.kickstart.tools.SchemaParserOptions;
import graphql.kickstart.tools.SchemaParserOptions.GenericWrapper;
import graphql.kickstart.tools.TypeDefinitionFactory;
import graphql.kickstart.tools.proxy.ProxyHandler;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.visibility.GraphqlFieldVisibility;
import graphql.schema.visibility.NoIntrospectionGraphqlFieldVisibility;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Objects.nonNull;

/**
 * @author Andrew Potter
 */
@Slf4j
@Configuration
@ConditionalOnClass(SchemaParser.class)
@AutoConfigureAfter({JacksonAutoConfiguration.class})
@EnableConfigurationProperties(GraphQLToolsProperties.class)
public class GraphQLJavaToolsAutoConfiguration {

  @Autowired(required = false)
  private SchemaParserDictionary dictionary;

  @Autowired(required = false)
  private GraphQLScalarType[] scalars;

  @Autowired(required = false)
  private List<SchemaDirective> directives;

  @Autowired(required = false)
  private List<SchemaDirectiveWiring> directiveWirings;

  @Autowired(required = false)
  private List<GenericWrapper> genericWrappers;

  @Autowired(required = false)
  private ObjectMapperConfigurer objectMapperConfigurer;

  @Autowired(required = false)
  private List<ProxyHandler> proxyHandlers;

  @Autowired(required = false)
  private CoroutineContextProvider coroutineContextProvider;

  @Autowired(required = false)
  private List<TypeDefinitionFactory> typeDefinitionFactories;

  @Autowired(required = false)
  private GraphqlFieldVisibility fieldVisibility;

  @Autowired
  private GraphQLToolsProperties props;

  @Bean
  @ConditionalOnMissingBean
  public SchemaStringProvider schemaStringProvider() {
    return new ClasspathResourceSchemaStringProvider(props.getSchemaLocationPattern());
  }

  @Bean
  @ConditionalOnMissingBean
  @ConfigurationProperties("graphql.tools.schema-parser-options")
  public SchemaParserOptions.Builder optionsBuilder(
      @Autowired(required = false) PerFieldObjectMapperProvider perFieldObjectMapperProvider
  ) {
    SchemaParserOptions.Builder optionsBuilder = SchemaParserOptions.newOptions();

    if (perFieldObjectMapperProvider != null) {
      optionsBuilder.objectMapperProvider(perFieldObjectMapperProvider);
    } else {
      optionsBuilder.objectMapperConfigurer(objectMapperConfigurer);
    }

    Optional.ofNullable(genericWrappers).ifPresent(optionsBuilder::genericWrappers);

    if (proxyHandlers != null) {
      proxyHandlers.forEach(optionsBuilder::addProxyHandler);
    }

    Optional.ofNullable(coroutineContextProvider).ifPresent(optionsBuilder::coroutineContextProvider);

    if (typeDefinitionFactories != null) {
      typeDefinitionFactories.forEach(optionsBuilder::typeDefinitionFactory);
    }

    Optional.ofNullable(fieldVisibility).ifPresent(optionsBuilder::fieldVisibility);

    return optionsBuilder;
  }

  @Bean
  @ConditionalOnBean({GraphQLResolver.class})
  @ConditionalOnMissingBean
  public SchemaParser schemaParser(
      List<GraphQLResolver<?>> resolvers,
      SchemaStringProvider schemaStringProvider,
      SchemaParserOptions.Builder optionsBuilder
  ) throws IOException {
    SchemaParserBuilder builder = new SchemaParserBuilder();
    if (nonNull(dictionary)) {
      builder.dictionary(dictionary.getDictionary());
    }
    List<String> schemaStrings = schemaStringProvider.schemaStrings();
    schemaStrings.forEach(builder::schemaString);

    if (scalars != null) {
      builder.scalars(scalars);
    }

    builder.options(optionsBuilder.build());

    if (directives != null) {
      directives.forEach(it -> builder.directive(it.getName(), it.getDirective()));
    }

    if (directiveWirings != null) {
      directiveWirings.forEach(builder::directiveWiring);
    }

    return builder
        .resolvers(resolvers)
        .build();
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnProperty(value = "graphql.tools.use-default-objectmapper", havingValue = "true", matchIfMissing = true)
  public PerFieldObjectMapperProvider perFieldObjectMapperProvider(ObjectMapper objectMapper) {
    objectMapper
        .registerModule(new Jdk8Module())
        .registerModule(new KotlinModule());
    return fieldDefinition -> objectMapper;
  }

  @Bean
  @ConditionalOnBean(SchemaParser.class)
  @ConditionalOnMissingBean({GraphQLSchema.class, GraphQLSchemaProvider.class})
  public GraphQLSchema graphQLSchema(SchemaParser schemaParser) {
    return schemaParser.makeExecutableSchema();
  }

  @Bean
  @ConditionalOnProperty(value = "graphql.tools.introspection-enabled", havingValue = "false")
  GraphqlFieldVisibility disableIntrospection() {
    log.warn("GraphQL introspection query disabled! This puts your server in contravention of the "
        + "GraphQL specification and expectations of most clients, so use this option with caution");
    return new NoIntrospectionGraphqlFieldVisibility();
  }
}
