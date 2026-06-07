package cat.itacademy.s04.s02.n01.fruit.controller;

import cat.itacademy.s04.s02.n01.fruit.application.service.CreateFruitService;
import cat.itacademy.s04.s02.n01.fruit.application.service.GetAllFruitsService;
import cat.itacademy.s04.s02.n01.fruit.application.service.GetFruitByIdService;
import cat.itacademy.s04.s02.n01.fruit.application.usecases.DeleteFruitByIdUseCase;
import cat.itacademy.s04.s02.n01.fruit.application.usecases.UpdateFruitByIdUseCase;
import cat.itacademy.s04.s02.n01.fruit.controller.exception.FruitNotFoundException;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import cat.itacademy.s04.s02.n01.fruit.domain.model.FruitName;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Weight;
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


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(FruitRestController.class)
class FruitRestControllerTest {

    private static final String NAME_OF_FRUIT = "Apple";
    private static final double WEIGHT_AMOUNT = 0.5;
    private static final String KG_MAGNITUDE = "KILOGRAMS";
    private static final String POUNDS_MAGNITUDE = "POUNDS";
    private static final Long ID = 1L;
    private static final String API_URL_STRING = "/api/fruits";

    private static final Fruit FRUIT = new Fruit(1L, FruitName.of(NAME_OF_FRUIT), Weight.inKiloGrams(WEIGHT_AMOUNT));
    private static final FruitResponseDTO FRUIT_RESPONSE = new FruitResponseDTO(1L, NAME_OF_FRUIT, WEIGHT_AMOUNT);

    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateFruitService createFruitUseCase;

    @MockitoBean
    private GetAllFruitsService getAllFruitsUseCase;

    @MockitoBean
    private GetFruitByIdService getFruitByIdUseCase;

    @MockitoBean
    private UpdateFruitByIdUseCase updateFruitByIdUseCase;

    @MockitoBean
    private DeleteFruitByIdUseCase deleteFruitByIdUseCase;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Nested
    @DisplayName("PUT /api/fruits")
    class CreateFruit {

