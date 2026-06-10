package cat.itacademy.s04.s02.n01.fruit.controller;

import cat.itacademy.s04.s02.n01.fruit.controller.dto.RegisterFruitRequestDTO;
import cat.itacademy.s04.s02.n01.fruit.controller.exception.InvalidRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateFruitRequestDTOTest {
    private final static Long PROVIDER_ID = 1L;

    @Test
    void constructor_withValidMagnitudesDoesNotThrowException(){
        assertDoesNotThrow(() -> {new RegisterFruitRequestDTO("Apple", 1.0, "KILOGRAMS", PROVIDER_ID);});
        assertDoesNotThrow(() -> {new RegisterFruitRequestDTO("Apple", 1.0, "POUNDS", PROVIDER_ID);});
    }

    @Test
    void constructor_withInvalidMagnitude_throwsInvalidRequestException(){
        assertThrows(InvalidRequestException.class, () -> {new RegisterFruitRequestDTO("Apple", 1.0, "INVALID_MAGNITUDE", PROVIDER_ID);
        }
        );
    }

    @Test
    void constructor_withBlankMagnitude_throwsInvalidRequestException(){
        assertThrows(InvalidRequestException.class, () -> {new RegisterFruitRequestDTO("Apple", 1.0, "", PROVIDER_ID);
                }
        );
    }
}