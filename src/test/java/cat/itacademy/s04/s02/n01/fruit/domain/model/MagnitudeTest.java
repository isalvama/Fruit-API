package cat.itacademy.s04.s02.n01.fruit.domain.model;

import cat.itacademy.s04.s02.n01.fruit.domain.exception.InvalidWeightException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MagnitudeTest {

    @Nested
    @DisplayName("Weight Validation")
    class WeightValidation {

        @Test
        void shouldThrowInvalidWeightExceptionWhenAmountIsNegativeOrZero() {
            Exception exception = assertThrows(InvalidWeightException.class, () -> {Magnitude.KILOGRAMS.validate(-1);});
            assertTrue(exception.getMessage().contains("negative"));
        }

        @Test
        void shouldThrowInvalidWeightExceptionWhenAmountKgIsGraterThanMaxLimit() {
            Exception exception = assertThrows(InvalidWeightException.class, () -> {Magnitude.KILOGRAMS.validate(601);});
            assertTrue(exception.getMessage().contains("greater than"));
            assertTrue(exception.getMessage().contains(Magnitude.KILOGRAMS.getSymbol()));

        }

        @Test
        void shouldThrowInvalidWeightExceptionWhenAmountPoundsIsGraterThanMaxLimit() {
            Exception exception = assertThrows(InvalidWeightException.class, () -> {Magnitude.POUNDS.validate(1323);});
            assertTrue(exception.getMessage().contains("greater than"));
            assertTrue(exception.getMessage().contains(Magnitude.POUNDS.getSymbol()));

        }
    }
}