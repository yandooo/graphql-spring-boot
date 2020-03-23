package graphql.kickstart.playground.boot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlaygroundTestConfig.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-playground-mapping-test.properties")
public class PlaygroundCustomMappingTest {

    private static final String CONFIGURED_MAPPING = "/test-mapping";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldUseTheConfiguredRequestMapping() throws Exception {
        mockMvc.perform(get(CONFIGURED_MAPPING))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(content().string(not(isEmptyString())));
    }
}
