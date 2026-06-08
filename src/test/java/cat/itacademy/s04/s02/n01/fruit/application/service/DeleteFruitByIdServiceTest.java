package cat.itacademy.s04.s02.n01.fruit.application.service;

import cat.itacademy.s04.s02.n01.fruit.application.repository.FruitRepository;
import cat.itacademy.s04.s02.n01.fruit.application.repository.JPAFruitRepositoryImpl;
import cat.itacademy.s04.s02.n01.fruit.application.usecases.DeleteFruitByIdUseCase;
import cat.itacademy.s04.s02.n01.fruit.controller.exception.FruitNotFoundException;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import cat.itacademy.s04.s02.n01.common.domain.value_object.Name;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Weight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DeleteFruitByIdServiceTest {
    private DeleteFruitByIdUseCase deleteFruitByIdUseCase;
    private FruitRepository fruitRepository;
    private static final Long ID = 123L;
    private static final String NAME = "Fruit Name";
    private static final Double WEIGHT = 12.0;
    private static final Name FRUIT_NAME = Name.of(NAME);
    private static final Weight WEIGHT_IN_KG = Weight.inKiloGrams(WEIGHT);
    private static final Fruit FRUIT = new Fruit(ID, FRUIT_NAME, WEIGHT_IN_KG);


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
