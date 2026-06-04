package cat.itacademy.s04.s02.n01.fruit.controller;

import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;

public record CreateFruitResponseDTO (Long id, String name, double weightInKg){

    public static CreateFruitResponseDTO from (Fruit fruit ){
        return new CreateFruitResponseDTO(
                fruit.getId(),
                fruit.getName().name(),
                fruit.getWeight().amount()
        );
    }


}
