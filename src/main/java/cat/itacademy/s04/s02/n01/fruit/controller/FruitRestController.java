package cat.itacademy.s04.s02.n01.fruit.controller;

import cat.itacademy.s04.s02.n01.fruit.application.usecases.*;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/fruits")
@RequiredArgsConstructor
@Validated
public class FruitRestController {

    private final RegisterFruitUseCase registerFruitUseCase;
    private final GetAllFruitsUseCase getAllFruitsUseCase;
    private final GetFruitByIdUseCase getFruitByIdUseCase;
    private final GetFruitsByProviderIdUseCase getFruitsByProviderIdUseCase;
    private final UpdateFruitByIdUseCase updateFruitByIdUseCase;
    private final DeleteFruitByIdUseCase deleteFruitByIdUseCase;

    @PostMapping
    public ResponseEntity<FruitResponseDTO> registerFruit(@Valid @RequestBody RegisterFruitRequestDTO request){
        Fruit fruit = registerFruitUseCase.registerFruit(request.name(), request.weightAmount(), request.magnitude(), request.providerId());
        FruitResponseDTO fruitResponseDTO = FruitResponseDTO.from(fruit);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(fruit.getId())
                .toUri();
        return ResponseEntity.created(location).body(fruitResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<FruitResponseDTO>> getFruits(){
        List<Fruit> fruits = getAllFruitsUseCase.execute();
        List<FruitResponseDTO> fruitResponseDTOs = FruitResponseDTO.from(fruits);
        return ResponseEntity.ok().body(fruitResponseDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FruitResponseDTO> getFruitById(@PathVariable @Positive(message = "The ID must be a positive number") Long id){
        Fruit fruit = getFruitByIdUseCase.execute(id);
        FruitResponseDTO fruitResponseDTO = FruitResponseDTO.from(fruit);
        return ResponseEntity.ok(fruitResponseDTO);
    }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<FruitResponseDTO>> getFruitsByProviderId(@PathVariable @Positive(message = "The ID must be a positive number") Long providerId){
        List<Fruit> fruits = getFruitsByProviderIdUseCase.execute(providerId);
        List<FruitResponseDTO> fruitResponseDTOs = FruitResponseDTO.from(fruits);
        return ResponseEntity.ok().body(fruitResponseDTOs);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FruitResponseDTO> updateFruitById(@PathVariable @Positive(message = "The ID must be a positive number") Long id, @Valid @RequestBody UpdateFruitRequestDTO updateFruitRequestDTO){
        Fruit fruit = updateFruitByIdUseCase.execute(id, updateFruitRequestDTO);
        FruitResponseDTO fruitResponseDTO = FruitResponseDTO.from(fruit);
        return ResponseEntity.ok(fruitResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FruitResponseDTO> deleteFruitById(@PathVariable @Positive(message = "The ID must be a positive number") Long id){
        deleteFruitByIdUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
