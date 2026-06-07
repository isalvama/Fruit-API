package cat.itacademy.s04.s02.n01.fruit.application.repository;

import cat.itacademy.s04.s02.n01.fruit.application.repository.entity.FruitJpaEntity;
import cat.itacademy.s04.s02.n01.fruit.application.repository.entity.FruitMapper;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class H2FruitRepositoryImpl implements FruitRepository{

    private final JpaFruitSpringDataRepository jpaSpringDataRepository;

    @Override
    public Fruit saveFruit(Fruit fruit) {
        if (fruit.getId() == null) {
            FruitJpaEntity fruitJpaEntity = FruitMapper.toEntity(fruit);
            fruitJpaEntity = jpaSpringDataRepository.save(fruitJpaEntity);
            return FruitMapper.toDomain(fruitJpaEntity);
        }
        FruitJpaEntity fruitJpaEntity =  jpaSpringDataRepository.findById(String.valueOf(fruit.getId()))
                .map(entity -> {
                    entity.updateFruit(
                            fruit.getName().name(),
                            fruit.getWeight().amount()
                    );
                    return jpaSpringDataRepository.save(entity);
                }).orElseGet(() -> jpaSpringDataRepository.save(FruitMapper.toEntity(fruit)));
        return FruitMapper.toDomain(fruitJpaEntity);
    }

    @Override
    public List<Fruit> getAllFruits() {
        List<FruitJpaEntity> allFruitsJpaEntities = jpaSpringDataRepository.findAll();
        return allFruitsJpaEntities.stream().map(FruitMapper::toDomain).toList();
    }

    @Override
    public Optional<Fruit> getFruitById(Long id){
        Optional<FruitJpaEntity> fruitJpaEntity = jpaSpringDataRepository.findById(String.valueOf(id));
        return fruitJpaEntity.map(FruitMapper::toDomain);
    }

    @Override
    public void deleteFruitById(Long id){
        jpaSpringDataRepository.deleteById(String.valueOf(id));
    }

}
