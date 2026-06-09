package cat.itacademy.s04.s02.n01.provider.controller;

import cat.itacademy.s04.s02.n01.common.domain.value_object.Name;
import cat.itacademy.s04.s02.n01.provider.application.usecase.DeleteProviderByIdUseCase;
import cat.itacademy.s04.s02.n01.provider.application.usecase.RegisterProviderUseCase;
import cat.itacademy.s04.s02.n01.provider.application.usecase.UpdateProviderByIdUseCase;
import cat.itacademy.s04.s02.n01.provider.controller.exception.ProviderAlreadyExistsException;
import cat.itacademy.s04.s02.n01.provider.controller.exception.ProviderHasAssociatedFruitsException;
import cat.itacademy.s04.s02.n01.provider.controller.exception.ProviderNotFoundException;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

    @MockitoBean
    private UpdateProviderByIdUseCase updateProviderByIdUseCase;

    @MockitoBean
    private DeleteProviderByIdUseCase deleteProviderByIdUseCase;

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
        void registerProvider_withValidData_returns400BadRequestProviderAlreadyExists() throws Exception {
            CreateProviderRequestDTO createProviderRequestDTO = new CreateProviderRequestDTO(PROVIDER_NAME, COUNTRY);
            String exceptionMessage = String.format("Provider with name %s already exists", PROVIDER_NAME);
            when(registerProviderUseCase.execute(PROVIDER_NAME, COUNTRY)).thenThrow(new ProviderAlreadyExistsException(exceptionMessage));
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

        @Nested
        @DisplayName("PATCH /api/providers/{id}")
        class UpdateProviderById {
            private static final String API_URL_STRING_ID = API_URL_STRING + "/{id}";
            private static final long ID = 1L;

            @Test
            @DisplayName("returns 200 OK with updated provider data when input is valid")
            void updateProviderById_withValidData_returns200WithUpdatedProviderData() throws Exception {

                UpdateProviderRequestDTO updateProviderRequestDTO = new UpdateProviderRequestDTO(PROVIDER_NAME, COUNTRY);

                when(updateProviderByIdUseCase.execute(ID, updateProviderRequestDTO)).thenReturn(PROVIDER);

                ResultActions result = mockMvc.perform(patch(API_URL_STRING_ID, ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProviderRequestDTO)));

                result.andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(ID))
                        .andExpect(jsonPath("$.name").value(PROVIDER_NAME))
                        .andExpect(jsonPath("$.country").value(COUNTRY));

                verify(updateProviderByIdUseCase).execute(ID, updateProviderRequestDTO);
            }

            @Test
            @DisplayName("returns 200 OK with updated provider data when Name is null and country valid")
            void updateProviderById_withNullName_returns200WithUpdatedProviderData() throws Exception {
                UpdateProviderRequestDTO updateProviderRequestDTO = new UpdateProviderRequestDTO(null, COUNTRY);

                when(updateProviderByIdUseCase.execute(ID, updateProviderRequestDTO)).thenReturn(PROVIDER);

                ResultActions result = mockMvc.perform(patch(API_URL_STRING_ID, ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProviderRequestDTO)));

                result.andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(ID))
                        .andExpect(jsonPath("$.name").value(PROVIDER_NAME))
                        .andExpect(jsonPath("$.country").value(COUNTRY));

                verify(updateProviderByIdUseCase).execute(ID, updateProviderRequestDTO);

            }

            @Test
            @DisplayName("returns 200 OK with updated provider data when Name is valid and country null")
            void updateProviderById_withNullMagnitudeAndWeightAmount_returns200WithUpdatedProviderData() throws Exception {
                UpdateProviderRequestDTO updateProviderRequestDTO = new UpdateProviderRequestDTO(PROVIDER_NAME, null);

                when(updateProviderByIdUseCase.execute(ID, updateProviderRequestDTO)).thenReturn(PROVIDER);

                ResultActions result = mockMvc.perform(patch(API_URL_STRING_ID, ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProviderRequestDTO)));

                result.andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(ID))
                        .andExpect(jsonPath("$.name").value(PROVIDER_NAME))
                        .andExpect(jsonPath("$.country").value(COUNTRY));

                verify(updateProviderByIdUseCase).execute(ID, updateProviderRequestDTO);
            }

            @Test
            @DisplayName("returns 400 Bad Request when input data is invalid (name is < 2 size)")
            void updateProviderById_withNameSizeLessThan2_returns400ValidationError() throws Exception {
                String invalidName = "a";
                UpdateProviderRequestDTO updateProviderRequestDTO = new UpdateProviderRequestDTO(invalidName, COUNTRY);

                ResultActions result = mockMvc.perform(patch(API_URL_STRING_ID, ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProviderRequestDTO)));

                result.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.title", Matchers.containsString("Validation Error")))
                        .andExpect(jsonPath("$.errors.name", Matchers.containsString("2")));

                verifyNoInteractions(updateProviderByIdUseCase);
            }

            @Test
            @DisplayName("returns 400 Bad Request when input data is invalid (name is > 100 size)")
            void updateProviderById_withNameSizeGreaterThan100_returns400ValidationError() throws Exception {
                String invalidName = "a".repeat(101);
                UpdateProviderRequestDTO updateProviderRequestDTO = new UpdateProviderRequestDTO(invalidName, COUNTRY);

                ResultActions result = mockMvc.perform(patch(API_URL_STRING_ID, ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProviderRequestDTO)));

                result.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.title", Matchers.containsString("Validation Error")))
                        .andExpect(jsonPath("$.errors.name", Matchers.containsString("100")));

                verifyNoInteractions(updateProviderByIdUseCase);
            }


            @Test
            @DisplayName("returns 400 Bad Request when input data is invalid (country contains numbers)")
            void updateProviderById_whenCountryContainsNumbers_returns400ValidationError() throws Exception {
                UpdateProviderRequestDTO updateProviderRequestDTO = new UpdateProviderRequestDTO(PROVIDER_NAME, "r3");

                ResultActions result = mockMvc.perform(patch(API_URL_STRING_ID, ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProviderRequestDTO)));

                result.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.title", Matchers.containsString("Validation Error")))
                        .andExpect(jsonPath("$.errors.country", Matchers.containsString("2 letters")));

                verifyNoInteractions(updateProviderByIdUseCase);
            }


            @Test
            @DisplayName("returns 400 Bad Request when input data is invalid (country is blank)")
            void updateProviderById_whenMagnitudeIsInvalid_returns400ValidationError() throws Exception {
                String jsonInput = """
                        {
                            "name": "Apple",
                            "country": "  "
                        }
                        """;

                ResultActions result = mockMvc.perform(patch(API_URL_STRING_ID, ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput));

                result.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.title", containsString("Validation Error In Input Data")))
                        .andExpect(jsonPath("$.errors.country", containsString("Country")))
                        .andExpect(jsonPath("$.errors.country", containsString("2 letters")));

                verifyNoInteractions(updateProviderByIdUseCase);
            }

            @Test
            @DisplayName("returns 404 Provider Not Found when the service throws a ProviderNotFoundException")
            void updateProviderById_returns404WhenProviderNotFoundExceptionIsThrown() throws Exception {
                UpdateProviderRequestDTO updateProviderRequestDTO = new UpdateProviderRequestDTO(PROVIDER_NAME, COUNTRY);
                String exceptionMessage = "There are no providers with the id";

                when(updateProviderByIdUseCase.execute(ID, updateProviderRequestDTO)).thenThrow(new ProviderNotFoundException(exceptionMessage));

                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.patch(API_URL_STRING_ID, ID)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateProviderRequestDTO)));

                result.andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.title", Matchers.containsString("Provider Not Found")))
                        .andExpect(jsonPath("$.detail", Matchers.containsString(exceptionMessage)));

                verify(updateProviderByIdUseCase).execute(ID, updateProviderRequestDTO);
            }
        }
        @Nested
        @DisplayName("DELETE /api/fruits/{id}")
        class DeleteFruitById {
            String API_URL_STRING_ID = API_URL_STRING + "/{id}";

            @Test
            void deleteProviderById_returns204() throws Exception {
                ResultActions result = mockMvc.perform(delete(API_URL_STRING_ID, PROVIDER.getId())
                        .contentType(MediaType.APPLICATION_JSON));

                result.andExpect(status().isNoContent());

                verify(deleteProviderByIdUseCase).execute(PROVIDER.getId());
            }

            @Test
            @DisplayName("returns 400 Bad Request when input data is invalid (id is negative)")
            void deleteProviderById_returns400BadRequestIdIsNegative() throws Exception {
                ResultActions result = mockMvc.perform(delete(API_URL_STRING_ID, -1L)
                        .contentType(MediaType.APPLICATION_JSON));

                result.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.title", Matchers.containsString("Validation Error in Parameter")))
                        .andExpect(jsonPath("$.errors['deleteProviderById.id']", Matchers.containsString("ID")))
                        .andExpect(jsonPath("$.errors['deleteProviderById.id']", Matchers.containsString("positive")));

                verifyNoInteractions(deleteProviderByIdUseCase);
            }

            @Test
            @DisplayName("returns 404 Not Found when the service throws a ProviderNotFoundException")
            void deleteProviderById_returns404WhenProviderNotFoundExceptionIsThrown() throws Exception {
                final String exceptionMessage = "There are no providers registered with the id " + ID;
                doThrow(new ProviderNotFoundException(exceptionMessage)).when(deleteProviderByIdUseCase).execute(anyLong());

                ResultActions result = mockMvc.perform(delete(API_URL_STRING_ID, ID)
                        .contentType(MediaType.APPLICATION_JSON));

                result.andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.title", Matchers.containsString("Provider Not Found")))
                        .andExpect(jsonPath("$.detail", Matchers.containsString(exceptionMessage)));

                verify(deleteProviderByIdUseCase).execute(anyLong());
            }

            @Test
            @DisplayName("returns 401 Bad Request Found when the service throws a ProviderHasAssociatedFruitsException")
            void deleteProviderById_returns404WhenProviderHasAssociatedFruitsExceptionIsThrown() throws Exception {
                final String exceptionMessage = String.format("Provider with id %s cannot be deleted because it has associated fruits", ID);
                doThrow(new ProviderHasAssociatedFruitsException(exceptionMessage)).when(deleteProviderByIdUseCase).execute(anyLong());

                ResultActions result = mockMvc.perform(delete(API_URL_STRING_ID, ID)
                        .contentType(MediaType.APPLICATION_JSON));

                result.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.title", Matchers.containsString("Provider In Use")))
                        .andExpect(jsonPath("$.detail", Matchers.containsString(exceptionMessage)));

                verify(deleteProviderByIdUseCase).execute(anyLong());
            }
        }
    }
}