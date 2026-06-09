package cat.itacademy.s04.s02.n01.fruit.domain.model;

import cat.itacademy.s04.s02.n01.common.domain.value_object.Name;
import cat.itacademy.s04.s02.n01.provider.domain.model.Country;
import cat.itacademy.s04.s02.n01.provider.domain.model.Provider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FruitTest {

    private static final String PROVIDER_NAME = "Fruit Provider";
    private static final String COUNTRY = "US";
    private static final Long PROVIDER_ID = 1L;
    private static final Provider PROVIDER = new Provider(PROVIDER_ID, Name.of(PROVIDER_NAME), Country.of(COUNTRY));

    private static final Name FRUIT_NAME = Name.of("Fruit");
    private static final Weight WEIGHT = new Weight(100.0, Magnitude.KILOGRAMS);


    @Nested
    @DisplayName("Fruit Creation")
    class FruitCreation {
        @Test
        void shouldCreateFruitWithFactoryMethod() {
            Fruit fruit = Fruit.create(FRUIT_NAME, WEIGHT, PROVIDER);

            assertNotNull(fruit);
            assertEquals(FRUIT_NAME, fruit.getName());
            assertEquals(WEIGHT, fruit.getWeightInKg());
        }
    }
}