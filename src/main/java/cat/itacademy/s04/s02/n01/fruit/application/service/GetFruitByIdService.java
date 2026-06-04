package cat.itacademy.s04.s02.n01.fruit.application.service;

import cat.itacademy.s04.s02.n01.fruit.application.repository.FruitRepository;
import cat.itacademy.s04.s02.n01.fruit.application.usecases.GetFruitByIdUseCase;
import cat.itacademy.s04.s02.n01.fruit.controller.exception.FruitNotFoundException;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetFruitByIdService implements GetFruitByIdUseCase {
    private final FruitRepository fruitRepository;

    @Override
    public Fruit execute(Long id) {
        Optional<Fruit> optionalFruit = fruitRepository.getFruitById(id);
        return optionalFruit.orElseThrow(() -> new FruitNotFoundException("There are no fruits registered with the id " + id));
    }
}
