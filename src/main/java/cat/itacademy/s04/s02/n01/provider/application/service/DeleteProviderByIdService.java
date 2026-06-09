package cat.itacademy.s04.s02.n01.provider.application.service;

import cat.itacademy.s04.s02.n01.fruit.application.repository.FruitRepository;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import cat.itacademy.s04.s02.n01.provider.application.repository.ProviderRepository;
import cat.itacademy.s04.s02.n01.provider.application.usecase.DeleteProviderByIdUseCase;
import cat.itacademy.s04.s02.n01.provider.controller.exception.ProviderHasAssociatedFruitsException;
import cat.itacademy.s04.s02.n01.provider.controller.exception.ProviderNotFoundException;
import cat.itacademy.s04.s02.n01.provider.domain.model.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeleteProviderByIdService implements DeleteProviderByIdUseCase {
    private final ProviderRepository providerRepository;
    private final FruitRepository fruitRepository;
    @Override
    public void execute(Long id) {
        Provider provider = providerRepository.getProviderById(id).orElseThrow(() -> new ProviderNotFoundException("There are no providers registered with the id " + id));
        List<Fruit> providerFruits = fruitRepository.getFruitsByProviderId(id);
        if (!providerFruits.isEmpty()){
            throw new ProviderHasAssociatedFruitsException(String.format("Provider with id %s cannot be deleted because it has associated fruits.", id));
        }
        providerRepository.deleteProviderById(id);
    }
}
