package com.oembedler.moon.graphql.boot


import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.util.LinkedMultiValueMap

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GraphQLServletTest {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun `query over HTTP POST multipart with variables returns data requires multipartconfig`() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA

        val queryHeader = HttpHeaders()
        queryHeader.contentType = MediaType.APPLICATION_JSON_UTF8
        val queryEntity = HttpEntity("query echo(\$string: String!)", queryHeader)

        val variablesHeader = HttpHeaders()
        variablesHeader.contentType = MediaType.APPLICATION_JSON_UTF8
        val variablesEntity = HttpEntity("{\"string\":\"echo\"}", variablesHeader)

        val multipartRequest = LinkedMultiValueMap<String, HttpEntity<String>>()
        multipartRequest.add("query", queryEntity)
        multipartRequest.add("variables", variablesEntity)

        val requestEntity = HttpEntity(multipartRequest, headers)

        val response = restTemplate.exchange("/graphql", HttpMethod.POST, requestEntity, String::class.java)
        assertNotNull(response)
        assertEquals(HttpStatus.OK, response.statusCode)
    }

}
