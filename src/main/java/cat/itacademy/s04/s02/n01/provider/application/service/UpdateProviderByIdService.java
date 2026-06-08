package cat.itacademy.s04.s02.n01.provider.application.service;

import cat.itacademy.s04.s02.n01.provider.application.usecase.UpdateProviderByIdUseCase;
import cat.itacademy.s04.s02.n01.provider.controller.UpdateProviderRequestDTO;
import cat.itacademy.s04.s02.n01.provider.domain.model.Provider;

public class UpdateProviderByIdService implements UpdateProviderByIdUseCase {
    @Override
    public Provider execute(Long id, UpdateProviderRequestDTO updateProviderRequestDTO) {
        return null;
    }
}
