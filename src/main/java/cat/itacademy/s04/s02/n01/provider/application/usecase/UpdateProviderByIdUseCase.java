package cat.itacademy.s04.s02.n01.provider.application.usecase;

import cat.itacademy.s04.s02.n01.provider.controller.dto.UpdateProviderRequestDTO;
import cat.itacademy.s04.s02.n01.provider.domain.model.Provider;

public interface UpdateProviderByIdUseCase {
    Provider execute(Long id, UpdateProviderRequestDTO updateProviderRequestDTO);
}
