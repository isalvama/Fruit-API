package cat.itacademy.s04.s02.n01;
import cat.itacademy.s04.s02.n01.fruit.application.repository.FruitRepository;
import cat.itacademy.s04.s02.n01.fruit.controller.dto.RegisterFruitRequestDTO;
import cat.itacademy.s04.s02.n01.fruit.controller.dto.UpdateFruitRequestDTO;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import cat.itacademy.s04.s02.n01.common.domain.value_object.Name;
import cat.itacademy.s04.s02.n01.fruit.domain.value_object.Weight;
import cat.itacademy.s04.s02.n01.provider.application.repository.ProviderRepository;
import cat.itacademy.s04.s02.n01.provider.controller.dto.CreateProviderRequestDTO;
import cat.itacademy.s04.s02.n01.provider.controller.dto.UpdateProviderRequestDTO;
import cat.itacademy.s04.s02.n01.provider.domain.value_object.Country;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = FruitApiH2Application.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class FruitIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FruitRepository fruitRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String API_FRUITS = "/api/fruits";
    private static final String API_PROVIDER = "/api/providers";
    private static final String DEFAULT_P_NAME = "Fruit Provider";
    private static final String DEFAULT_P_COUNTRY = "ES";
    private static final Long DEFAULT_P_ID = 1L;
    private static final Provider DEFAULT_PROVIDER = new Provider(DEFAULT_P_ID, Name.of(DEFAULT_P_NAME), Country.of(DEFAULT_P_COUNTRY));

    private static final String DEFAULT_F_NAME = "Pineapple";
    private static final Double DEFAULT_F_WEIGHT = 4.6;
    private static final String DEFAULT_MAGNITUDE = "KILOGRAMS";
    private static final Fruit DEFAULT_FRUIT = Fruit.create(Name.of(DEFAULT_F_NAME), Weight.inKiloGrams(DEFAULT_F_WEIGHT), DEFAULT_PROVIDER);

    private Provider createAndSaveProvider(String name, String country) {
        return providerRepository.saveProvider(Provider.create(Name.of(name), Country.of(country)));
    }

    private Fruit createAndSaveFruit(String name, Double weight, Provider provider) {
        return fruitRepository.saveFruit(Fruit.create(Name.of(name), Weight.inKiloGrams(weight), provider));
    }

    // =========================================================================
    // FRUIT ENDPOINTS
    // =========================================================================

    @Nested
    @DisplayName("POST " + API_FRUITS)
    class CreateFruit {
        private Long generatedProviderId;

        @BeforeEach
        void setUp() {
            this.generatedProviderId = createAndSaveProvider(DEFAULT_P_NAME, DEFAULT_P_COUNTRY).getId();
        }

        @DisplayName("should return 201 when fruit data is valid")
        @Test
        void shouldRegisterFruit() throws Exception {
            RegisterFruitRequestDTO createFruitDTO = new RegisterFruitRequestDTO(DEFAULT_F_NAME, DEFAULT_F_WEIGHT, DEFAULT_MAGNITUDE, generatedProviderId);

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(API_FRUITS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createFruitDTO)));

            result.andExpect(status().isCreated())
                    .andExpect(header().string("Location", containsString(API_FRUITS + "/")))
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.name").value(DEFAULT_F_NAME))
                    .andExpect(jsonPath("$.weightInKg").value(DEFAULT_F_WEIGHT))
                    .andExpect(jsonPath("$.providerId").value(generatedProviderId));
        }

        @Test
        @DisplayName("returns 404 Not Found when provider id does not exist")
        void shouldReturn404WhenProviderNotFound() throws Exception {
            Long providerId = 909L;
            RegisterFruitRequestDTO registerFruitRequestDTO = new RegisterFruitRequestDTO(DEFAULT_F_NAME, DEFAULT_F_WEIGHT, DEFAULT_MAGNITUDE, providerId);

            ResultActions result = mockMvc.perform(post(API_FRUITS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerFruitRequestDTO)));

            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail", containsString("Provider Not Found")))
                    .andExpect(jsonPath("$.detail", containsString("Provider")))
                    .andExpect(jsonPath("$.detail", containsString(providerId.toString())))
                    .andExpect(jsonPath("$.detail", containsString("not exist")));
        }
    }

    @Nested
    @DisplayName("GET " + API_FRUITS)
    class GetFruits {
        private Long generatedProviderId;

        @Test
        @DisplayName("should return list of fruits when they exist")
        void shouldReturnFruitsList() throws Exception {
            Provider p = createAndSaveProvider(DEFAULT_P_NAME, DEFAULT_P_COUNTRY);
            String fruit1Name = "Apple";
            String fruit2Name = "Banana";
            Double fruit1Weight = 1.0;
            Double fruit2Weight = 1.0;

            createAndSaveFruit(fruit1Name, fruit1Weight, p);
            createAndSaveFruit(fruit2Name, fruit2Weight, p);

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(API_FRUITS)
                    .contentType(MediaType.APPLICATION_JSON));

            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id").exists())
                    .andExpect(jsonPath("$[0].name").value(fruit1Name))
                    .andExpect(jsonPath("$[0].weightInKg").value(fruit1Weight))

                    .andExpect(jsonPath("$[1].id").exists())
                    .andExpect(jsonPath("$[1].name").value(fruit2Name))
                    .andExpect(jsonPath("$[1].weightInKg").value(fruit2Weight));
        }

        @Test
        @DisplayName("should return 404 Not Found when there are no registered fruits")
        void shouldReturn404WhenFruitsNotFound() throws Exception {

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(API_FRUITS)
                    .contentType(MediaType.APPLICATION_JSON));

            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.title", Matchers.containsString("Fruit Not Found")))
                    .andExpect(jsonPath("$.detail", Matchers.containsString("no registered fruits")));
        }
    }

    @Nested
    @DisplayName("GET " + API_FRUITS + "{id}")
    class GetFruitById {

        @Test
        @DisplayName("should return 200 with fruit data when fruit exists")
        void shouldReturn200WithFruitData() throws Exception {
            String fruitName = "Kiwi";
            double fruitWeightAmount = 0.3;
            createAndSaveFruit(DEFAULT_F_NAME, DEFAULT_F_WEIGHT, DEFAULT_PROVIDER);
            createAndSaveFruit(fruitName, fruitWeightAmount, DEFAULT_PROVIDER);

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(API_FRUITS + "/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON));

            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value(DEFAULT_F_NAME))
                    .andExpect(jsonPath("$.weightInKg").value(DEFAULT_F_WEIGHT));


            ResultActions result2 = mockMvc.perform(MockMvcRequestBuilders.get(API_FRUITS + "/{id}", 2)
                    .contentType(MediaType.APPLICATION_JSON));

            result2.andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(2))
                    .andExpect(jsonPath("$.name").value(fruitName))
                    .andExpect(jsonPath("$.weightInKg").value(fruitWeightAmount));
        }

        @Test
        @DisplayName("returns 404 Not Found when there are no registered fruits")
        void shouldReturn404WhenFruitNotFound() throws Exception {
            Long id = 103L;

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(API_FRUITS + "/{id}", 909L)
                    .contentType(MediaType.APPLICATION_JSON));

            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.title", Matchers.containsString("Fruit Not Found")))
                    .andExpect(jsonPath("$.detail", Matchers.containsString("no fruits registered")))
                    .andExpect(jsonPath("$.detail", Matchers.containsString(id.toString())));
        }
    }

    @Nested
    @DisplayName("GET " + API_FRUITS + "/provider/{providerId}")
    class GetFruitsByProviderId {
        String GET_FRUITS_BY_PROVIDER_API = API_FRUITS + "/provider/{providerId}";


        @Test
        @DisplayName("should return fruits for a specific provider")
        void shouldReturnFruitsByProvider() throws Exception {
            Provider p1 = createAndSaveProvider("P1", "ES");
            createAndSaveFruit("Apple", 1.0, p1);
            createAndSaveFruit("Pear", 1.0, p1);

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(GET_FRUITS_BY_PROVIDER_API, p1.getId())
                    .contentType(MediaType.APPLICATION_JSON));

            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id").exists())
                    .andExpect(jsonPath("$[0].name").value("Apple"))
                    .andExpect(jsonPath("$[1].id").exists())
                    .andExpect(jsonPath("$[1].name").value("Pear"));
        }

        @Test
        @DisplayName("should return 404 Provider Not Found when the provider with the id does not exist")
        void shouldReturn404ProviderNotFound() throws Exception {
            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(GET_FRUITS_BY_PROVIDER_API, 909L)
                    .contentType(MediaType.APPLICATION_JSON));

            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.title").value("Provider Not Found"))
                    .andExpect(jsonPath("$.detail", containsString("Provider")))
                    .andExpect(jsonPath("$.detail", containsString("id")));
        }

        @Test
        @DisplayName("should return 404 Provider Not Found when the provider id does not have any associated fruit")
        void shouldReturn404FruitFruitNotFound() throws Exception {
            Provider p2 = createAndSaveProvider("P2", "FR");

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(GET_FRUITS_BY_PROVIDER_API, p2.getId())
                    .contentType(MediaType.APPLICATION_JSON));

            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.title").value("Fruit Not Found"))
                    .andExpect(jsonPath("$.detail", containsString("no fruits registered")))
                    .andExpect(jsonPath("$.detail", containsString("provider with id")));
        }
    }

    @Nested
    @DisplayName("PATCH " + API_FRUITS + "/{id}")
    class UpdateFruitById {

        private static final String UPDATE_USER_BY_ID_API = API_FRUITS + "/{id}";

        @Test
        @DisplayName("should return 200 when the update worked successfully with the updated data of the fruit")
        void shouldReturn200WithFruitDataUpdated() throws Exception {
            Fruit fruit1 = createAndSaveFruit(DEFAULT_F_NAME, DEFAULT_F_WEIGHT, DEFAULT_PROVIDER);

            String newName = "Pear";
            double newWeight = 2.45;
            UpdateFruitRequestDTO updateFruitRequestDTO = new UpdateFruitRequestDTO(newName, newWeight, DEFAULT_MAGNITUDE);

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.patch(UPDATE_USER_BY_ID_API, fruit1.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateFruitRequestDTO)));
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.name").value(newName))
                    .andExpect(jsonPath("$.weightInKg").value(newWeight));
        }

        @Test
        @DisplayName("should return 404 Fruit Not Found when there are no registered fruits")
        void shouldReturn404FruitNotFound() throws Exception {
            UpdateFruitRequestDTO updateFruitRequestDTO = new UpdateFruitRequestDTO(DEFAULT_F_NAME, DEFAULT_F_WEIGHT, DEFAULT_MAGNITUDE);

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.patch(UPDATE_USER_BY_ID_API, 901L)
                    .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateFruitRequestDTO)));

            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.title", Matchers.containsString("Fruit Not Found")))
                    .andExpect(jsonPath("$.detail", Matchers.containsString("registered")))
                    .andExpect(jsonPath("$.detail", Matchers.containsString("no")));

        }
    }

    @Nested
    @DisplayName("GET " + API_FRUITS + "/{id}")
    class DeleteFruitById {

        @Test
        @DisplayName("should return 204 No Content when fruit was deleted successfully")
        void shouldReturn204NoContent() throws Exception {
            createAndSaveFruit(DEFAULT_F_NAME, DEFAULT_F_WEIGHT, DEFAULT_PROVIDER);

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete(API_FRUITS + "/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON));

            result.andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("should return 404 Fruit Not Found when the fruit with id does not exist")
        void shouldReturn404FruitNotFound() throws Exception {

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete(API_FRUITS + "/{id}", 909L)
                    .contentType(MediaType.APPLICATION_JSON));

            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.title", Matchers.containsString("Fruit Not Found")))
                    .andExpect(jsonPath("$.detail", Matchers.containsString("registered")))
                    .andExpect(jsonPath("$.detail", Matchers.containsString("no")));
        }
    }


    // =========================================================================
    // PROVIDER ENDPOINTS
    // =========================================================================

    @Nested
    @DisplayName("PATCH " + API_PROVIDER)
    class RegisterProvider {

        @Test
        @DisplayName("should return 201 with location and body of the provider when the provider was registered successfully")
        void shouldReturn201WithLocationAndBodyWithProviderData() throws Exception {
            String providerName = "Fruit King";
            CreateProviderRequestDTO createProviderRequestDTO = new CreateProviderRequestDTO(providerName, DEFAULT_P_COUNTRY);

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(API_PROVIDER)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createProviderRequestDTO)));

            result.andExpect(status().isCreated())
                    .andExpect(header().string("Location", containsString("/api/providers/")))
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.name").value(providerName))
                    .andExpect(jsonPath("$.country").value(DEFAULT_P_COUNTRY));
        }

        @Test
        @DisplayName("should return 400 Bad Request when a provider with a matching name already exists")
        void shouldReturn400BadRequestProviderAlreadyExists() throws Exception {
            createAndSaveProvider(DEFAULT_P_NAME, DEFAULT_P_COUNTRY);

            CreateProviderRequestDTO createProviderRequestDTO = new CreateProviderRequestDTO(DEFAULT_P_NAME, DEFAULT_P_COUNTRY);

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(API_PROVIDER)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createProviderRequestDTO)));

            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title").value("Provider Already Exists"))
                    .andExpect(jsonPath("$.detail", Matchers.containsString(DEFAULT_P_NAME)))
                    .andExpect(jsonPath("$.detail", Matchers.containsString("exists")));
        }

        @Test
        @DisplayName("returns 400 Bad Request Invalid Request Input Data when input data is invalid (name is blank)")
        void shouldReturn400BadRequest() throws Exception {
            String jsonInput =
                    "{\"name\": \"     \", \"country\" : \"AR\"}";

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(API_PROVIDER, DEFAULT_P_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonInput));

            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title", Matchers.containsString("Invalid Request Input Data")))
                    .andExpect(jsonPath("$.detail", Matchers.containsString("Name")))
                    .andExpect(jsonPath("$.detail", Matchers.containsString("blank")));
        }

        @Test
        @DisplayName("returns 400 Bad Request Invalid Request Input Data when input data is invalid (country name has numbers)")
        void shouldReturn400ValidationErrorInInputData() throws Exception {
            String jsonInput =
                    "{\"name\": \"Good Fruits\", \"country\" : \"ARG\"}";

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(API_PROVIDER)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonInput));

            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title", Matchers.containsString("Validation Error")))
                    .andExpect(jsonPath("$.errors.country", Matchers.containsString("Country")))
                    .andExpect(jsonPath("$.errors.country", Matchers.containsString("letters")));
        }
    }

    @Nested
    @DisplayName("PATCH " + API_PROVIDER + "/{id}")
    class UpdateProviderById {
        private static final String API_URL_STRING_ID = API_PROVIDER + "/{id}";
        Provider provider;

        @BeforeEach
        void setUp(){
            provider = createAndSaveProvider(DEFAULT_P_NAME, DEFAULT_P_COUNTRY);
        }

        @Test
        @DisplayName("returns 200 OK with updated provider data when input is valid")
        void shouldReturn200WithUpdatedProviderDataNewNameAndCountry() throws Exception {
            final String newProviderName = "New Provider";
            final String newCountry = "SW";

            UpdateProviderRequestDTO updateProviderRequestDTO = new UpdateProviderRequestDTO(newProviderName, newCountry);

            ResultActions result = mockMvc.perform(patch(API_URL_STRING_ID, provider.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateProviderRequestDTO)));

            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(provider.getId()))
                    .andExpect(jsonPath("$.name").value(newProviderName))
                    .andExpect(jsonPath("$.country").value(newCountry));
        }

        @Test
        @DisplayName("returns 200 OK with updated provider data when name is null and country valid")
        void shouldReturn200WithUpdatedProviderDataNewCountry() throws Exception {
            final String newCountry = "SW";

            UpdateProviderRequestDTO updateProviderRequestDTO = new UpdateProviderRequestDTO(null, newCountry);

            ResultActions result = mockMvc.perform(patch(API_URL_STRING_ID, provider.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateProviderRequestDTO)));

            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(provider.getId()))
                    .andExpect(jsonPath("$.name").value(DEFAULT_P_NAME))
                    .andExpect(jsonPath("$.country").value(newCountry));
        }

        @Test
        @DisplayName("returns 200 OK with updated provider data when name is valid and country null")
        void shouldReturn200WithUpdatedProviderDataNewName() throws Exception {
            final String newProviderName = "New Provider";

            UpdateProviderRequestDTO updateProviderRequestDTO = new UpdateProviderRequestDTO(newProviderName, null);

            ResultActions result = mockMvc.perform(patch(API_URL_STRING_ID, provider.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateProviderRequestDTO)));

            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(provider.getId()))
                    .andExpect(jsonPath("$.name").value(newProviderName))
                    .andExpect(jsonPath("$.country").value(DEFAULT_P_COUNTRY));
        }

        @Test
        @DisplayName("returns 400 Bad Request when input data is invalid (name is < 2 size)")
        void returns400ValidationErrorInvalidName() throws Exception {
            String invalidName = "a";
            UpdateProviderRequestDTO updateProviderRequestDTO = new UpdateProviderRequestDTO(invalidName, DEFAULT_P_COUNTRY);

            ResultActions result = mockMvc.perform(patch(API_URL_STRING_ID, provider.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateProviderRequestDTO)));

            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title", Matchers.containsString("Validation Error")))
                    .andExpect(jsonPath("$.errors.name", Matchers.containsString("2")));
        }


        @Test
        @DisplayName("returns 400 Bad Request when input data is invalid (country contains numbers)")
        void returns400ValidationErrorInvalidCountry() throws Exception {
            UpdateProviderRequestDTO updateProviderRequestDTO = new UpdateProviderRequestDTO(DEFAULT_P_NAME, "r3");

            ResultActions result = mockMvc.perform(patch(API_URL_STRING_ID, provider.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateProviderRequestDTO)));

            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title", Matchers.containsString("Validation Error")))
                    .andExpect(jsonPath("$.errors.country", Matchers.containsString("2 letters")));
        }

        @Test
        @DisplayName("returns 404 Provider Not Found when the provider with the specified id does not exist")
        void shouldReturn404ProviderNotFound() throws Exception {
            UpdateProviderRequestDTO updateProviderRequestDTO = new UpdateProviderRequestDTO(DEFAULT_P_NAME, DEFAULT_P_COUNTRY);

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.patch(API_URL_STRING_ID, 909L)
                    .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateProviderRequestDTO)));

            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.title", Matchers.containsString("Provider Not Found")))
                    .andExpect(jsonPath("$.detail", Matchers.containsString("provider")))
                    .andExpect(jsonPath("$.detail", Matchers.containsString("found")));
        }
    }

    @Nested
    @DisplayName("DELETE " + API_PROVIDER + "/{id}")
    class DeleteProviderById {
        private String API_PROVIDER_ID = API_PROVIDER + "/{id}";
        private Provider pWithFruits;
        private Provider pWithoutFruits;
        Fruit fruit;


        @BeforeEach
        void setUp(){
             pWithFruits = createAndSaveProvider("Provider With Fruits", "ES");
             pWithoutFruits = createAndSaveProvider("Provider Without Fruits", "FR");Provider.create(Name.of("Provider Without Fruits"), Country.of("FR"));
             fruit = createAndSaveFruit("Apple", 0.5, pWithFruits);
        }

        @Test
        @DisplayName("returns 204 No Content when provider with specified id exists and hos no associated fruits")
        void shouldReturn204NoContent() throws Exception {
            ResultActions result = mockMvc.perform(delete(API_PROVIDER_ID, pWithoutFruits.getId())
                    .contentType(MediaType.APPLICATION_JSON));

            result.andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("returns 400 Bad Request when input data is invalid (id is negative)")
        void shouldReturn400BadRequestIdIsNegative() throws Exception {
            ResultActions result = mockMvc.perform(delete(API_PROVIDER_ID, -1L)
                    .contentType(MediaType.APPLICATION_JSON));

            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title", Matchers.containsString("Validation Error in Parameter")))
                    .andExpect(jsonPath("$.errors['deleteProviderById.id']", Matchers.containsString("ID")))
                    .andExpect(jsonPath("$.errors['deleteProviderById.id']", Matchers.containsString("positive")));
        }

        @Test
        @DisplayName("returns 404 when the provider with the specified id does not exist")
        void shouldReturn404ProviderNotFound() throws Exception {
            Long id = 909L;
            ResultActions result = mockMvc.perform(delete(API_PROVIDER_ID, id)
                    .contentType(MediaType.APPLICATION_JSON));

            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.title", Matchers.containsString("Provider Not Found")))
                    .andExpect(jsonPath("$.detail", Matchers.containsString("no providers")))
                    .andExpect(jsonPath("$.detail", Matchers.containsString(id.toString())));
        }

        @Test
        @DisplayName("returns 401 Bad Request Provider In Use when the provider has associated fruits")
        void shouldReturn404ProviderInUse() throws Exception {
            ResultActions result = mockMvc.perform(delete(API_PROVIDER_ID, pWithFruits.getId())
                    .contentType(MediaType.APPLICATION_JSON));

            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title", Matchers.containsString("Provider In Use")))
                    .andExpect(jsonPath("$.detail", Matchers.containsString("associated fruits")));
        }
    }
}
