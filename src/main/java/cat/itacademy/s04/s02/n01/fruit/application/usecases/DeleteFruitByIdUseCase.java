package cat.itacademy.s04.s02.n01.fruit.application.usecases;

import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;

public interface DeleteFruitByIdUseCase {
    void execute (Long id);
}
