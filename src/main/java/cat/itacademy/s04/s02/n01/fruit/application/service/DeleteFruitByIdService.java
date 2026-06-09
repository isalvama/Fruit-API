package cat.itacademy.s04.s02.n01.fruit.application.service;

import cat.itacademy.s04.s02.n01.fruit.application.repository.FruitRepository;
import cat.itacademy.s04.s02.n01.fruit.application.usecases.DeleteFruitByIdUseCase;
import cat.itacademy.s04.s02.n01.fruit.controller.exception.FruitNotFoundException;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteFruitByIdService implements DeleteFruitByIdUseCase {
    private final FruitRepository fruitRepository;
    @Override
    public void execute(Long id) {
        Fruit fruit = fruitRepository.getFruitById(id).orElseThrow(() -> new FruitNotFoundException("There are no fruits registered with the id " + id));
        fruitRepository.deleteFruitById(id);
    }
}
