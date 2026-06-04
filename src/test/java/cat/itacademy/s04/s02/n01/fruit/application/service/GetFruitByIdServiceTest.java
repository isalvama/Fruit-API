package cat.itacademy.s04.s02.n01.fruit.application.service;

import cat.itacademy.s04.s02.n01.fruit.application.repository.FruitRepository;
import cat.itacademy.s04.s02.n01.fruit.application.repository.H2FruitRepositoryImpl;
import cat.itacademy.s04.s02.n01.fruit.controller.exception.FruitNotFoundException;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import cat.itacademy.s04.s02.n01.fruit.domain.model.FruitName;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Weight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetFruitByIdServiceTest {
    private static final String NAME_OF_FRUIT = "Apple";
    private static final double WEIGHT_AMOUNT = 0.5;
    private static final String KG_MAGNITUDE = "KILOGRAMS";
    private static final String POUNDS_MAGNITUDE = "POUNDS";
    private static final Long ID = 1L;
    private static final String API_URL_STRING = "/api/fruits";
    private static final Fruit FRUIT = new Fruit(1L, FruitName.of(NAME_OF_FRUIT), Weight.inKiloGrams(WEIGHT_AMOUNT));
    private FruitRepository fruitRepository;
    private GetFruitByIdService getFruitByIdService;

    @BeforeEach
    void setUp(){
        fruitRepository = mock(H2FruitRepositoryImpl.class);
        getFruitByIdService = new GetFruitByIdService(fruitRepository);
    }

    @Test
    void execute_returnsFruitIfFound() {
        when(fruitRepository.getFruitById(FRUIT.getId())).thenReturn(Optional.of(FRUIT));

        Fruit fruit = getFruitByIdService.execute(FRUIT.getId());
        assertNotNull(fruit);
        assertEquals(FRUIT.getId(), fruit.getId());
        assertEquals(FRUIT.getName(), fruit.getName());
        assertEquals(FRUIT.getWeight(), fruit.getWeight());

        verify(fruitRepository).getFruitById(FRUIT.getId());
    }

    @Test
    void execute_throwsFruitNotFoundExceptionIfIsNotFound() {
        when(fruitRepository.getFruitById(FRUIT.getId())).thenReturn(Optional.empty());

        assertThrows(FruitNotFoundException.class, () -> {getFruitByIdService.execute(FRUIT.getId());});

        verify(fruitRepository).getFruitById(FRUIT.getId());
    }
}