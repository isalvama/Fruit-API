package cat.itacademy.s04.s02.n01.fruit.application.repository.entity;

import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import cat.itacademy.s04.s02.n01.common.domain.value_object.Name;
import cat.itacademy.s04.s02.n01.fruit.domain.value_object.Magnitude;
import cat.itacademy.s04.s02.n01.fruit.domain.value_object.Weight;
import cat.itacademy.s04.s02.n01.provider.application.repository.entity.ProviderJPAEntity;
import cat.itacademy.s04.s02.n01.provider.domain.value_object.Country;
import cat.itacademy.s04.s02.n01.provider.domain.model.Provider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FruitMapperTest {

    private static final String PROVIDER_NAME = "Fruit Provider";
    private static final String COUNTRY = "US";
    private static final Long ID = 1L;
    private static final Provider PROVIDER = new Provider(ID, Name.of(PROVIDER_NAME), Country.of(COUNTRY));
    private static final ProviderJPAEntity PROVIDER_JPA_ENTITY = new ProviderJPAEntity(ID, PROVIDER_NAME, COUNTRY);

    private static final String FRUIT_NAME = "Fruit";
    private static final Double WEIGHT = 1.3;

    @Nested
    @DisplayName("Map To Domain")
    public class ToDomain{
        @Test
        void toDomainModel() {
            Fruit fruit = new Fruit(null, Name.of(FRUIT_NAME), Weight.inKiloGrams(WEIGHT), PROVIDER);
            FruitJpaEntity fruitJpaEntity = FruitMapper.toEntity(fruit);
            assertNull(fruitJpaEntity.getId());
            assertEquals(FRUIT_NAME, fruitJpaEntity.getName());
            assertEquals(WEIGHT, fruitJpaEntity.getWeightInKg());
            assertEquals(PROVIDER_NAME, fruitJpaEntity.getProvider().getName());
            assertEquals(COUNTRY, fruitJpaEntity.getProvider().getCountry());
        }
    }

    @Nested
    @DisplayName("Map To Entity")
    public class ToEntity{
        @Test
        void toModelEntity() {
            FruitJpaEntity fruitJpaEntity = new FruitJpaEntity(ID, FRUIT_NAME, WEIGHT, PROVIDER_JPA_ENTITY);
            Fruit fruit = FruitMapper.toDomain(fruitJpaEntity);
            assertEquals(ID, fruit.getId());
            assertEquals(FRUIT_NAME, fruit.getName().name());
            assertEquals(WEIGHT, fruit.getWeightInKg().amount());
            assertEquals(Magnitude.KILOGRAMS, fruit.getWeightInKg().magnitude());
            assertEquals(PROVIDER_NAME, fruit.getProvider().getName().name());
            assertEquals(COUNTRY, fruit.getProvider().getCountry().name());

        }
    }
}