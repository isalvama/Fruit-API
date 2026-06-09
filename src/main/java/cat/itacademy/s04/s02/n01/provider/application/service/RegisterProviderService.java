package cat.itacademy.s04.s02.n01.provider.application.service;

import cat.itacademy.s04.s02.n01.common.domain.value_object.Name;
import cat.itacademy.s04.s02.n01.provider.application.repository.ProviderRepository;
import cat.itacademy.s04.s02.n01.provider.application.usecase.RegisterProviderUseCase;
import cat.itacademy.s04.s02.n01.provider.controller.exception.ProviderAlreadyExistsException;
import cat.itacademy.s04.s02.n01.provider.domain.model.Country;
import cat.itacademy.s04.s02.n01.provider.domain.model.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class RegisterProviderService implements RegisterProviderUseCase {
    private final ProviderRepository providerRepository;

    @Override
    public Provider execute(String name, String country) {
        Optional<Provider> optionalProvider = providerRepository.getProviderByName(name);
        if (optionalProvider.isPresent()){
            throw new ProviderAlreadyExistsException(String.format("Provider with name %s already exists", name));
        }
        Provider provider = Provider.create(Name.of(name), Country.of(country));
        return providerRepository.saveProvider(provider);
    }
}
