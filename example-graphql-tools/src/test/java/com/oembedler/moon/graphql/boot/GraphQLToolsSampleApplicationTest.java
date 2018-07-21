package com.oembedler.moon.graphql.boot;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static graphql.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GraphQLToolsSampleApplicationTest {

    @Autowired
    private GraphQLTestUtils graphQLTestUtils;

    @Test
    public void get_comments() throws IOException {
        JsonNode parsedResponse = graphQLTestUtils.perform("graphql/post-get-comments.graphql");
        assertNotNull(parsedResponse);
        assertNotNull(parsedResponse.get("data"));
        assertNotNull(parsedResponse.get("data").get("post"));
        assertEquals("1", parsedResponse.get("data").get("post").get("id").asText());
    }

}
