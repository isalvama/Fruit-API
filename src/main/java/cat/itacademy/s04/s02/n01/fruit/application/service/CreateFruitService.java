package cat.itacademy.s04.s02.n01.fruit.application.service;

import cat.itacademy.s04.s02.n01.fruit.application.repository.FruitRepository;
import cat.itacademy.s04.s02.n01.fruit.application.usecases.CreateFruitUseCase;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import cat.itacademy.s04.s02.n01.fruit.domain.model.FruitName;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Magnitude;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Weight;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateFruitService implements CreateFruitUseCase {
    private final FruitRepository h2FruitRepository;

    @Override
    public Fruit createFruit(String name, double weightAmount, String magnitude) {
        Weight weight = Weight.toDomainWeight(weightAmount, Magnitude.fromString(magnitude.toUpperCase()));
        Fruit fruit = Fruit.create(FruitName.of(name), weight.convertToKgWeight());
        return h2FruitRepository.saveFruit(fruit);
    }
}
