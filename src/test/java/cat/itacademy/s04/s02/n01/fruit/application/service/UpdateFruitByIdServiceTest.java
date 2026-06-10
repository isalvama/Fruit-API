package cat.itacademy.s04.s02.n01.fruit.application.service;

import cat.itacademy.s04.s02.n01.fruit.application.repository.FruitRepository;
import cat.itacademy.s04.s02.n01.fruit.application.repository.JPAFruitRepositoryImpl;
import cat.itacademy.s04.s02.n01.fruit.application.usecases.UpdateFruitByIdUseCase;
import cat.itacademy.s04.s02.n01.fruit.controller.dto.UpdateFruitRequestDTO;
import cat.itacademy.s04.s02.n01.fruit.controller.exception.FruitNotFoundException;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import cat.itacademy.s04.s02.n01.common.domain.value_object.Name;
import cat.itacademy.s04.s02.n01.fruit.domain.value_object.Magnitude;
import cat.itacademy.s04.s02.n01.fruit.domain.value_object.Weight;
import cat.itacademy.s04.s02.n01.provider.domain.value_object.Country;
import cat.itacademy.s04.s02.n01.provider.domain.model.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateFruitByIdServiceTest {
    private UpdateFruitByIdUseCase updateFruitByIdUseCase;
    private FruitRepository fruitRepository;

    private static final String PROVIDER_NAME = "Fruit Provider";
    private static final String COUNTRY = "US";
    private static final Long PROVIDER_ID = 1L;
    private static final Provider PROVIDER = new Provider(PROVIDER_ID, Name.of(PROVIDER_NAME), Country.of(COUNTRY));

    private static final Long ID = 123L;
    private static final String NAME = "Fruit Name";
    private static final Double WEIGHT = 12.0;
    private static final Name FRUIT_NAME = Name.of(NAME);
    private static final Weight WEIGHT_IN_KG = Weight.inKiloGrams(WEIGHT);
    private static final Fruit FRUIT = new Fruit(ID, FRUIT_NAME, WEIGHT_IN_KG, PROVIDER);
    private static final String KG_MAGNITUDE_CONST_STRING = "KILOGRAMS";


    @BeforeEach
    void setUp() {
        fruitRepository = mock(JPAFruitRepositoryImpl.class);
        updateFruitByIdUseCase = new UpdateFruitByIdService(fruitRepository);
    }

    @Test
    void updateFruit_newWeight() {
        final Double newWeight = 1.0;
        UpdateFruitRequestDTO updateFruitRequestDTO = new UpdateFruitRequestDTO(null, newWeight, KG_MAGNITUDE_CONST_STRING);
        when(fruitRepository.getFruitById(ID)).thenReturn(Optional.of(FRUIT));
        when(fruitRepository.saveFruit(any(Fruit.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        Fruit fruit = updateFruitByIdUseCase.execute(ID, updateFruitRequestDTO);
        assertNotNull(fruit);
        assertEquals(NAME, fruit.getName().name());
        assertEquals(newWeight, fruit.getWeightInKg().amount());

        verify(fruitRepository).getFruitById(any());
        verify(fruitRepository).saveFruit(any(Fruit.class));

    }

    @Test
    void updateFruit_newName() {
        final String newName = "Kiwi";
        UpdateFruitRequestDTO updateFruitRequestDTO = new UpdateFruitRequestDTO(newName, null, null);

        when(fruitRepository.getFruitById(ID)).thenReturn(Optional.of(FRUIT));
        when(fruitRepository.saveFruit(any(Fruit.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        Fruit fruit = updateFruitByIdUseCase.execute(ID, updateFruitRequestDTO);

        assertNotNull(fruit);
        assertEquals(newName, fruit.getName().name());
        assertEquals(WEIGHT, fruit.getWeightInKg().amount());

        verify(fruitRepository).getFruitById(any());
        verify(fruitRepository).saveFruit(any(Fruit.class));
    }

    @Test
    void updateFruit_newNameAndWeight() {
        final String newName = "Kiwi";
        final double newWeight = 23.5;
        UpdateFruitRequestDTO updateFruitRequestDTO = new UpdateFruitRequestDTO(newName, newWeight, KG_MAGNITUDE_CONST_STRING);
        when(fruitRepository.getFruitById(ID)).thenReturn(Optional.of(FRUIT));
        when(fruitRepository.saveFruit(any(Fruit.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        Fruit fruit = updateFruitByIdUseCase.execute(ID, updateFruitRequestDTO);
        assertNotNull(fruit);
        assertEquals(newName, fruit.getName().name());
        assertEquals(newWeight, fruit.getWeightInKg().amount());

        verify(fruitRepository).getFruitById(any());
        verify(fruitRepository).saveFruit(any(Fruit.class));
    }

    @Test
    void updateFruit_newWeightInPounds() {
        final String newName = "Kiwi";
        final double newWeight = 23.5;
        UpdateFruitRequestDTO updateFruitRequestDTO = new UpdateFruitRequestDTO(newName, newWeight, "POUNDS");
        when(fruitRepository.getFruitById(ID)).thenReturn(Optional.of(FRUIT));
        when(fruitRepository.saveFruit(any(Fruit.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        Fruit fruit = updateFruitByIdUseCase.execute(ID, updateFruitRequestDTO);
        assertNotNull(fruit);
        assertEquals(newName, fruit.getName().name());
        assertEquals(Magnitude.POUNDS.convertToKg(newWeight), fruit.getWeightInKg().amount());

        verify(fruitRepository).getFruitById(any());
        verify(fruitRepository).saveFruit(any(Fruit.class));
    }

    @Test
    void updateFruit_nameIsBlank_remainsTheSame() {
        final String newName = "    ";
        UpdateFruitRequestDTO updateFruitRequestDTO = new UpdateFruitRequestDTO(newName, null, null);
        when(fruitRepository.getFruitById(ID)).thenReturn(Optional.of(FRUIT));
        when(fruitRepository.saveFruit(any(Fruit.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        Fruit fruit = updateFruitByIdUseCase.execute(ID, updateFruitRequestDTO);
        assertNotNull(fruit);
        assertEquals(NAME, fruit.getName().name());
        assertEquals(WEIGHT, fruit.getWeightInKg().amount());

        verify(fruitRepository).getFruitById(any());
        verify(fruitRepository).saveFruit(any(Fruit.class));
    }

    @Test
    void updateFruit_magnitudeIsBlank_remainsTheSame() {
        UpdateFruitRequestDTO updateFruitRequestDTO = new UpdateFruitRequestDTO(null, null, null);
        when(fruitRepository.getFruitById(ID)).thenReturn(Optional.of(FRUIT));
        when(fruitRepository.saveFruit(any(Fruit.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        Fruit fruit = updateFruitByIdUseCase.execute(ID, updateFruitRequestDTO);
        assertNotNull(fruit);
        assertEquals(NAME, fruit.getName().name());
        assertEquals(WEIGHT, fruit.getWeightInKg().amount());

        verify(fruitRepository).getFruitById(any());
        verify(fruitRepository).saveFruit(any(Fruit.class));
    }

    @Test
    void updateFruit_whenRepositoryReturnsEmptyOptional_throwsFruitNotFoundException() {
        UpdateFruitRequestDTO updateFruitRequestDTO = new UpdateFruitRequestDTO("Lemon", 9.3, KG_MAGNITUDE_CONST_STRING);
        when(fruitRepository.getFruitById(ID)).thenReturn(Optional.empty());
        assertThrows(FruitNotFoundException.class, () -> {updateFruitByIdUseCase.execute(ID, updateFruitRequestDTO);});

        verify(fruitRepository).getFruitById(ID);
        verify(fruitRepository, never()).saveFruit(any(Fruit.class));
    }
}