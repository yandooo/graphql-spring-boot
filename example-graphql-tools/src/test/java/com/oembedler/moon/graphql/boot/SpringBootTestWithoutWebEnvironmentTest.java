package com.oembedler.moon.graphql.boot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootTestWithoutWebEnvironmentTest {

    @Test
    public void loads_without_complaining_about_missing_ServerContainer() {

    }

}
