package cat.itacademy.s04.s02.n01.fruit.controller;

import cat.itacademy.s04.s02.n01.fruit.application.service.CreateFruitService;
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


import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoInteractions;
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

    @BeforeEach
    void setUp(){
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
        void createUser_returns400ValidationErrorInInputDataBlankName() throws Exception {
            String jsonInput =
                    "{\"name\": \"\", \"weightAmount\" : 0.5, \"magnitude\": \"POUNDS\"}";


            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(API_URL_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonInput));

            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title", Matchers.containsString("Validation Error")))
                    .andExpect(jsonPath("$.errors.name", Matchers.containsString("blank")));

            verifyNoInteractions(createFruitUseCase);
        }

        @Test
        @DisplayName("returns 400 Bad Request when input data is invalid (weightAmount is negative)")
        void createUser_returns400ValidationErrorInInputDataWeightAmountIsNegative() throws Exception {
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
        void createUser_returns400ValidationErrorInInputDataInvalidMagnitude() throws Exception {
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


    }
}