        @Test
        void createFruit_withKgMagnitude_returns201WithLocationAndBodyFruitWithId() throws Exception {
            CreateFruitRequestDTO createFruitDTO = new CreateFruitRequestDTO(NAME_OF_FRUIT, WEIGHT_AMOUNT, KG_MAGNITUDE);
            when(createFruitUseCase.createFruit(NAME_OF_FRUIT, WEIGHT_AMOUNT, KG_MAGNITUDE)).thenReturn(FRUIT);
            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(API_URL_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createFruitDTO)));
            result.andExpect(status().isCreated())
                    .andExpect(header().string("Location", containsString("/api/fruits/" + ID)))
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value(NAME_OF_FRUIT))
                    .andExpect(jsonPath("$.weightInKg").value(WEIGHT_AMOUNT));
            verify(createFruitUseCase).createFruit(NAME_OF_FRUIT, WEIGHT_AMOUNT, KG_MAGNITUDE);
        }

        @Test
        @DisplayName("returns 400 Bad Request when input data is invalid (name is blank)")
        void createFruit_returns400ValidationErrorInInputDataBlankName() throws Exception {
            String jsonInput =
                    "{\"name\": \"     \", \"weightAmount\" : 0.5, \"magnitude\": \"POUNDS\"}";


            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(API_URL_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonInput));

            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title", Matchers.containsString("Invalid Request Input Data")))
                    .andExpect(jsonPath("$.detail", Matchers.containsString("Name")))
                    .andExpect(jsonPath("$.detail", Matchers.containsString("blank")));

            verifyNoInteractions(createFruitUseCase);
        }

        @Test
        @DisplayName("returns 400 Bad Request when input data is invalid (name length is < 2)")
        void createFruit_returns400ValidationErrorNameLengthIsLessThan2() throws Exception {
            String jsonInput =
                    "{\"name\": \"a\", \"weightAmount\" : 0.5, \"magnitude\": \"POUNDS\"}";


            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(API_URL_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonInput));

            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title", Matchers.containsString("Validation Error")))
                    .andExpect(jsonPath("$.errors.name", Matchers.containsString("2")));

            verifyNoInteractions(createFruitUseCase);
        }

        @Test
        @DisplayName("returns 400 Bad Request when input data is invalid (weightAmount is negative)")
        void createFruit_returns400ValidationErrorInInputDataWeightAmountIsNegative() throws Exception {
            String jsonInput =
                    "{\"name\": \"Pear\", \"weightAmount\" : -0.5, \"magnitude\": \"KILOGRAMS\"}";


            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(API_URL_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonInput));

            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title", Matchers.containsString("Validation Error")))
                    .andExpect(jsonPath("$.errors.weightAmount", Matchers.containsString("Weight Amount")))
                    .andExpect(jsonPath("$.errors.weightAmount", Matchers.containsString("negative")));

            verifyNoInteractions(createFruitUseCase);
        }

        @Test
        @DisplayName("returns 400 Bad Request when input data is invalid (invalid magnitude)")
        void createFruit_returns400ValidationErrorInInputDataInvalidMagnitude() throws Exception {
            String jsonInput =
                    "{\"name\": \"orange\", \"weightAmount\" : 0.5, \"magnitude\": \"INVALID_MAGNITUDE\"}";

            ResultActions result1 = mockMvc.perform(MockMvcRequestBuilders.post(API_URL_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonInput));

            result1.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title", containsString("Invalid Request")))
                    .andExpect(jsonPath("$.detail", containsString("Magnitude")))
                    .andExpect(jsonPath("$.detail", containsString("not valid")));

            verifyNoInteractions(createFruitUseCase);
        }

        @Nested
        @DisplayName("GET /api/fruits")
        class GetFruits {

            @Test
            void getFruits_returns201WithFruitData() throws Exception {
                when(getAllFruitsUseCase.execute()).thenReturn(List.of(FRUIT));

                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(API_URL_STRING)
                        .contentType(MediaType.APPLICATION_JSON));

                result.andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].id").value(1))
                        .andExpect(jsonPath("$[0].name").value(NAME_OF_FRUIT))
                        .andExpect(jsonPath("$[0].weightInKg").value(WEIGHT_AMOUNT));

                verify(getAllFruitsUseCase).execute();
            }

            @Test
            @DisplayName("returns 404 Not Found when the service throws a FruitNotFoundException")
            void getFruit_returns404WhenFruitNotFoundExceptionIsThrown() throws Exception {
                String exceptionMessage = "There are no registered fruits.";
                when(getAllFruitsUseCase.execute()).thenThrow(new FruitNotFoundException(exceptionMessage));

                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(API_URL_STRING)
                        .contentType(MediaType.APPLICATION_JSON));

                result.andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.title", Matchers.containsString("Fruit Not Found")))
                        .andExpect(jsonPath("$.detail", Matchers.containsString(exceptionMessage)));

                verify(getAllFruitsUseCase).execute();
            }
        }

        @Nested
        @DisplayName("GET /api/fruits/{id}")
        class GetFruitById {
            String API_URL_STRING_ID = API_URL_STRING + "/{id}";

            @Test
            void getFruitById_returns200WithFruitData() throws Exception {
                when(getFruitByIdUseCase.execute(FRUIT.getId())).thenReturn(FRUIT);

                ResultActions result = mockMvc.perform(get(API_URL_STRING_ID, FRUIT.getId())
                        .contentType(MediaType.APPLICATION_JSON));

                result.andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(FRUIT.getId()))
                        .andExpect(jsonPath("$.name").value(NAME_OF_FRUIT))
                        .andExpect(jsonPath("$.weightInKg").value(WEIGHT_AMOUNT));

                verify(getFruitByIdUseCase).execute(FRUIT.getId());
            }

            @Test
            void getFruitById_returns404FruitNotFoundWithExceptionDetails() throws Exception {
                String exceptionMessage = "There are no fruits registered with the id";
                when(getFruitByIdUseCase.execute(FRUIT.getId())).thenThrow(new FruitNotFoundException(exceptionMessage + FRUIT.getId()));
                ResultActions result = mockMvc.perform(get(API_URL_STRING_ID, FRUIT.getId())
                        .contentType(MediaType.APPLICATION_JSON));

                result.andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.title", containsString("Fruit Not Found")))
                        .andExpect(jsonPath("$.detail", containsString("no fruits registered")))
                        .andExpect(jsonPath("$.detail", containsString(String.valueOf(FRUIT.getId()))));

                verify(getFruitByIdUseCase).execute(FRUIT.getId());
            }

            @Test
            void getFruitById_returns400BadRequestWhenIdIsNegative() throws Exception {
                ResultActions result = mockMvc.perform(get(API_URL_STRING_ID, -1)
                        .contentType(MediaType.APPLICATION_JSON));

                result.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.title", containsString("Validation Error")))
                        .andExpect(jsonPath("$.errors").exists());

                verifyNoInteractions(getFruitByIdUseCase);
            }
        }

        @Nested
        @DisplayName("PATCH /api/fruits/{id}")
        class UpdateFruitById {
            private static final String API_URL_STRING_ID = API_URL_STRING + "/{id}";
            private static final long ID = 1L;

            @Test
            @DisplayName("returns 200 OK with updated fruit data when input is valid")
            void updateFruitById_withValidData_returns201WithUpdatedFruitData() throws Exception {

                UpdateFruitRequestDTO updateFruitRequestDTO = new UpdateFruitRequestDTO(NAME_OF_FRUIT, WEIGHT_AMOUNT, KG_MAGNITUDE);

                when(updateFruitByIdUseCase.execute(ID, updateFruitRequestDTO)).thenReturn(FRUIT);

                ResultActions result = mockMvc.perform(patch(API_URL_STRING_ID, ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateFruitRequestDTO)));

                result.andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(ID))
                        .andExpect(jsonPath("$.name").value(NAME_OF_FRUIT))
                        .andExpect(jsonPath("$.weightInKg").value(WEIGHT_AMOUNT));

                verify(updateFruitByIdUseCase).execute(ID, updateFruitRequestDTO);
            }

            @Test
            @DisplayName("returns 200 OK with updated fruit data when Name is null and weightAmount and magnitude are " +
                    "valid")
            void updateFruitById_withNullName_returns200WithUpdatedFruitData() throws Exception {
                UpdateFruitRequestDTO updateFruitRequestDTO = new UpdateFruitRequestDTO(null, WEIGHT_AMOUNT, KG_MAGNITUDE);


                when(updateFruitByIdUseCase.execute(ID, updateFruitRequestDTO)).thenReturn(FRUIT);

                ResultActions result = mockMvc.perform(patch(API_URL_STRING_ID, ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateFruitRequestDTO)));

                result.andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(ID))
                        .andExpect(jsonPath("$.name").value(NAME_OF_FRUIT))
                        .andExpect(jsonPath("$.weightInKg").value(WEIGHT_AMOUNT));

                verify(updateFruitByIdUseCase).execute(ID, updateFruitRequestDTO);

            }

            @Test
            @DisplayName("returns 200 OK with updated fruit data when Name is valid and weightAmount and magnitude null")
            void updateFruitById_withNullMagnitudeAndWeightAmount_returns200WithUpdatedFruitData() throws Exception {
                UpdateFruitRequestDTO updateFruitRequestDTO = new UpdateFruitRequestDTO(NAME_OF_FRUIT, null, null);

                when(updateFruitByIdUseCase.execute(ID, updateFruitRequestDTO)).thenReturn(FRUIT);

                ResultActions result = mockMvc.perform(patch(API_URL_STRING_ID, ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateFruitRequestDTO)));

                result.andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(ID))
                        .andExpect(jsonPath("$.name").value(NAME_OF_FRUIT))
                        .andExpect(jsonPath("$.weightInKg").value(WEIGHT_AMOUNT));

                verify(updateFruitByIdUseCase).execute(ID, updateFruitRequestDTO);
            }

            @Test
            @DisplayName("returns 400 Bad Request when input data is invalid (name is < 2 size)")
            void updateFruitById_withNameSizeLessThan2_returns400ValidationError() throws Exception {
                String invalidName = "a";
                UpdateFruitRequestDTO updateFruitRequestDTO = new UpdateFruitRequestDTO(invalidName, WEIGHT_AMOUNT, KG_MAGNITUDE);

                ResultActions result = mockMvc.perform(patch(API_URL_STRING_ID, ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateFruitRequestDTO)));

                result.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.title", Matchers.containsString("Validation Error")))
                        .andExpect(jsonPath("$.errors.name", Matchers.containsString("2")));

                verifyNoInteractions(updateFruitByIdUseCase);
            }

            @Test
            @DisplayName("returns 400 Bad Request when input data is invalid (name is > 100 size)")
            void updateFruitById_withNameSizeGreaterThan100_returns400ValidationError() throws Exception {
                String invalidName = "a".repeat(101);
                UpdateFruitRequestDTO updateFruitRequestDTO = new UpdateFruitRequestDTO(invalidName, WEIGHT_AMOUNT, KG_MAGNITUDE);

                ResultActions result = mockMvc.perform(patch(API_URL_STRING_ID, ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateFruitRequestDTO)));

                result.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.title", Matchers.containsString("Validation Error")))
                        .andExpect(jsonPath("$.errors.name", Matchers.containsString("100")));

                verifyNoInteractions(updateFruitByIdUseCase);
            }


            @Test
            @DisplayName("returns 400 Bad Request when input data is invalid (weightAmount is negative)")
            void updateFruitById_whenWeightAmountIsNegative_returns400ValidationError() throws Exception {
                UpdateFruitRequestDTO updateFruitRequestDTO = new UpdateFruitRequestDTO(NAME_OF_FRUIT, -1.0, KG_MAGNITUDE);

                ResultActions result = mockMvc.perform(patch(API_URL_STRING_ID, ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateFruitRequestDTO)));

                result.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.title", Matchers.containsString("Validation Error")))
                        .andExpect(jsonPath("$.errors.weightAmount", Matchers.containsString("negative")));

                verifyNoInteractions(updateFruitByIdUseCase);
            }


            @Test
            @DisplayName("returns 400 Bad Request when input data is invalid (invalid magnitude)")
            void updateFruitById_whenMagnitudeIsInvalid_returns400ValidationError() throws Exception {
                String jsonInput = """
                        {
                            "name": "Apple",
                            "weightAmount": 1.0,
                            "magnitude": "INVALID_MAGNITUDE"
                        }
                        """;

                ResultActions result = mockMvc.perform(patch(API_URL_STRING_ID, ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput));

                result.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.title", containsString("Invalid Request")))
                        .andExpect(jsonPath("$.detail", containsString("Magnitude")))
                        .andExpect(jsonPath("$.detail", containsString("not valid")));

                verifyNoInteractions(updateFruitByIdUseCase);
            }

            @Test
            @DisplayName("returns 400 Bad Request when input data is invalid (valid magnitude, invalid weightAmount)")
            void updateFruitById_whenMagnitudeIsValidAndWeightAmountNull_returns400ValidationError() throws Exception {
                Map<String, Object> body = new HashMap<>();
                body.put("name", "Apple");
                body.put("weightAmount", 1.5);
                body.put("magnitude", null);

                ResultActions result = mockMvc.perform(patch(API_URL_STRING_ID, ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)));

                result.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.title", containsString("Invalid Request")))
                        .andExpect(jsonPath("$.detail", containsString("Magnitude")))
                        .andExpect(jsonPath("$.detail", containsString("required")));

                verifyNoInteractions(updateFruitByIdUseCase);
            }

            @Test
            @DisplayName("returns 400 Bad Request when input data is invalid (valid magnitude, invalid weightAmount)")
            void updateFruitById_whenWeightAmountIsValidAndMagnitudeIsInvalid_returns400ValidationError() throws Exception {
                Map<String, Object> body = new HashMap<>();
                body.put("name", "Apple");
                body.put("weightAmount", null);
                body.put("magnitude", "POUNDS");

                ResultActions result = mockMvc.perform(patch(API_URL_STRING_ID, ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)));

                result.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.title", containsString("Invalid Request")))
                        .andExpect(jsonPath("$.detail", containsString("Weight Amount")))
                        .andExpect(jsonPath("$.detail", containsString("required")));

                verifyNoInteractions(updateFruitByIdUseCase);
            }

            @Test
            @DisplayName("returns 404 Not Found when the service throws a FruitNotFoundException")
            void updateFruitById_returns404WhenFruitNotFoundExceptionIsThrown() throws Exception {
                UpdateFruitRequestDTO updateFruitRequestDTO = new UpdateFruitRequestDTO(NAME_OF_FRUIT, WEIGHT_AMOUNT, KG_MAGNITUDE);
                String exceptionMessage = "There are no registered fruits.";

                when(updateFruitByIdUseCase.execute(ID, updateFruitRequestDTO)).thenThrow(new FruitNotFoundException(exceptionMessage));

                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.patch(API_URL_STRING_ID, ID)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateFruitRequestDTO)));

                result.andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.title", Matchers.containsString("Fruit Not Found")))
                        .andExpect(jsonPath("$.detail", Matchers.containsString(exceptionMessage)));

                verify(updateFruitByIdUseCase).execute(ID, updateFruitRequestDTO);
            }

            @Nested
            @DisplayName("DELETE /api/fruits/{id}")
            class DeleteFruitById {
                String API_URL_STRING_ID = API_URL_STRING + "/{id}";

                @Test
                void getFruitById_returns204() throws Exception {
                    ResultActions result = mockMvc.perform(delete(API_URL_STRING_ID, FRUIT.getId())
                            .contentType(MediaType.APPLICATION_JSON));

                    result.andExpect(status().isNoContent());

                    verify(deleteFruitByIdUseCase).execute(FRUIT.getId());
                }

                @Test
                @DisplayName("returns 400 Bad Request when input data is invalid (id is negative)")
                void getFruitById_returns400BadRequestIdIsNegative() throws Exception {
                    ResultActions result = mockMvc.perform(delete(API_URL_STRING_ID, -1L)
                            .contentType(MediaType.APPLICATION_JSON));

                    result.andExpect(status().isBadRequest())
                            .andExpect(jsonPath("$.title", Matchers.containsString("Validation Error in Parameter")))
                            .andExpect(jsonPath("$.errors['deleteFruitById.id']", Matchers.containsString("ID")))
                            .andExpect(jsonPath("$.errors['deleteFruitById.id']", Matchers.containsString("positive")));

                    verifyNoInteractions(deleteFruitByIdUseCase);
                }

                @Test
                @DisplayName("returns 404 Not Found when the service throws a FruitNotFoundException")
                void deleteFruitById_returns404WhenFruitNotFoundExceptionIsThrown() throws Exception {
                    final String exceptionMessage = "There are no fruits registered with the id " + ID;
                    doThrow(new FruitNotFoundException(exceptionMessage)).when(deleteFruitByIdUseCase).execute(anyLong());

                    ResultActions result = mockMvc.perform(delete(API_URL_STRING_ID, ID)
                            .contentType(MediaType.APPLICATION_JSON));

                    result.andExpect(status().isNotFound())
                            .andExpect(jsonPath("$.title", Matchers.containsString("Fruit Not Found")))
                            .andExpect(jsonPath("$.detail", Matchers.containsString(exceptionMessage)));

                    verify(deleteFruitByIdUseCase).execute(anyLong());
                }
            }
        }
    }
}