package graphql.kickstart.autoconfigure.annotations.test.subscription;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import io.reactivex.Flowable;

public class TestDataFetcher implements DataFetcher<Object> {

  @Override
  public Object get(DataFetchingEnvironment environment) {
    return Flowable.just("some value");
  }
}
