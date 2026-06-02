package cat.itacademy.s04.s02.n01.fruit.application.usecases;

import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Weight;

public interface CreateFruitUseCase {

    public Fruit createFruitInKg(String name, Weight weight);
}
