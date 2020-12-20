package com.graphql.sample.boot;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Disabled
public class SpringBootTestWithoutWebEnvironmentTest {

  @Test
  public void loads_without_complaining_about_missing_ServerContainer() {

  }

}
