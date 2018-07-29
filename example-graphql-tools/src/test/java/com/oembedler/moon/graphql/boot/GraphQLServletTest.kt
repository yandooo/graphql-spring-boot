package com.oembedler.moon.graphql.boot


import com.graphql.spring.boot.test.GraphQLTestTemplate
import com.graphql.spring.boot.test.GraphQLTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@GraphQLTest
class GraphQLServletTest {

    @Autowired
    private lateinit var graphQLTestTemplate: GraphQLTestTemplate

    @Test
    fun `query over HTTP POST multipart with variables returns data requires multipartconfig`() {
        val response = graphQLTestTemplate.postMultipart("query echo(\$string: String!)", """{"string":"echo"}""")
        assertNotNull(response)
        assertEquals(HttpStatus.OK, response.statusCode)
    }

}
