package cat.itacademy.s04.s02.n01.fruit.controller;

import cat.itacademy.s04.s02.n01.fruit.controller.exception.InvalidRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateFruitRequestDTOTest {

    @Test
    void constructor_withValidMagnitudesDoesNotThrowException(){
        assertDoesNotThrow(() -> {new UpdateFruitRequestDTO("Apple", 1.0, "KILOGRAMS");});
        assertDoesNotThrow(() -> {new UpdateFruitRequestDTO("Kiwi", 1.0, "POUNDS");});
        assertDoesNotThrow(() -> {new UpdateFruitRequestDTO(null, 1.0, "POUNDS");});
        assertDoesNotThrow(() -> {new UpdateFruitRequestDTO("Orange", null, null);});
    }

    @Test
    void constructor_withNullAmountAndValidMagnitude_throwsInvalidRequestException(){
        assertThrows(InvalidRequestException.class, () -> {new UpdateFruitRequestDTO("Apple", null, "KILOGRAMS");});
        assertThrows(InvalidRequestException.class, () -> {new UpdateFruitRequestDTO("Apple", null, "POUNDS");});
    }

    @Test
    void constructor_withNullBlankOrInvalidMagnitudeAndValidAmount_throwsInvalidRequestException(){
        assertThrows(InvalidRequestException.class, () -> {new UpdateFruitRequestDTO("Apple", 1.0, null);});
        assertThrows(InvalidRequestException.class, () -> {new UpdateFruitRequestDTO("Apple", 1.0, "");});
        assertThrows(InvalidRequestException.class, () -> {new UpdateFruitRequestDTO("Apple", 1.0, "INVALID_MAGNITUDE");});
    }
}