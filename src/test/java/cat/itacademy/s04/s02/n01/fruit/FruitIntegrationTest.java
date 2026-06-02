package cat.itacademy.s04.s02.n01.fruit;
import cat.itacademy.s04.s02.n01.fruit.controller.CreateFruitDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = FruitApiH2Application.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class FruitIntegrationTest {
    private static final String NAME = "Pineapple";
    private static final Double WEIGHT = 4.6;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("PUT /in-kg")
    class CreateUser {
        @Test
        void createFruitInKg_returns201WithLocationAndBodyFruitWithId() throws Exception {
            CreateFruitDTO createFruitDTO = new CreateFruitDTO(NAME, WEIGHT);
            String id = "1";

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/fruits/in-kg")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createFruitDTO)));
            result.andExpect(status().isCreated())
                    .andExpect(header().string("Location", containsString("/in-kg/" + id)))
                    .andExpect(jsonPath("$.id").value(id))
                    .andExpect(jsonPath("$.name").value(NAME))
                    .andExpect(jsonPath("$.weightInKg").value(WEIGHT));
        }
    }
}
