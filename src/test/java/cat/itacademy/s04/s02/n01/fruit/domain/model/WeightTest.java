package cat.itacademy.s04.s02.n01.fruit.domain.model;

import cat.itacademy.s04.s02.n01.fruit.domain.exception.InvalidWeightException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeightTest {

    private static final double AMOUNT = 1.6;

    @Nested
    @DisplayName("Weight Creation")
    class WeightCreation {
        @Test
        void shouldCreateKgWeightWithInKiloGramsFactoryMethod() {
            Weight weightInKg = Weight.inKiloGrams(AMOUNT);

            assertNotNull(weightInKg);
            assertEquals(Magnitude.KILOGRAMS, weightInKg.magnitude());
            assertEquals(AMOUNT, weightInKg.amount());
        }

        @Test
        void shouldCreateLbsWeightWithInPoundsFactoryMethod() {
            Weight weightInKg = Weight.inPounds(AMOUNT);

            assertNotNull(weightInKg);
            assertEquals(Magnitude.POUNDS, weightInKg.magnitude());
            assertEquals(AMOUNT, weightInKg.amount());
        }
    }

    @Nested
    @DisplayName("Weight Validation")
    class WeightValidation {
        @Test
        void shouldThrowInvalidWeightExceptionWhenMagnitudeIsNull() {
            Exception exception = assertThrows(InvalidWeightException.class, () -> {new Weight(AMOUNT, null);});
            assertTrue(exception.getMessage().contains("null"));
            assertTrue(exception.getMessage().contains("magnitude"));
        }
    }
}