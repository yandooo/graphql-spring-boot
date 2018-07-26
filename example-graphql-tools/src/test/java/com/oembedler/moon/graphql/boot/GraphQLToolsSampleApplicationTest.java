package com.oembedler.moon.graphql.boot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.oembedler.moon.graphql.boot.resolvers.Post;
import com.oembedler.moon.graphql.testing.GraphQLResponse;
import com.oembedler.moon.graphql.testing.GraphQLTestUtils;
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
        GraphQLResponse response = graphQLTestUtils.perform("graphql/post-get-comments.graphql");
        assertNotNull(response);
        JsonNode parsedResponse = response.readTree();
        assertNotNull(parsedResponse);
        assertNotNull(parsedResponse.get("data"));
        assertNotNull(parsedResponse.get("data").get("post"));
        assertEquals("1", parsedResponse.get("data").get("post").get("id").asText());
    }

    @Test
    public void create_post() throws IOException {
        ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("text", "lorem ipsum dolor sit amet");
        GraphQLResponse response = graphQLTestUtils.perform("graphql/create-post.graphql", variables);
        assertNotNull(response);
        JsonNode parsedResponse = response.readTree();
        assertNotNull(parsedResponse);
        assertNotNull(parsedResponse.get("data"));
        assertNotNull(parsedResponse.get("data").get("createPost"));
        assertNotNull(parsedResponse.get("data").get("createPost").get("id"));
    }

}
