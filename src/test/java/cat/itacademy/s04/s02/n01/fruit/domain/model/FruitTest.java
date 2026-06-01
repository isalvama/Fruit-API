package cat.itacademy.s04.s02.n01.fruit.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FruitTest {

    private static final FruitName FRUIT_NAME = FruitName.of("Fruit");
    private static final Weight WEIGHT = new Weight(100.0, Magnitude.KILOGRAMS);


    @Nested
    @DisplayName("Fruit Creation")
    class FruitCreation {
        @Test
        void shouldCreateFruitWithFactoryMethod() {
            Fruit fruit = Fruit.create(FRUIT_NAME, WEIGHT);

            assertNotNull(fruit);
            assertEquals(FRUIT_NAME, fruit.getName());
            assertEquals(WEIGHT, fruit.getWeight());
        }
    }
}