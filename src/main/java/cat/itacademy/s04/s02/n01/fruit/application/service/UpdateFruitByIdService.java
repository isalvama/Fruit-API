package cat.itacademy.s04.s02.n01.fruit.application.service;

import cat.itacademy.s04.s02.n01.fruit.application.repository.FruitRepository;
import cat.itacademy.s04.s02.n01.fruit.application.usecases.UpdateFruitByIdUseCase;
import cat.itacademy.s04.s02.n01.fruit.controller.dto.UpdateFruitRequestDTO;
import cat.itacademy.s04.s02.n01.fruit.controller.exception.FruitNotFoundException;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import cat.itacademy.s04.s02.n01.common.domain.value_object.Name;
import cat.itacademy.s04.s02.n01.fruit.domain.value_object.Magnitude;
import cat.itacademy.s04.s02.n01.fruit.domain.value_object.Weight;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdateFruitByIdService implements UpdateFruitByIdUseCase {
    private final FruitRepository fruitRepository;

    @Override
    public Fruit execute(Long id, UpdateFruitRequestDTO updateFruitRequestDTO) {

        Fruit fruit = fruitRepository.getFruitById(id).orElseThrow(() -> new FruitNotFoundException("There are no fruits registered with the id " + id));

        if ((updateFruitRequestDTO.magnitude() != null && !updateFruitRequestDTO.magnitude().isBlank()) && updateFruitRequestDTO.weightAmount() != null){
            Magnitude magnitude = Magnitude.fromString(updateFruitRequestDTO.magnitude());
            Weight weight = Weight.toDomainWeight(updateFruitRequestDTO.weightAmount(), magnitude);
            fruit.setWeightInKg(weight.convertToKgWeight());
        }

        Optional.ofNullable(updateFruitRequestDTO.name())
                .ifPresent(name -> {
                    if (!name.isBlank()) {
                        fruit.setName(new Name(name));
                    } else {
                     fruit.setName(fruit.getName());
                    }
                });
        return fruitRepository.saveFruit(fruit);
    }
}
