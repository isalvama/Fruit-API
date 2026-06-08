package cat.itacademy.s04.s02.n01.provider.controller;

import cat.itacademy.s04.s02.n01.provider.application.usecase.RegisterProviderUseCase;
import cat.itacademy.s04.s02.n01.provider.application.usecase.UpdateProviderByIdUseCase;
import cat.itacademy.s04.s02.n01.provider.domain.model.Provider;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

    @RestController
    @RequestMapping("/api/providers")
    @RequiredArgsConstructor
    @Validated
    public class ProviderRestController {

        private final RegisterProviderUseCase registerProviderUseCase;
        private final UpdateProviderByIdUseCase updateProviderByIdUseCase;

        @PostMapping
        public ResponseEntity<ProviderResponseDTO> registerProvider(@Valid @RequestBody CreateProviderRequestDTO request){
            Provider provider = registerProviderUseCase.execute(request.name(), request.country());
            ProviderResponseDTO providerResponseDTO = ProviderResponseDTO.from(provider);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(provider.getId())
                    .toUri();
            return ResponseEntity.created(location).body(providerResponseDTO);
        }

        @PatchMapping("/{id}")
        public ResponseEntity<ProviderResponseDTO> updateFruitById(@PathVariable @Positive(message = "The ID must be a positive number") Long id, @Valid @RequestBody UpdateProviderRequestDTO updateProviderRequestDTO){
            Provider provider = updateProviderByIdUseCase.execute(id, updateProviderRequestDTO);
            ProviderResponseDTO providerResponseDTO = ProviderResponseDTO.from(provider);
            return ResponseEntity.ok(providerResponseDTO);
        }
    }
