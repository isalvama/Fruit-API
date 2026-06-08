package cat.itacademy.s04.s02.n01.fruit.application.service;

import cat.itacademy.s04.s02.n01.fruit.application.repository.FruitRepository;
import cat.itacademy.s04.s02.n01.fruit.application.usecases.GetFruitsByProviderIdUseCase;
import cat.itacademy.s04.s02.n01.fruit.controller.exception.FruitNotFoundException;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import cat.itacademy.s04.s02.n01.provider.application.repository.ProviderRepository;
import cat.itacademy.s04.s02.n01.provider.controller.exception.ProviderNotFoundException;
import cat.itacademy.s04.s02.n01.provider.domain.model.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetFruitsByProviderIdService implements GetFruitsByProviderIdUseCase {
    private final FruitRepository fruitRepository;
    private final ProviderRepository providerRepository;
    @Override
    public List<Fruit> execute(Long id) {
        Optional<Provider> optionalProvider = providerRepository.getProviderById(id);

        optionalProvider.orElseThrow(() ->
                new ProviderNotFoundException(String.format("Provider with id %s does not exist", id)));

        List<Fruit> fruits = fruitRepository.getFruitsByProviderId(id);

        if (fruits.isEmpty()){
            throw new FruitNotFoundException("There are no fruits registered with the provider with id " + id);
        }

        return fruits;
    }

}
