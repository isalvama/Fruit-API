package cat.itacademy.s04.s02.n01.fruit.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FruitNameTest {
    private static final String NAME = "Apple";

    @Nested
    @DisplayName("FruitName Creation")
    class FruitNameCreation {

        @Test
        void shouldCreateFruitNameWithFactoryMethod() {
            FruitName fruitName = FruitName.of(NAME);

            assertNotNull(fruitName);
            assertEquals(NAME, fruitName.name());
        }
    }

    @Nested
    @DisplayName("FruitName Validation")
    class FruitNameValidation {

        @Test
        void shouldThrowInvalidFruitNameExceptionWhenNameIsNull() {
            Exception exception = assertThrows(InvalidFruitNameException.class, () -> {
                FruitName.of(null);
            });
            assertTrue(exception.getMessage().contains("FruitName"));
            assertTrue(exception.getMessage().contains("null"));

        }

        @Test
        void shouldThrowInvalidFruitNameExceptionWhenNameIsBlank() {
            Exception exception = assertThrows(InvalidFruitNameException.class, () -> {
                FruitName.of("");
            });
            assertTrue(exception.getMessage().contains("FruitName"));
            assertTrue(exception.getMessage().contains("blank"));

        }

        @Test
        void shouldThrowInvalidFruitNameExceptionWhenNameSizeIsSmallerThan2() {
            Exception exception = assertThrows(InvalidFruitNameException.class, () -> {
                FruitName.of("1");
            });
            assertTrue(exception.getMessage().contains("FruitName"));
            assertTrue(exception.getMessage().contains("2 letters"));

        }

        @Test
        void shouldThrowInvalidFruitNameExceptionWhenNameSizeIsGreaterThan100() {
            String hundredLettersWord = "a".repeat(101);
            Exception exception = assertThrows(InvalidFruitNameException.class, () -> {
                FruitName.of(hundredLettersWord);
            });
            assertTrue(exception.getMessage().contains("FruitName"));
            assertTrue(exception.getMessage().contains("100"));

        }

        @Nested
        @DisplayName("FruitName format")
        class FruitNameCreation {

            @Test
            void shouldCreateFruitNameWithFactoryMethod() {
                FruitName fruitName = FruitName.of("lowerCASE name");
                assertEquals("Lowercase Name", fruitName.name());
            }
        }
    }
}