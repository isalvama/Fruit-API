package cat.itacademy.s04.s02.n01.fruit.domain.model;

import lombok.Getter;
import lombok.Setter;

public class Fruit {
    @Getter
    private final Long id;
    @Getter
    @Setter
    private FruitName name;
    @Setter
    private Weight weightInKg;

    public Fruit(Long id, FruitName name, Weight weightInKg) {
        this.id = id;
        this.name = name;
        this.weightInKg = weightInKg;
    }

    public static Fruit create (FruitName name, Weight weightInKg){
        return new Fruit(
                null,
                name,
                weightInKg
        );
    }

    public Weight getWeight() {
        return weightInKg;
    }
}
