package cat.itacademy.s04.s02.n01.fruit.controller;

import cat.itacademy.s04.s02.n01.fruit.controller.exception.InvalidRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateFruitRequestDTOTest {

    @Test
    void constructor_withValidMagnitudesDoesNotThrowException(){
        assertDoesNotThrow(() -> {new CreateFruitRequestDTO("Apple", 1.0, "KILOGRAMS");});
        assertDoesNotThrow(() -> {new CreateFruitRequestDTO("Apple", 1.0, "POUNDS");});
    }

    @Test
    void constructor_withInvalidMagnitude_throwsInvalidRequestException(){
        assertThrows(InvalidRequestException.class, () -> {new CreateFruitRequestDTO("Apple", 1.0, "INVALID_MAGNITUDE");
        }
        );
    }


}