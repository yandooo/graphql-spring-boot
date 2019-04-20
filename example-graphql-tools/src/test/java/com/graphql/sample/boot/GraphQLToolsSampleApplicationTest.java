package com.graphql.sample.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import com.graphql.spring.boot.test.GraphQLTest;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static graphql.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@GraphQLTest
public class GraphQLToolsSampleApplicationTest {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Test
    @Ignore
    public void get_comments() throws IOException {
        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/post-get-comments.graphql");
        assertNotNull(response);
        assertTrue(response.isOk());
        assertEquals("1", response.get("$.data.post.id"));
    }

    @Test
    public void create_post() throws IOException {
        ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("text", "lorem ipsum dolor sit amet");
        GraphQLResponse response = graphQLTestTemplate.perform("graphql/create-post.graphql", variables);
        assertNotNull(response);
        assertNotNull(response.get("$.data.createPost.id"));
    }

}
