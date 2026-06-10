package cat.itacademy.s04.s02.n01.fruit.application.repository.entity;

import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import cat.itacademy.s04.s02.n01.common.domain.value_object.Name;
import cat.itacademy.s04.s02.n01.fruit.domain.value_object.Weight;
import cat.itacademy.s04.s02.n01.provider.application.repository.entity.ProviderMapper;

public class FruitMapper {

    private FruitMapper(){}

    public static Fruit toDomain(FruitJpaEntity fruitEntity){
        return new Fruit(
                fruitEntity.getId(),
                Name.of(fruitEntity.getName()),
                Weight.inKiloGrams(fruitEntity.getWeightInKg()),
                ProviderMapper.toDomain(fruitEntity.getProvider())
        );
    }

    public static FruitJpaEntity toEntity(Fruit fruit){
        return new FruitJpaEntity(
                fruit.getId() != null ? fruit.getId() : null,
                fruit.getName().name(),
                fruit.getWeightInKg().amount(),
                ProviderMapper.toEntity(fruit.getProvider())
        );
    }
}
