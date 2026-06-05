package cat.itacademy.s04.s02.n01.fruit.controller;

import cat.itacademy.s04.s02.n01.fruit.controller.exception.InvalidRequestException;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Magnitude;
import jakarta.validation.constraints.*;

public record CreateFruitRequestDTO(
        @NotNull
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,
        @Positive (message = "Weight Amount cannot be negative")
        double weightAmount,
        @NotBlank(message = "Magnitude cannot be null or blank")
        String magnitude
) {
    public CreateFruitRequestDTO {
        validateMagnitude(magnitude);
        validateName(name);
    }

    private void validateMagnitude(String magnitude) {
        boolean isValid = Magnitude.validateMagnitude(magnitude);
        if (!isValid) {
            throw new InvalidRequestException("Magnitude is not valid");
        }
    }

    private void validateName(String name) {
        if (name.isBlank()){
            throw new InvalidRequestException("Name can not be blank");
        }
    }
}
