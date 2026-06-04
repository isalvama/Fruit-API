package cat.itacademy.s04.s02.n01.fruit.application.service;

import cat.itacademy.s04.s02.n01.fruit.application.repository.FruitRepository;
import cat.itacademy.s04.s02.n01.fruit.application.usecases.GetAllFruitsUseCase;
import cat.itacademy.s04.s02.n01.fruit.controller.exception.FruitNotFoundException;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllFruitsService implements GetAllFruitsUseCase {
    private final FruitRepository fruitRepository;

    @Override
    public List<Fruit> execute() {
        List<Fruit> allFruits = fruitRepository.getAllFruits();
        if (allFruits.isEmpty()){
            throw new FruitNotFoundException("There are no registered fruits.");
        }
        return allFruits;
    }
}
