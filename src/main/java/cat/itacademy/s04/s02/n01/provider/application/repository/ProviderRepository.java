package cat.itacademy.s04.s02.n01.provider.application.repository;

import cat.itacademy.s04.s02.n01.provider.domain.model.Provider;

import java.util.Optional;

public interface ProviderRepository {
    Provider saveProvider(Provider provider);
    Optional<Provider> getProviderByName(String name);
    Optional<Provider> getProviderById(Long ProviderId);
}
