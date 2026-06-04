package cat.itacademy.s04.s02.n01.fruit.application.service;

import cat.itacademy.s04.s02.n01.fruit.application.repository.FruitRepository;
import cat.itacademy.s04.s02.n01.fruit.application.usecases.GetAllFruitsUseCase;
import cat.itacademy.s04.s02.n01.fruit.controller.exception.FruitNotFoundException;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;

import java.util.List;

public class GetAllFruitsService implements GetAllFruitsUseCase {
    FruitRepository fruitRepository;
    @Override
    public List<Fruit> execute() {
        List<Fruit> allFruits = fruitRepository.getAllFruits();
        if (allFruits.isEmpty()){
            throw new FruitNotFoundException("There are no registered fruits.");
        }
        return allFruits;
    }
}
