package cat.itacademy.s04.s02.n01.fruit.controller;

import cat.itacademy.s04.s02.n01.fruit.controller.exception.InvalidRequestException;
import cat.itacademy.s04.s02.n01.fruit.domain.exception.InvalidWeightException;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Magnitude;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Weight;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Arrays;

public record CreateFruitRequestDTO(
        @NotNull
        @NotBlank (message = "Name cannot be blank")
        String name,
        @Positive (message = "Weight Amount cannot be negative")
        double weightAmount,
        @NotBlank(message = "Magnitude cannot be null or blank")
        String magnitude
) {
    public CreateFruitRequestDTO {
        validateMagnitude(magnitude);
    }

    private void validateMagnitude(String magnitude) {
        Magnitude.validateMagnitude(magnitude);
    }
}
