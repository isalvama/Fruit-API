package cat.itacademy.s04.s02.n01.fruit.application.service;

import cat.itacademy.s04.s02.n01.fruit.application.repository.FruitRepository;
import cat.itacademy.s04.s02.n01.fruit.application.repository.JPAFruitRepositoryImpl;
import cat.itacademy.s04.s02.n01.fruit.application.usecases.GetAllFruitsUseCase;
import cat.itacademy.s04.s02.n01.fruit.controller.exception.FruitNotFoundException;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import cat.itacademy.s04.s02.n01.common.domain.value_object.Name;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Weight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetAllFruitsServiceTest {
    private static final String NAME = "Pineapple";
    private static final Double WEIGHT = 4.6;
    private static final String MAGNITUDE = "KILOGRAMS";
    private static final String API_URL = "/api/fruits";
    private static final Fruit FRUIT = Fruit.create(Name.of(NAME), Weight.inKiloGrams(WEIGHT));
    private FruitRepository fruitRepository;
    private GetAllFruitsUseCase getAllFruitsUseCase;

    @BeforeEach
    void setUp(){
        fruitRepository = mock(JPAFruitRepositoryImpl.class);
        getAllFruitsUseCase = new GetAllFruitsService(fruitRepository);
    }


    @Test
    void execute_shouldReturnAListOfFruits() {
        String fruit2Name = "Kiwi";
        double fruit2WeightAmount = 0.3;
        Fruit fruit2 = Fruit.create(Name.of(fruit2Name), Weight.inKiloGrams(fruit2WeightAmount));
        when(fruitRepository.getAllFruits()).thenReturn(List.of(FRUIT, fruit2));

        List<Fruit> fruits = getAllFruitsUseCase.execute();
        assertEquals(2, fruits.size());
        assertEquals(WEIGHT, fruits.getFirst().getWeight().amount());
        assertEquals(NAME, fruits.getFirst().getName().name());
        assertEquals(fruit2WeightAmount, fruits.get(1).getWeight().amount());
        assertEquals(fruit2Name, fruits.get(1).getName().name());
    }

    @Test
    void execute_shouldThrowFruitNotFoundException() {
        when(fruitRepository.getAllFruits()).thenReturn(List.of());
        assertThrows(FruitNotFoundException.class, () -> {getAllFruitsUseCase.execute();});
    }
}