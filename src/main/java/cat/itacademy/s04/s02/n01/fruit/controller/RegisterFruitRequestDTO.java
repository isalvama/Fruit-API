package cat.itacademy.s04.s02.n01.fruit.controller;

import cat.itacademy.s04.s02.n01.fruit.controller.exception.InvalidRequestException;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Magnitude;
import jakarta.validation.constraints.*;

public record RegisterFruitRequestDTO(
        @NotNull
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,
        @NotNull
        @Positive (message = "Weight Amount cannot be negative")
        Double weightAmount,
        @NotBlank(message = "Magnitude cannot be null or blank")
        String magnitude,
        @NotNull (message = "providerId cannot be null")
        @Positive (message = "providerId cannot be negative")
        Long providerId
) {
    public RegisterFruitRequestDTO {
        if (magnitude == null) throw new InvalidRequestException("Magnitude is required");
        validateMagnitude(magnitude);
        validateName(name);
        validateAmount(weightAmount, magnitude);
    }

    private void validateMagnitude(String magnitude) {
        boolean isValid = Magnitude.validateMagnitude(magnitude);
        if (!isValid) {
            throw new InvalidRequestException("Magnitude is not valid");
        }
    }
    private void validateAmount(double weightAmount, String magnitude) {
        Magnitude.fromString(magnitude).validateMaxLimit(weightAmount);
    }

    private void validateName(String name) {
        if (name.isBlank()){
            throw new InvalidRequestException("Name can not be blank");
        }
    }
}
