package cat.itacademy.s04.s02.n01.fruit.controller;

import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateFruitDTO(
        @NotNull
        @NotBlank
        String name,
        @NotNull
        Double weight
) {
    public static CreateFruitDTO from (Fruit fruit){
        return new CreateFruitDTO(
                fruit.getName().toString(),
                fruit.getWeight().amount()
        );
    }
}
