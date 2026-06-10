package cat.itacademy.s04.s02.n01.fruit.application.service;

import cat.itacademy.s04.s02.n01.fruit.application.repository.FruitRepository;
import cat.itacademy.s04.s02.n01.fruit.application.repository.JPAFruitRepositoryImpl;
import cat.itacademy.s04.s02.n01.fruit.application.usecases.RegisterFruitUseCase;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import cat.itacademy.s04.s02.n01.common.domain.value_object.Name;
import cat.itacademy.s04.s02.n01.fruit.domain.value_object.Weight;
import cat.itacademy.s04.s02.n01.provider.application.repository.JPAProviderRepositoryImpl;
import cat.itacademy.s04.s02.n01.provider.application.repository.ProviderRepository;
import cat.itacademy.s04.s02.n01.provider.controller.exception.ProviderNotFoundException;
import cat.itacademy.s04.s02.n01.provider.domain.value_object.Country;
import cat.itacademy.s04.s02.n01.provider.domain.model.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegisterFruitServiceTest {
    private RegisterFruitUseCase createFruitUseCase;
    private FruitRepository fruitRepository;
    private ProviderRepository providerRepository;

    private static final String PROVIDER_NAME = "Fruit Provider";
    private static final String COUNTRY = "US";
    private static final Long PROVIDER_ID = 1L;
    private static final Provider PROVIDER = new Provider(PROVIDER_ID, Name.of(PROVIDER_NAME), Country.of(COUNTRY));
    private static final String KG_MAGNITUDE = "KILOGRAMS";

    private static final Long FRUIT_ID = 123L;
    private static final String NAME = "Fruit Name";
    private static final Double WEIGHT = 12.0;
    private static final Name FRUIT_NAME = Name.of(NAME);
    private static final Weight WEIGHT_IN_KG = Weight.inKiloGrams(WEIGHT);
    private static final Fruit FRUIT = new Fruit(FRUIT_ID, FRUIT_NAME, WEIGHT_IN_KG, PROVIDER);

    @BeforeEach
    void setUp(){

        fruitRepository = mock(JPAFruitRepositoryImpl.class);
        providerRepository = mock(JPAProviderRepositoryImpl.class);
        createFruitUseCase = new RegisterFruitService(fruitRepository, providerRepository);
    }

    @Test
    void createFruit_successfully() {
        when(fruitRepository.saveFruit(any(Fruit.class))).thenReturn(FRUIT);
        when(providerRepository.getProviderById(PROVIDER_ID)).thenReturn(Optional.of(PROVIDER));

        Fruit fruit = createFruitUseCase.registerFruit(NAME, WEIGHT, KG_MAGNITUDE, PROVIDER_ID);
        assertNotNull(fruit);
        assertEquals(NAME, fruit.getName().name());
        assertEquals(WEIGHT, fruit.getWeightInKg().amount());
        assertEquals(PROVIDER, fruit.getProvider());

        verify(providerRepository).getProviderById(PROVIDER_ID);
        verify(fruitRepository).saveFruit(any(Fruit.class));
    }

    @Test
    void createFruit_throwsProviderNotFoundException() {
        when(providerRepository.getProviderById(PROVIDER_ID)).thenReturn(Optional.empty());

        assertThrows(ProviderNotFoundException.class, () -> {createFruitUseCase.registerFruit(NAME, WEIGHT, KG_MAGNITUDE, PROVIDER_ID);});

        verify(providerRepository).getProviderById(PROVIDER_ID);
        verifyNoInteractions(fruitRepository);
    }
}