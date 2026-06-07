package cat.itacademy.s04.s02.n01.fruit.application.repository.entity;

import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import cat.itacademy.s04.s02.n01.common.domain.value_object.Name;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Magnitude;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Weight;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FruitMapperTest {

    private static final String FRUIT_NAME = "Fruit";
    private static final Double WEIGHT = 1.3;
    private static final Long ID = 1L;

    @Nested
    @DisplayName("Map To Domain")
    public class ToDomain{
        @Test
        void toDomainModel() {
            Fruit fruit = new Fruit(null, Name.of(FRUIT_NAME), Weight.inKiloGrams(WEIGHT));

            FruitJpaEntity fruitJpaEntity = FruitMapper.toEntity(fruit);
            assertNull(fruitJpaEntity.getId());
            assertEquals(FRUIT_NAME, fruitJpaEntity.getName());
            assertEquals(WEIGHT, fruitJpaEntity.getWeightInKg());
        }
    }

    @Nested
    @DisplayName("Map To Entity")
    public class ToEntity{
        @Test
        void toModelEntity() {
            FruitJpaEntity fruitJpaEntity = new FruitJpaEntity(ID, FRUIT_NAME, WEIGHT);
            Fruit fruit = FruitMapper.toDomain(fruitJpaEntity);
            assertEquals(ID, fruit.getId());
            assertEquals(FRUIT_NAME, fruit.getName().name());
            assertEquals(WEIGHT, fruit.getWeight().amount());
            assertEquals(Magnitude.KILOGRAMS, fruit.getWeight().magnitude());
        }
    }
}