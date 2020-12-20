package graphql.kickstart.spring.web.boot.sample;

import com.oembedler.moon.graphql.engine.relay.ConnectionObjectType;
import com.oembedler.moon.graphql.engine.relay.EdgeObjectType;
import graphql.kickstart.spring.web.boot.sample.schema.objecttype.TodoObjectType;
import java.util.List;

/**
 * @author <a href="mailto:java.lang.RuntimeException@gmail.com">oEmbedler Inc.</a>
 */
public class TodoSimpleListConnection extends SimpleListConnection {

  public TodoSimpleListConnection(List<?> data) {
    super(data);
  }

  @Override
  public <T extends EdgeObjectType> T createEdgeObject() {
    return (T) new TodoObjectType.TodoEdgeObjectType();
  }

  @Override
  public <T extends ConnectionObjectType> T createConnectionObject() {
    return (T) new TodoObjectType.TodoConnectionObjectType();
  }

}
