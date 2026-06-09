package cat.itacademy.s04.s02.n01.provider.application.service;

import cat.itacademy.s04.s02.n01.common.domain.value_object.Name;
import cat.itacademy.s04.s02.n01.provider.application.repository.JPAProviderRepositoryImpl;
import cat.itacademy.s04.s02.n01.provider.application.repository.ProviderRepository;
import cat.itacademy.s04.s02.n01.provider.application.usecase.UpdateProviderByIdUseCase;
import cat.itacademy.s04.s02.n01.provider.controller.ProviderResponseDTO;
import cat.itacademy.s04.s02.n01.provider.controller.UpdateProviderRequestDTO;
import cat.itacademy.s04.s02.n01.provider.controller.exception.ProviderNotFoundException;
import cat.itacademy.s04.s02.n01.provider.domain.model.Country;
import cat.itacademy.s04.s02.n01.provider.domain.model.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateProviderByIdServiceTest {

        private UpdateProviderByIdUseCase updateProviderByIdUseCase;
        private ProviderRepository providerRepository;
    private static final String PROVIDER_NAME = "Fruit Provider";
    private static final String COUNTRY = "US";
    private static final Long ID = 1L;

    private static final Provider PROVIDER = new Provider(ID, Name.of(PROVIDER_NAME), Country.of(COUNTRY));
    private static final ProviderResponseDTO PROVIDER_RESPONSE = new ProviderResponseDTO(ID, PROVIDER_NAME, COUNTRY);


        @BeforeEach
        void setUp() {
            providerRepository = mock(JPAProviderRepositoryImpl.class);
            updateProviderByIdUseCase = new UpdateProviderByIdService(providerRepository);
        }

        @Test
        void updateProvider_newName() {
            final String newName = "New Name";
            UpdateProviderRequestDTO updateProviderRequestDTO = new UpdateProviderRequestDTO(newName, null);
            when(providerRepository.getProviderById(ID)).thenReturn(Optional.of(PROVIDER));
            when(providerRepository.saveProvider(any(Provider.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
            Provider provider = updateProviderByIdUseCase.execute(ID, updateProviderRequestDTO);
            assertNotNull(provider);
            assertEquals(newName, provider.getName().name());
            assertEquals(COUNTRY, provider.getCountry().name());

            verify(providerRepository).getProviderById(any());
            verify(providerRepository).saveProvider(any(Provider.class));
        }

        @Test
        void updateProvider_newCountry() {
            final String newCountry = "PA";
            UpdateProviderRequestDTO updateProviderRequestDTO = new UpdateProviderRequestDTO(null, newCountry);
            when(providerRepository.getProviderById(ID)).thenReturn(Optional.of(PROVIDER));
            when(providerRepository.saveProvider(any(Provider.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
            Provider provider = updateProviderByIdUseCase.execute(ID, updateProviderRequestDTO);
            assertNotNull(provider);
            assertEquals(PROVIDER_NAME, provider.getName().name());
            assertEquals(newCountry, provider.getCountry().name());

            verify(providerRepository).getProviderById(any());
            verify(providerRepository).saveProvider(any(Provider.class));
        }

        @Test
        void updateProvider_whenRepositoryReturnsEmptyOptional_throwsProviderNotFoundException() {
            final String newCountry = "PA";
            UpdateProviderRequestDTO updateProviderRequestDTO = new UpdateProviderRequestDTO(null, newCountry);
            when(providerRepository.getProviderById(ID)).thenReturn(Optional.empty());
            assertThrows(ProviderNotFoundException.class, () -> {updateProviderByIdUseCase.execute(ID, updateProviderRequestDTO);});

            verify(providerRepository).getProviderById(ID);
            verify(providerRepository, never()).saveProvider(any(Provider.class));
        }
    }
