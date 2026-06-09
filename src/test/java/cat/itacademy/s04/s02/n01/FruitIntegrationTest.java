package cat.itacademy.s04.s02.n01;
import cat.itacademy.s04.s02.n01.fruit.application.repository.FruitRepository;
import cat.itacademy.s04.s02.n01.fruit.controller.RegisterFruitRequestDTO;
import cat.itacademy.s04.s02.n01.fruit.controller.UpdateFruitRequestDTO;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import cat.itacademy.s04.s02.n01.common.domain.value_object.Name;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Weight;
import cat.itacademy.s04.s02.n01.provider.application.repository.ProviderRepository;
import cat.itacademy.s04.s02.n01.provider.domain.model.Country;
import cat.itacademy.s04.s02.n01.provider.domain.model.Provider;
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
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = FruitApiH2Application.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class FruitIntegrationTest {

    private static final String PROVIDER_NAME = "Fruit Provider";
    private static final String COUNTRY = "US";
    private static final Long PROVIDER_ID = 1L;
    private static final Provider PROVIDER = new Provider(PROVIDER_ID, Name.of(PROVIDER_NAME), Country.of(COUNTRY));

    private static final String NAME = "Pineapple";
    private static final Double WEIGHT = 4.6;
    private static final String MAGNITUDE = "KILOGRAMS";
    private static final String API_URL = "/api/fruits";
    private static final Fruit FRUIT = Fruit.create(Name.of(NAME), Weight.inKiloGrams(WEIGHT), PROVIDER);


    @Autowired
    private FruitRepository fruitRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Nested
    @DisplayName("PUT /api/fruits")
    class CreateFruit {
        private Long generatedProviderId;

        @BeforeEach
        void setUp(){
            Provider provider = Provider.create(Name.of(PROVIDER_NAME), Country.of(COUNTRY));
            Provider savedProvider = providerRepository.saveProvider(provider);
            this.generatedProviderId = savedProvider.getId();
        }

        @Test
        void createFruit_returns201WithLocationAndBodyFruitWithId() throws Exception {
            RegisterFruitRequestDTO createFruitDTO = new RegisterFruitRequestDTO(NAME, WEIGHT, MAGNITUDE, PROVIDER_ID);

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createFruitDTO)));
            result.andExpect(status().isCreated())
                    .andExpect(header().string("Location", containsString(API_URL + "/")))
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.name").value(NAME))
                    .andExpect(jsonPath("$.weightInKg").value(WEIGHT))
                    .andExpect(jsonPath("$.providerId").value(generatedProviderId));
        }

            @Test
            @DisplayName("returns 404 Not Found when provider does not exist")
            void createFruit_whenProviderDoesNotExist_returns404() throws Exception {

                Long providerId = 909L;
                RegisterFruitRequestDTO registerFruitRequestDTO = new RegisterFruitRequestDTO(NAME, WEIGHT, MAGNITUDE, providerId);
                String exceptionMessage = String.format("Provider with id %s does not exist", providerId);

                ResultActions result = mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerFruitRequestDTO)));

                result.andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.detail", containsString("Provider Not Found")))
                        .andExpect(jsonPath("$.detail", containsString(exceptionMessage)));
        }
    }

    @Nested
    @DisplayName("GET /api/fruits")
    class GetFruits {
        private Long generatedProviderId;

        @BeforeEach
        void setUp(){
            Provider provider = Provider.create(Name.of(PROVIDER_NAME), Country.of(COUNTRY));
            Provider savedProvider = providerRepository.saveProvider(provider);
            this.generatedProviderId = savedProvider.getId();
        }

        @Test
        void getFruits_returns200WithListOfRegisteredFruitData() throws Exception {
            String fruit2Name = "Kiwi";
            double fruit2WeightAmount = 0.3;
            Fruit fruit2 = Fruit.create(Name.of(fruit2Name), Weight.inKiloGrams(fruit2WeightAmount), PROVIDER);
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

        @Nested
        @DisplayName("GET /api/fruits/{id}")
        class GetFruitById {

            @Test
            void getFruitById_returns200WithFruitData() throws Exception {
                String fruitName = "Kiwi";
                double fruitWeightAmount = 0.3;
                Fruit fruit = Fruit.create(Name.of(fruitName), Weight.inKiloGrams(fruitWeightAmount), PROVIDER);
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
    @DisplayName("GET /api/fruits/provider/{providerId}")
    class GetFruitsByProviderId {
        private final String fruitName = "Kiwi";
        private final double fruitWeightAmount = 0.3;
        private final Fruit fruit = Fruit.create(Name.of(fruitName), Weight.inKiloGrams(fruitWeightAmount), PROVIDER);
        private final static String GET_FRUITS_BY_PROVIDER_URL = API_URL + "/provider/{providerId}";
        private Long generatedProviderId;
        private Long generatedProviderId2;


        @BeforeEach
        void setUp(){
            Provider provider = Provider.create(Name.of(PROVIDER_NAME), Country.of(COUNTRY));
            Provider savedProvider = providerRepository.saveProvider(provider);
            generatedProviderId = savedProvider.getId();

            Provider provider2 = Provider.create(Name.of("Provider 2"), Country.of("BV"));
            Provider savedProvider2 = providerRepository.saveProvider(provider);
            generatedProviderId2 = savedProvider2.getId();

            fruitRepository.saveFruit(FRUIT);
            fruitRepository.saveFruit(fruit);
        }

        @Test
        void getFruitsByProviderId_returns200WithFruitData() throws Exception {
            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(GET_FRUITS_BY_PROVIDER_URL, generatedProviderId)
                    .contentType(MediaType.APPLICATION_JSON));

            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id").exists())
                    .andExpect(jsonPath("$[0].name").value(NAME))
                    .andExpect(jsonPath("$[0].weightInKg").value(WEIGHT))
                    .andExpect(jsonPath("$[1].id").exists())
                    .andExpect(jsonPath("$[1].name").value(fruitName))
                    .andExpect(jsonPath("$[1].weightInKg").value(fruitWeightAmount));
        }

        @Test
        void getFruitsByProviderId_returns404ProviderNotFound() throws Exception {
            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(GET_FRUITS_BY_PROVIDER_URL, 909L)
                    .contentType(MediaType.APPLICATION_JSON));

            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.title").value("Provider Not Found"))
                    .andExpect(jsonPath("$.detail", containsString("Provider")))
                    .andExpect(jsonPath("$.detail", containsString("id")));
        }

        @Test
        void getFruitsByProviderId_returns404FruitNotFound() throws Exception {
            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(GET_FRUITS_BY_PROVIDER_URL, generatedProviderId2)
                    .contentType(MediaType.APPLICATION_JSON));

            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.title").value("Fruit Not Found"))
                    .andExpect(jsonPath("$.detail", containsString("no fruits registered")))
                    .andExpect(jsonPath("$.detail", containsString("provider with id")));
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
                Fruit fruit = Fruit.create(Name.of(fruitName), Weight.inKiloGrams(fruitWeightAmount), PROVIDER);
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
        private final Fruit fruit = Fruit.create(Name.of(fruitName), Weight.inKiloGrams(fruitWeightAmount), PROVIDER);


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
