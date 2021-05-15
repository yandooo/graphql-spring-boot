package graphql.kickstart.autoconfigure.editor.graphiql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {GraphiQLAutoConfiguration.class})
@AutoConfigureMockMvc
@TestPropertySource("classpath:enabled-config.properties")
class GraphiQLEnabledTest {

  @Autowired private ApplicationContext applicationContext;
  @Autowired private MockMvc mockMvc;

  @Test
  void graphiqlLoads() {
    assertThat(applicationContext.getBean(GraphiQLController.class)).isNotNull();
  }

  @Test
  void graphiqlShouldBeAvailableAtDefaultEndpoint() throws Exception {
    mockMvc
        .perform(get("/graphiql"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
        .andExpect(content().string(not(is(emptyString()))))
        .andReturn();
  }
}
