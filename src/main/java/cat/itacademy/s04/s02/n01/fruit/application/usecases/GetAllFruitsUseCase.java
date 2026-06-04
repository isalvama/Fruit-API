package cat.itacademy.s04.s02.n01.fruit.application.usecases;

import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;

import java.util.List;

public interface GetAllFruitsUseCase {
    public List<Fruit> execute();
}
