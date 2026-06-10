package cat.itacademy.s04.s02.n01.fruit.application.usecases;

import cat.itacademy.s04.s02.n01.fruit.controller.dto.UpdateFruitRequestDTO;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;

public interface UpdateFruitByIdUseCase {
    Fruit execute (Long id, UpdateFruitRequestDTO updateFruitRequestDTO);
}
