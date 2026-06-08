package cat.itacademy.s04.s02.n01.fruit.controller;

import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;

import java.util.List;

public record FruitResponseDTO(Long id, String name, Double weightInKg, Long ProviderId){

    public static FruitResponseDTO from (Fruit fruit){
        return new FruitResponseDTO(
                fruit.getId(),
                fruit.getName().name(),
                fruit.getWeightInKg().amount(),
                fruit.getProvider().getId()
        );
    }

    public static List<FruitResponseDTO> from (List<Fruit> fruits){
        return fruits.stream().map(FruitResponseDTO::from).toList();
    }
}
