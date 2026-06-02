package cat.itacademy.s04.s02.n01.fruit.controller;

import cat.itacademy.s04.s02.n01.fruit.application.usecases.CreateFruitUseCase;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Weight;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/fruits")
@RequiredArgsConstructor
@Validated
public class FruitRestController {

    private final CreateFruitUseCase createFruitUseCase;

    @PostMapping("/in-kg")
    public ResponseEntity<CreateFruitResponseDTO> createFruitInKg(@Valid @RequestBody CreateFruitDTO request){
        Fruit fruit = createFruitUseCase.createFruitInKg(request.name(), Weight.inKiloGrams(request.weight()));
        CreateFruitResponseDTO createFruitResponseDTO = CreateFruitResponseDTO.from(fruit);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(fruit.getId())
                .toUri();
        return ResponseEntity.created(location).body(createFruitResponseDTO);
    }

}
