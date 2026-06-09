package cat.itacademy.s04.s02.n01.provider.application.service;

import cat.itacademy.s04.s02.n01.common.domain.value_object.Name;
import cat.itacademy.s04.s02.n01.fruit.application.repository.FruitRepository;
import cat.itacademy.s04.s02.n01.fruit.application.repository.JPAFruitRepositoryImpl;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Weight;
import cat.itacademy.s04.s02.n01.provider.application.repository.ProviderRepository;
import cat.itacademy.s04.s02.n01.provider.application.usecase.DeleteProviderByIdUseCase;
import cat.itacademy.s04.s02.n01.provider.controller.exception.ProviderHasAssociatedFruitsException;
import cat.itacademy.s04.s02.n01.provider.controller.exception.ProviderNotFoundException;
import cat.itacademy.s04.s02.n01.provider.domain.model.Country;
import cat.itacademy.s04.s02.n01.provider.domain.model.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DeleteProviderByIdServiceTest {
    private static final Provider PROVIDER = new Provider(1L, Name.of("Fruit Provider"), Country.of("US"));

    private static final Long ID = 123L;
    private static final Fruit FRUIT = new Fruit(ID, Name.of("Fruit Name"), Weight.inKiloGrams(12.0), PROVIDER);

    private DeleteProviderByIdUseCase deleteProviderByIdUseCase;
    private FruitRepository fruitRepository;
    private ProviderRepository providerRepository;

    @BeforeEach
    void setUp() {
        fruitRepository = mock(JPAFruitRepositoryImpl.class);
        providerRepository = mock(ProviderRepository.class);
        deleteProviderByIdUseCase = new DeleteProviderByIdService(providerRepository, fruitRepository);
    }

    @Test
    void execute_whenProviderRepositoryReturnsEmptyOptional_throwsProviderNotFoundException() {
        when(providerRepository.getProviderById(ID)).thenReturn(Optional.empty());
        assertThrows(ProviderNotFoundException.class, () -> {
            deleteProviderByIdUseCase.execute(ID);
        });

        verify(providerRepository).getProviderById(ID);
        verify(providerRepository, never()).deleteProviderById(any(long.class));
    }

    @Test
    void execute_whenFruitRepositoryReturnsListWithFruit_throwsProviderHasAssociatedFruitsException() {
        when(providerRepository.getProviderById(ID)).thenReturn(Optional.of(PROVIDER));
        when(fruitRepository.getFruitsByProviderId(ID)).thenReturn(List.of(FRUIT));

        assertThrows(ProviderHasAssociatedFruitsException.class, () -> {
            deleteProviderByIdUseCase.execute(ID);
        });

        verify(fruitRepository).getFruitsByProviderId(ID);
        verify(providerRepository, never()).deleteProviderById(any(long.class));
    }

    @Test
    void execute_whenFruitRepositoryReturnsEmptyList_throwsProviderHasAssociatedFruitsException() {
        when(providerRepository.getProviderById(ID)).thenReturn(Optional.of(PROVIDER));
        when(fruitRepository.getFruitsByProviderId(ID)).thenReturn(List.of());

        assertDoesNotThrow(() -> {
            deleteProviderByIdUseCase.execute(ID);
        });

        verify(fruitRepository).getFruitsByProviderId(ID);
        verify(providerRepository, times(1)).deleteProviderById(any(long.class));
    }
}