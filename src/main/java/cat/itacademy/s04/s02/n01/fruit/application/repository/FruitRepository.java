package cat.itacademy.s04.s02.n01.fruit.application.repository;

import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;

import java.util.List;
import java.util.Optional;

public interface FruitRepository {

    Fruit saveFruit(Fruit fruit);

    List<Fruit> getAllFruits();

    Optional<Fruit> getFruitById(Long id);
}
