package cat.itacademy.s04.s02.n01.provider.controller;

import cat.itacademy.s04.s02.n01.common.domain.value_object.Name;
import cat.itacademy.s04.s02.n01.provider.application.usecase.RegisterProviderUseCase;
import cat.itacademy.s04.s02.n01.provider.domain.model.Country;
import cat.itacademy.s04.s02.n01.provider.domain.model.Provider;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ProviderRestController.class)
class ProviderRestControllerTest {
    private static final String PROVIDER_NAME = "Fruit Provider";
    private static final String COUNTRY = "US";
    private static final Long ID = 1L;
    private static final String API_URL_STRING = "/api/providers";

    private static final Provider PROVIDER = new Provider(ID, Name.of(PROVIDER_NAME), Country.of(COUNTRY));
    private static final ProviderResponseDTO PROVIDER_RESPONSE = new ProviderResponseDTO(ID, PROVIDER_NAME, COUNTRY);

    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegisterProviderUseCase registerProviderUseCase;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Nested
    @DisplayName("PUT /api/providers")
    class RegisterProvider {

        @Test
        void registerProvider_withValidData_returns201WithLocationAndBodyProviderWithIdAndData() throws Exception {
            CreateProviderRequestDTO createProviderRequestDTO = new CreateProviderRequestDTO(PROVIDER_NAME, COUNTRY);
            when(registerProviderUseCase.execute(PROVIDER_NAME, COUNTRY)).thenReturn(PROVIDER);
            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(API_URL_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createProviderRequestDTO)));
            result.andExpect(status().isCreated())
                    .andExpect(header().string("Location", containsString("/api/providers/" + ID)))
                    .andExpect(jsonPath("$.id").value(ID))
                    .andExpect(jsonPath("$.name").value(PROVIDER_NAME))
                    .andExpect(jsonPath("$.country").value(COUNTRY));
            verify(registerProviderUseCase).execute(PROVIDER_NAME, COUNTRY);
        }

        @Test
        @DisplayName("returns 400 Bad Request when input data is invalid (name is blank)")
        void registerProvider_returns400ValidationErrorInInputDataBlankName() throws Exception {
            String jsonInput =
                    "{\"name\": \"     \", \"country\" : \"AR\"}";


            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(API_URL_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonInput));

            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title", Matchers.containsString("Invalid Request Input Data")))
                    .andExpect(jsonPath("$.detail", Matchers.containsString("Name")))
                    .andExpect(jsonPath("$.detail", Matchers.containsString("blank")));

            verifyNoInteractions(registerProviderUseCase);
        }

        @Test
        @DisplayName("returns 400 Bad Request when input data is invalid (country size is more little 2)")
        void registerProvider_returns400ValidationErrorNameLengthIsLessThan2() throws Exception {
            String jsonInput =
                    "{\"name\": \"a\", \"country\" : \"AR\"}";

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(API_URL_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonInput));

            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title", Matchers.containsString("Validation Error")))
                    .andExpect(jsonPath("$.errors.name", Matchers.containsString("2")));

            verifyNoInteractions(registerProviderUseCase);
        }

        @Test
        @DisplayName("returns 400 Bad Request when input data is invalid (country size is greater than 2)")
        void registerProvider_returns400ValidationErrorInInputDataCountrySizeIsGreaterThanLimit() throws Exception {
            String jsonInput =
                    "{\"name\": \"Good Fruits\", \"country\" : \"R\"}";


            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(API_URL_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonInput));

            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title", Matchers.containsString("Validation Error")))
                    .andExpect(jsonPath("$.errors.country", Matchers.containsString("Country")))
                    .andExpect(jsonPath("$.errors.country", Matchers.containsString("2 letters")));

            verifyNoInteractions(registerProviderUseCase);
        }

        @Test
        @DisplayName("returns 400 Bad Request when input data is invalid (country name has numbers)")
        void registerProvider_returns400ValidationErrorInInputDataCountryNameHasNumbers() throws Exception {
            String jsonInput =
                    "{\"name\": \"Good Fruits\", \"country\" : \"ARG\"}";

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(API_URL_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonInput));

            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title", Matchers.containsString("Validation Error")))
                    .andExpect(jsonPath("$.errors.country", Matchers.containsString("Country")))
                    .andExpect(jsonPath("$.errors.country", Matchers.containsString("letters")));

            verifyNoInteractions(registerProviderUseCase);
        }

        @Test
        @DisplayName("returns 400 Bad Request when input data is invalid (country name is blank)")
        void registerProvider_returns400ValidationErrorInInputDataCountryNameIsBlank() throws Exception {
            String jsonInput =
                    "{\"name\": \"Good Fruits\", \"country\" : \"  \"}";

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(API_URL_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonInput));

            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title", Matchers.containsString("Validation Error")))
                    .andExpect(jsonPath("$.errors.country", Matchers.containsString("Country")))
                    .andExpect(jsonPath("$.errors.country", Matchers.containsString("letters")));

            verifyNoInteractions(registerProviderUseCase);
        }

    }
}