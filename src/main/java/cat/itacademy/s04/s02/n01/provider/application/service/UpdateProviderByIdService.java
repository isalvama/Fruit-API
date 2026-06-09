package cat.itacademy.s04.s02.n01.provider.application.service;

import cat.itacademy.s04.s02.n01.common.domain.value_object.Name;
import cat.itacademy.s04.s02.n01.provider.application.repository.ProviderRepository;
import cat.itacademy.s04.s02.n01.provider.application.usecase.UpdateProviderByIdUseCase;
import cat.itacademy.s04.s02.n01.provider.controller.UpdateProviderRequestDTO;
import cat.itacademy.s04.s02.n01.provider.controller.exception.ProviderNotFoundException;
import cat.itacademy.s04.s02.n01.provider.domain.model.Country;
import cat.itacademy.s04.s02.n01.provider.domain.model.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UpdateProviderByIdService implements UpdateProviderByIdUseCase {
    private final ProviderRepository providerRepository;

    @Override
    public Provider execute(Long id, UpdateProviderRequestDTO updateProviderRequestDTO) {

        Provider provider = providerRepository.getProviderById(id).orElseThrow(() -> new ProviderNotFoundException("No providers found with id " + id));

        Optional.ofNullable(updateProviderRequestDTO.name()).ifPresent(n -> {
                    if (!n.isBlank()) {
                        provider.setName(new Name(n));
                    } else {
                        provider.setName(provider.getName());
                    }
                }
        );

        Optional.ofNullable(updateProviderRequestDTO.country()).ifPresent(c -> {
                    if (!c.isBlank()) {
                        provider.setCountry(new Country(c));
                    } else {
                        provider.setCountry(provider.getCountry());
                    }
                }
        );

        return providerRepository.saveProvider(provider);
    }
}
