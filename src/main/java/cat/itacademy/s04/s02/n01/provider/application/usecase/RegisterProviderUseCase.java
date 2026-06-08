package cat.itacademy.s04.s02.n01.provider.application.usecase;

import cat.itacademy.s04.s02.n01.provider.domain.model.Provider;

public interface RegisterProviderUseCase {
    Provider execute(String name, String country);
}
