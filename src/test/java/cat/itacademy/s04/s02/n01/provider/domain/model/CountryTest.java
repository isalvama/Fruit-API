package cat.itacademy.s04.s02.n01.provider.domain.model;

import cat.itacademy.s04.s02.n01.provider.domain.exception.InvalidCountryException;
import cat.itacademy.s04.s02.n01.provider.domain.value_object.Country;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountryTest {

        private static final String NAME = "ES";

        @Nested
        @DisplayName("Country Creation")
        class CountryCreation {

            @Test
            void shouldCreateCountryWithFactoryMethod() {
                Country country = Country.of(NAME);

                assertNotNull(country);
                assertEquals(NAME, country.name());
            }
        }

        @Nested
        @DisplayName("Country Validation")
        class CountryValidation {

            @Test
            void shouldThrowInvalidCountryExceptionWhenNameIsNull() {
                Exception exception = assertThrows(InvalidCountryException.class, () -> {
                    Country.of(null);
                });
                assertTrue(exception.getMessage().contains("Country"));
                assertTrue(exception.getMessage().contains("null"));

            }

            @Test
            void shouldThrowInvalidCountryExceptionWhenNameIsBlank() {
                Exception exception = assertThrows(InvalidCountryException.class, () -> {
                    Country.of("");
                });
                assertTrue(exception.getMessage().contains("Country"));
                assertTrue(exception.getMessage().contains("blank"));

            }

            @Test
            void shouldThrowInvalidCountryExceptionWhenNameSizeIsSmallerThan2() {
                Exception exception = assertThrows(InvalidCountryException.class, () -> {
                    Country.of("1");
                });
                assertTrue(exception.getMessage().contains("Country"));
                assertTrue(exception.getMessage().contains("2 letters"));

            }

            @Test
            void shouldThrowInvalidCountryExceptionWhenNameSizeIsGreaterThan100() {
                String hundredLettersWord = "a".repeat(3);
                Exception exception = assertThrows(InvalidCountryException.class, () -> {
                    Country.of(hundredLettersWord);
                });
                assertTrue(exception.getMessage().contains("Country"));
                assertTrue(exception.getMessage().contains("2 letters"));

            }

            @Nested
            @DisplayName("Country format")
            class CountryCreation {

                @Test
                void shouldCreateCountryWithFactoryMethod() {
                    Country country = Country.of("es");
                    assertEquals(NAME, country.name());
                }
            }
        }
    }