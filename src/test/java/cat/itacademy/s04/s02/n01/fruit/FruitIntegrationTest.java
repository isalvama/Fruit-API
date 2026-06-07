package cat.itacademy.s04.s02.n01.fruit;
import cat.itacademy.s04.s02.n01.fruit.application.repository.FruitRepository;
import cat.itacademy.s04.s02.n01.fruit.controller.CreateFruitRequestDTO;
import cat.itacademy.s04.s02.n01.fruit.controller.UpdateFruitRequestDTO;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import cat.itacademy.s04.s02.n01.fruit.domain.model.FruitName;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Weight;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
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
    @DisplayName("PUT /api/fruits")
    class CreateFruit {
        @Test
        void createFruit_returns201WithLocationAndBodyFruitWithId() throws Exception {
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
    @DisplayName("GET /api/fruits")
    class GetFruits {
        @Test
        void getFruits_returns200WithListOfRegisteredFruitData() throws Exception {
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

        @Nested
        @DisplayName("GET /api/fruits/{id}")
        class GetFruitById {

        }
            @Test
            void getFruitById_returns200WithFruitData() throws Exception {
                String fruitName = "Kiwi";
                double fruitWeightAmount = 0.3;
                Fruit fruit = Fruit.create(FruitName.of(fruitName), Weight.inKiloGrams(fruitWeightAmount));
                fruitRepository.saveFruit(FRUIT);
                fruitRepository.saveFruit(fruit);
                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(API_URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON));

                result.andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(1))
                        .andExpect(jsonPath("$.name").value(NAME))
                        .andExpect(jsonPath("$.weightInKg").value(WEIGHT));



                ResultActions result2 = mockMvc.perform(MockMvcRequestBuilders.get(API_URL + "/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON));

                result2.andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(2))
                        .andExpect(jsonPath("$.name").value(fruitName))
                        .andExpect(jsonPath("$.weightInKg").value(fruitWeightAmount));
            }

            @Test
            @DisplayName("returns 404 Not Found when there are no registered fruits")
            void getFruitById_returns404NotFound() throws Exception {
                Long id = 103L;

                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(API_URL + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON));

                result.andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.title", Matchers.containsString("Fruit Not Found")))
                        .andExpect(jsonPath("$.detail", Matchers.containsString("no fruits registered")))
                        .andExpect(jsonPath("$.detail", Matchers.containsString(id.toString())));
            }
        }

        @Nested
        @DisplayName("PATCH /api/fruits/{id}")
        class UpdateFruitById {

            UpdateFruitRequestDTO UPDATE_FRUIT_REQUEST_DTO = new UpdateFruitRequestDTO(NAME, WEIGHT, MAGNITUDE);


            private static final String UPDATE_USER_BY_ID_URL = "/api/fruits/{id}";
            @Test
            void updateFruitById_returns200WithFruitDataUpdated() throws Exception {
                String fruitName = "Kiwi";
                double fruitWeightAmount = 0.3;
                Fruit fruit = Fruit.create(FruitName.of(fruitName), Weight.inKiloGrams(fruitWeightAmount));
                fruitRepository.saveFruit(FRUIT);
                fruitRepository.saveFruit(fruit);
                String newName = "Pear";
                double newWeight = 2.45;
                UpdateFruitRequestDTO updateFruitRequestDTO = new UpdateFruitRequestDTO(newName, newWeight, MAGNITUDE);

                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.patch(UPDATE_USER_BY_ID_URL, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateFruitRequestDTO)));
                result.andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(1L))
                        .andExpect(jsonPath("$.name").value(newName))
                        .andExpect(jsonPath("$.weightInKg").value(newWeight));
            }

            @Test
            @DisplayName("returns 404 Not Found when there are no registered fruits")
            void updateFruitById_returns404NotFound() throws Exception {

                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.patch(UPDATE_USER_BY_ID_URL, 901L)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(UPDATE_FRUIT_REQUEST_DTO)));

                result.andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.title", Matchers.containsString("Fruit Not Found")))
                        .andExpect(jsonPath("$.detail", Matchers.containsString("registered")))
                        .andExpect(jsonPath("$.detail", Matchers.containsString("no")));

            }
        }

    @Nested
    @DisplayName("GET /api/fruits/{id}")
    class DeleteFruitById {
        private final String fruitName = "Kiwi";
        private final double fruitWeightAmount = 0.3;
        private final Fruit fruit = Fruit.create(FruitName.of(fruitName), Weight.inKiloGrams(fruitWeightAmount));


        @BeforeEach
        void setUp(){
            fruitRepository.saveFruit(FRUIT);
            fruitRepository.saveFruit(fruit);
        }
        @Test
        void deleteFruitById_returns204() throws Exception {

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete(API_URL + "/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON));

            result.andExpect(status().isNoContent());
        }

        @Test
        void deleteFruitById_returns404NotFound() throws Exception {

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete(API_URL + "/{id}", 909L)
                    .contentType(MediaType.APPLICATION_JSON));

            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.title", Matchers.containsString("Fruit Not Found")))
                    .andExpect(jsonPath("$.detail", Matchers.containsString("registered")))
                    .andExpect(jsonPath("$.detail", Matchers.containsString("no")));
        }
    }
}
