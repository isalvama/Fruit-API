package cat.itacademy.s04.s02.n01.fruit.application.service;

import cat.itacademy.s04.s02.n01.fruit.application.repository.FruitRepository;
import cat.itacademy.s04.s02.n01.fruit.application.repository.H2FruitRepositoryImpl;
import cat.itacademy.s04.s02.n01.fruit.application.usecases.CreateFruitUseCase;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import cat.itacademy.s04.s02.n01.fruit.domain.model.FruitName;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Weight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateFruitServiceTest {
    private CreateFruitUseCase createFruitUseCase;
    private FruitRepository fruitRepository;
    private static final Long ID = 123L;
    private static final String NAME = "Fruit Name";
    private static final Double WEIGHT = 12.0;
    private static final FruitName FRUIT_NAME = FruitName.of(NAME);
    private static final Weight WEIGHT_IN_KG = Weight.inKiloGrams(WEIGHT);
    private static final Fruit FRUIT = new Fruit(ID, FRUIT_NAME, WEIGHT_IN_KG);

    @BeforeEach
    void setUp(){
        fruitRepository = mock(H2FruitRepositoryImpl.class);
        createFruitUseCase = new CreateFruitService(fruitRepository);
    }

    @Test
    void createFruitInKg() {
        when(fruitRepository.saveFruit(FRUIT)).thenReturn(FRUIT);

        Fruit fruit = createFruitUseCase.createFruitInKg(NAME, WEIGHT_IN_KG);

        assertNotNull(fruit);
        assertEquals(ID, fruit.getId());
        assertEquals(NAME, fruit.getName().name());
        assertEquals(WEIGHT, fruit.getWeight().amount());
    }
}