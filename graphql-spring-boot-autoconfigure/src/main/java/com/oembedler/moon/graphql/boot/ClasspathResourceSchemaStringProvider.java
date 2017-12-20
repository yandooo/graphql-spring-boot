package com.oembedler.moon.graphql.boot;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

public class ClasspathResourceSchemaStringProvider implements SchemaStringProvider {

  @Autowired
  private ApplicationContext applicationContext;

  @Override
  public List<String> schemaStrings() throws IOException {
    Resource[] resources = applicationContext.getResources("classpath*:**/*.graphqls");
    if(resources.length <= 0) {
      throw new IllegalStateException("No *.graphqls files found on classpath.  Please add a graphql schema to the classpath or add a SchemaParser bean to your application context.");
    }

    List<String> schemaStrings = new ArrayList<>(resources.length);
    for(Resource resource : resources) {
      schemaStrings.add(readSchema(resource));
    }
    return schemaStrings;
  }

  private String readSchema(Resource resource) throws IOException {
    StringWriter writer = new StringWriter();
    IOUtils.copy(resource.getInputStream(), writer);
    return writer.toString();
  }

}
