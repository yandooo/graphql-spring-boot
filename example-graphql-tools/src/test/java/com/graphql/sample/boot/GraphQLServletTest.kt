package com.graphql.sample.boot


import com.graphql.spring.boot.test.GraphQLTestTemplate
import com.graphql.spring.boot.test.GraphQLTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@GraphQLTest
class GraphQLServletTest {

    @Autowired
    private lateinit var graphQLTestTemplate: GraphQLTestTemplate

    @Test
    fun `query over HTTP POST multipart with variables returns data requires multipartconfig`() {
        val response = graphQLTestTemplate.postMultipart("query echo(\$string: String!)", """{"string":"echo"}""")
        assertThat(response).isNotNull
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

}
