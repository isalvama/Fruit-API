package cat.itacademy.s04.s02.n01.fruit.application.repository;

import cat.itacademy.s04.s02.n01.fruit.application.repository.entity.FruitJpaEntity;
import cat.itacademy.s04.s02.n01.fruit.application.repository.entity.FruitMapper;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class H2FruitRepositoryImpl implements FruitRepository{

    private final JpaFruitSpringDataRepository jpaSpringDataRepository;


    @Override
    public Fruit saveFruit(Fruit fruit) {
        FruitJpaEntity fruitJpaEntity = FruitMapper.toEntity(fruit);
        fruitJpaEntity = jpaSpringDataRepository.save(fruitJpaEntity);
        return FruitMapper.toDomain(fruitJpaEntity);
    }
}
