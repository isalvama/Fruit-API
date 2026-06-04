package cat.itacademy.s04.s02.n01.fruit;
import cat.itacademy.s04.s02.n01.fruit.application.repository.FruitRepository;
import cat.itacademy.s04.s02.n01.fruit.controller.CreateFruitRequestDTO;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import cat.itacademy.s04.s02.n01.fruit.domain.model.FruitName;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Weight;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
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
    private static final Fruit FRUIT = Fruit.create(FruitName.of(NAME), Weight.inKiloGrams(WEIGHT));

    @Autowired
    private FruitRepository fruitRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    public void clear(){

    }

    @Nested
    @DisplayName("PUT /api/users")
    class CreateFruit {
        @Test
        void createFruitInKg_returns201WithLocationAndBodyFruitWithId() throws Exception {
            CreateFruitRequestDTO createFruitDTO = new CreateFruitRequestDTO(NAME, WEIGHT, MAGNITUDE);

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createFruitDTO)));
            result.andExpect(status().isCreated())
                    .andExpect(header().string("Location", containsString(API_URL + "/")))
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.name").value(NAME))
                    .andExpect(jsonPath("$.weightInKg").value(WEIGHT));
        }
    }

    @Nested
    @DisplayName("GET /api/users")
    class GetFruits {
        @Test
        void getFruits_returns201WithListOfRegisteredFruitData() throws Exception {
            String fruit2Name = "Kiwi";
            double fruit2WeightAmount = 0.3;
            Fruit fruit2 = Fruit.create(FruitName.of(fruit2Name), Weight.inKiloGrams(fruit2WeightAmount));
            fruitRepository.saveFruit(FRUIT);
            fruitRepository.saveFruit(fruit2);
            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(API_URL)
                    .contentType(MediaType.APPLICATION_JSON));

            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").exists())
                    .andExpect(jsonPath("$[0].name").value(NAME))
                    .andExpect(jsonPath("$[0].weightInKg").value(WEIGHT))

                    .andExpect(jsonPath("$[1].id").exists())
                    .andExpect(jsonPath("$[1].name").value(fruit2Name))
                    .andExpect(jsonPath("$[1].weightInKg").value(fruit2WeightAmount));
        }

        @Test
        @DisplayName("returns 404 Not Found when there are no registered fruits")
        void getFruit_returns404NotFound() throws Exception {

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(API_URL)
                    .contentType(MediaType.APPLICATION_JSON));

            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.title", Matchers.containsString("Fruit Not Found")))
                    .andExpect(jsonPath("$.detail", Matchers.containsString("no registered fruits")));
        }

    }
}
