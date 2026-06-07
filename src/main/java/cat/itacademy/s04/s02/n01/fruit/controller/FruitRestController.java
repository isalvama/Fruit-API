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

    private final CreateFruitUseCase createFruitUseCase;
    private final GetAllFruitsUseCase getAllFruitsUseCase;
    private final GetFruitByIdUseCase getFruitByIdUseCase;
    private final UpdateFruitByIdUseCase updateFruitByIdUseCase;
    private final DeleteFruitByIdUseCase deleteFruitByIdUseCase;

    @PostMapping
    public ResponseEntity<FruitResponseDTO> createFruit(@Valid @RequestBody CreateFruitRequestDTO request){
        Fruit fruit = createFruitUseCase.createFruit(request.name(), request.weightAmount(), request.magnitude());
        FruitResponseDTO createFruitResponseDTO = FruitResponseDTO.from(fruit);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(fruit.getId())
                .toUri();
        return ResponseEntity.created(location).body(createFruitResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<FruitResponseDTO>> getFruits(){
        List<Fruit> fruits = getAllFruitsUseCase.execute();
        List<FruitResponseDTO> createFruitResponseDTOs = FruitResponseDTO.from(fruits);
        return ResponseEntity.ok().body(createFruitResponseDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FruitResponseDTO> getFruitById(@PathVariable @Positive(message = "The ID must be a positive number") Long id){
        Fruit fruit = getFruitByIdUseCase.execute(id);
        FruitResponseDTO fruitResponseDTO = FruitResponseDTO.from(fruit);
        return ResponseEntity.ok(fruitResponseDTO);
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
