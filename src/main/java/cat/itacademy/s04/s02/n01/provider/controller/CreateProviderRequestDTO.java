package cat.itacademy.s04.s02.n01.provider.controller;

import cat.itacademy.s04.s02.n01.fruit.controller.exception.InvalidRequestException;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateProviderRequestDTO(

    @NotNull
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    String name,
    @NotNull
    @Pattern(regexp = "^[A-Z]{2}$", message = "Country must have only 2 letters")
    String country
) {
    public CreateProviderRequestDTO {
            validateName(name);
        }

        private void validateName(String name) {
            if (name.isBlank()) {
                throw new InvalidRequestException("Name can not be blank");
            }
        }
}
