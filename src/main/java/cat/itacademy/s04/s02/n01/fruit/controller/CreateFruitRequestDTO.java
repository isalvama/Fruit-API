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
        @NotNull(message = "Magnitude cannot be null")
        String magnitude
) {
    public CreateFruitRequestDTO{
        validateMagnitude(magnitude);
    }

    public void validateMagnitude(String magnitude){
        boolean isValid = Arrays.stream(Magnitude.values())
                .anyMatch(m -> magnitude.equalsIgnoreCase(m.name()));
        if (!isValid) {
            throw new InvalidRequestException("Magnitude is not valid");
        }
    }
}