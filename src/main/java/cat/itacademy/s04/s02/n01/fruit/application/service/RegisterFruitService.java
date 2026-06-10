package cat.itacademy.s04.s02.n01.fruit.application.service;

import cat.itacademy.s04.s02.n01.fruit.application.repository.FruitRepository;
import cat.itacademy.s04.s02.n01.fruit.application.usecases.RegisterFruitUseCase;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import cat.itacademy.s04.s02.n01.common.domain.value_object.Name;
import cat.itacademy.s04.s02.n01.fruit.domain.value_object.Magnitude;
import cat.itacademy.s04.s02.n01.fruit.domain.value_object.Weight;
import cat.itacademy.s04.s02.n01.provider.application.repository.ProviderRepository;
import cat.itacademy.s04.s02.n01.provider.controller.exception.ProviderNotFoundException;
import cat.itacademy.s04.s02.n01.provider.domain.model.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegisterFruitService implements RegisterFruitUseCase {
    private final FruitRepository fruitRepository;
    private final ProviderRepository providerRepository;

    @Override
    public Fruit registerFruit(String name, Double weightAmount, String magnitudeString, Long providerId) {
        Optional<Provider> optionalProvider = providerRepository.getProviderById(providerId);

        Provider provider = optionalProvider.orElseThrow(() ->
            new ProviderNotFoundException(String.format("Provider with id %s does not exist", providerId)));

        Weight weight = Weight.toDomainWeight(weightAmount, Magnitude.fromString(magnitudeString));
        Fruit fruit = Fruit.create(Name.of(name), weight.convertToKgWeight(), provider);
        return fruitRepository.saveFruit(fruit);
    }
}
