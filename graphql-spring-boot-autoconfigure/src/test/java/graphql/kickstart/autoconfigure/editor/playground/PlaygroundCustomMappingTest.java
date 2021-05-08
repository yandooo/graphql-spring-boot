package graphql.kickstart.autoconfigure.editor.playground;

import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PlaygroundTestConfig.class)
@AutoConfigureMockMvc
@ActiveProfiles("playground")
@TestPropertySource("classpath:application-playground-mapping-test.properties")
class PlaygroundCustomMappingTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void shouldUseTheConfiguredRequestMapping() throws Exception {
    mockMvc
        .perform(get(PlaygroundTestHelper.CUSTOM_MAPPING))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
        .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
        .andExpect(content().string(not(blankOrNullString())));
  }
}
