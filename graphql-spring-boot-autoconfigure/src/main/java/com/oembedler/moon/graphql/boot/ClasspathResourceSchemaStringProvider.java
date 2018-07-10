package com.oembedler.moon.graphql.boot;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClasspathResourceSchemaStringProvider implements SchemaStringProvider {

  @Autowired
  private ApplicationContext applicationContext;
  @Value("${graphql.tools.schemaLocationPattern:**/*.graphqls}")
  private String schemaLocationPattern;

  @Override
  public List<String> schemaStrings() throws IOException {
    Resource[] resources = applicationContext.getResources("classpath*:" + schemaLocationPattern);
    if (resources.length <= 0) {
      throw new IllegalStateException(
          "No graphql schema files found on classpath with location pattern '"
              + schemaLocationPattern
              + "'.  Please add a graphql schema to the classpath or add a SchemaParser bean to your application context.");
    }

    return Arrays.stream(resources)
        .map(this::readSchema)
        .collect(Collectors.toList());
  }

  private String readSchema(Resource resource) {
    StringWriter writer = new StringWriter();
    try (InputStream inputStream = resource.getInputStream()) {
      IOUtils.copy(inputStream, writer);
    } catch (IOException e) {
      throw new IllegalStateException("Cannot read graphql schema from resource " + resource, e);
    }
    return writer.toString();
  }

}
