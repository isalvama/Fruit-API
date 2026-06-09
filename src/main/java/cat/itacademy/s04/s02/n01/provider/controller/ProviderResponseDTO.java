package cat.itacademy.s04.s02.n01.provider.controller;

import cat.itacademy.s04.s02.n01.provider.domain.model.Provider;

import java.util.List;

public record ProviderResponseDTO(
        Long id, String name, String country){

    public static ProviderResponseDTO from (Provider provider ){
        return new ProviderResponseDTO(
                provider.getId(),
                provider.getName().name(),
                provider.getCountry().name()
        );
    }

    public static List<ProviderResponseDTO> from (List<Provider> providers){
        return providers.stream().map(ProviderResponseDTO::from).toList();
    }
}
