package cat.itacademy.s04.s02.n01.fruit.common.domain;

import cat.itacademy.s04.s02.n01.common.domain.value_object.Name;
import cat.itacademy.s04.s02.n01.fruit.domain.exception.InvalidNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NameTest {
    private static final String NAME = "Apple";

    @Nested
    @DisplayName("Name Creation")
    class NameCreation {

        @Test
        void shouldCreateNameWithFactoryMethod() {
            Name name = Name.of(NAME);

            assertNotNull(name);
            assertEquals(NAME, name.name());
        }
    }

    @Nested
    @DisplayName("Name Validation")
    class NameValidation {

        @Test
        void shouldThrowInvalidNameExceptionWhenNameIsNull() {
            Exception exception = assertThrows(InvalidNameException.class, () -> {
                Name.of(null);
            });
            assertTrue(exception.getMessage().contains("Name"));
            assertTrue(exception.getMessage().contains("null"));

        }

        @Test
        void shouldThrowInvalidNameExceptionWhenNameIsBlank() {
            Exception exception = assertThrows(InvalidNameException.class, () -> {
                Name.of("");
            });
            assertTrue(exception.getMessage().contains("Name"));
            assertTrue(exception.getMessage().contains("blank"));

        }

        @Test
        void shouldThrowInvalidNameExceptionWhenNameSizeIsSmallerThan2() {
            Exception exception = assertThrows(InvalidNameException.class, () -> {
                Name.of("1");
            });
            assertTrue(exception.getMessage().contains("Name"));
            assertTrue(exception.getMessage().contains("2 letters"));

        }

        @Test
        void shouldThrowInvalidNameExceptionWhenNameSizeIsGreaterThan100() {
            String hundredLettersWord = "a".repeat(101);
            Exception exception = assertThrows(InvalidNameException.class, () -> {
                Name.of(hundredLettersWord);
            });
            assertTrue(exception.getMessage().contains("Name"));
            assertTrue(exception.getMessage().contains("100"));

        }

        @Nested
        @DisplayName("Name format")
        class NameCreation {

            @Test
            void shouldCreateNameWithFactoryMethod() {
                Name fruitName = Name.of("lowerCASE name");
                assertEquals("Lowercase Name", fruitName.name());
            }
        }
    }
}