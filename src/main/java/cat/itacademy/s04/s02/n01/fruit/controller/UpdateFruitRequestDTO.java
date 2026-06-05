package cat.itacademy.s04.s02.n01.fruit.controller;

import cat.itacademy.s04.s02.n01.fruit.controller.exception.InvalidRequestException;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Magnitude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UpdateFruitRequestDTO(
        @NotBlank (message = "Name cannot be blank")
        String name,
        @Positive(message = "Weight Amount cannot be negative")
        Double weightAmount,
        String magnitude
) {
        public UpdateFruitRequestDTO{
                if (weightAmount != null && (magnitude == null || magnitude.isBlank())){
                        throw new InvalidRequestException("Magnitude is required when providing weight amount");
                }

                if ((magnitude != null && !magnitude.isBlank()) && weightAmount == null){
                        throw new InvalidRequestException("Weight Amount is required when providing a magnitude");
                }

                if ((magnitude != null && !magnitude.isBlank())){
                        validateMagnitude(magnitude);
                }
        }

        private void validateMagnitude(String magnitude){
                boolean isValid = Magnitude.validateMagnitude(magnitude);
                if (!isValid){
                        throw new InvalidRequestException("Magnitude is not valid");
                }
        }
}
