package cat.itacademy.s04.s02.n01.fruit.application.service;

import cat.itacademy.s04.s02.n01.fruit.application.repository.FruitRepository;
import cat.itacademy.s04.s02.n01.fruit.application.repository.JPAFruitRepositoryImpl;
import cat.itacademy.s04.s02.n01.fruit.application.usecases.DeleteFruitByIdUseCase;
import cat.itacademy.s04.s02.n01.fruit.controller.exception.FruitNotFoundException;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import cat.itacademy.s04.s02.n01.common.domain.value_object.Name;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Weight;
import cat.itacademy.s04.s02.n01.provider.domain.model.Country;
import cat.itacademy.s04.s02.n01.provider.domain.model.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DeleteFruitByIdServiceTest {
    private static final Provider PROVIDER = new Provider(1L, Name.of("Fruit Provider"), Country.of("US"));

    private static final Long ID = 123L;
    private static final Fruit FRUIT = new Fruit(123L, Name.of("Fruit Name"), Weight.inKiloGrams(12.0), PROVIDER);

    private DeleteFruitByIdUseCase deleteFruitByIdUseCase;
    private FruitRepository fruitRepository;

    @BeforeEach
    void setUp() {
        fruitRepository = mock(JPAFruitRepositoryImpl.class);
        deleteFruitByIdUseCase = new DeleteFruitByIdService(fruitRepository);
    }

    @Test
    void execute_whenRepositoryReturnsEmptyOptional_throwsFruitNotFoundException() {
        when(fruitRepository.getFruitById(ID)).thenReturn(Optional.empty());
        assertThrows(FruitNotFoundException.class, () -> {
            deleteFruitByIdUseCase.execute(ID);
        });

        verify(fruitRepository).getFruitById(ID);
        verify(fruitRepository, never()).deleteFruitById(any(long.class));
    }

    @Test
    void execute_whenRepositoryReturnsOptionalWithFruit_callsDeleteFruitByIdMethod() {
        when(fruitRepository.getFruitById(ID)).thenReturn(Optional.of(FRUIT));
        assertDoesNotThrow(() -> {
            deleteFruitByIdUseCase.execute(ID);
        });

        verify(fruitRepository).getFruitById(ID);
        verify(fruitRepository, times(1)).deleteFruitById(ID);
    }
}
