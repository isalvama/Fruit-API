package cat.itacademy.s04.s02.n01.fruit;
import cat.itacademy.s04.s02.n01.fruit.controller.CreateFruitRequestDTO;
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
    private static final String MAGNITUDE = "KILOGRAMS";
    private static final String API_URL = "/api/fruits";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("PUT /in-kg")
    class CreateUser {
        @Test
        void createFruitInKg_returns201WithLocationAndBodyFruitWithId() throws Exception {
            CreateFruitRequestDTO createFruitDTO = new CreateFruitRequestDTO(NAME, WEIGHT, MAGNITUDE);

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createFruitDTO)));
            result.andExpect(status().isCreated())
                    .andExpect(header().string("Location", containsString(API_URL + "/" + 1)))
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value(NAME))
                    .andExpect(jsonPath("$.weightInKg").value(WEIGHT));
        }
    }
}
