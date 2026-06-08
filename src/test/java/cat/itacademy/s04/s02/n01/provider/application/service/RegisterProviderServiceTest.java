package cat.itacademy.s04.s02.n01.provider.application.service;

import cat.itacademy.s04.s02.n01.common.domain.value_object.Name;
import cat.itacademy.s04.s02.n01.fruit.application.repository.FruitRepository;
import cat.itacademy.s04.s02.n01.fruit.application.repository.JPAFruitRepositoryImpl;
import cat.itacademy.s04.s02.n01.fruit.application.service.CreateFruitService;
import cat.itacademy.s04.s02.n01.fruit.application.usecases.CreateFruitUseCase;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Weight;
import cat.itacademy.s04.s02.n01.provider.application.repository.JPAProviderRepositoryImpl;
import cat.itacademy.s04.s02.n01.provider.application.repository.ProviderRepository;
import cat.itacademy.s04.s02.n01.provider.application.usecase.RegisterProviderUseCase;
import cat.itacademy.s04.s02.n01.provider.controller.ProviderResponseDTO;
import cat.itacademy.s04.s02.n01.provider.controller.exception.ProviderAlreadyExistsException;
import cat.itacademy.s04.s02.n01.provider.domain.model.Country;
import cat.itacademy.s04.s02.n01.provider.domain.model.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegisterProviderServiceTest {
    private static final String PROVIDER_NAME = "Fruit Provider";
    private static final String COUNTRY = "US";
    private static final Long ID = 1L;

    private static final Provider PROVIDER = new Provider(ID, Name.of(PROVIDER_NAME), Country.of(COUNTRY));
    private static final ProviderResponseDTO PROVIDER_RESPONSE = new ProviderResponseDTO(ID, PROVIDER_NAME, COUNTRY);
    private ProviderRepository providerRepository;
    private RegisterProviderUseCase registerProviderUseCase;

    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp(){
        providerRepository = mock(JPAProviderRepositoryImpl.class);
        registerProviderUseCase = new RegisterProviderService(providerRepository);
    }

    @Test
    void registerProvider_providerWithSameNameDoesNotExist() {
        when(providerRepository.registerProvider(any(Provider.class))).thenReturn(PROVIDER);
        when(providerRepository.getProviderByName(PROVIDER_NAME)).thenReturn(Optional.empty());
        Provider provider = registerProviderUseCase.execute(PROVIDER_NAME, COUNTRY);

        assertNotNull(provider);
        assertEquals(PROVIDER_NAME, provider.getName().name());
        assertEquals(COUNTRY, provider.getCountry().name());

        verify(providerRepository).getProviderByName(PROVIDER_NAME);
        verify(providerRepository).registerProvider(any(Provider.class));
    }

    @Test
    void registerProvider_providerWithSameNameDoesExist() {
        when(providerRepository.registerProvider(any(Provider.class))).thenReturn(PROVIDER);
        when(providerRepository.getProviderByName(PROVIDER_NAME)).thenReturn(Optional.of(new Provider(23L, Name.of("Fruit Provider"), Country.of("ES"))));
        assertThrows(ProviderAlreadyExistsException.class, () -> registerProviderUseCase.execute(PROVIDER_NAME, COUNTRY));

        verify(providerRepository).getProviderByName(PROVIDER_NAME);
        verify(providerRepository, never()).registerProvider(any(Provider.class));

    }
}