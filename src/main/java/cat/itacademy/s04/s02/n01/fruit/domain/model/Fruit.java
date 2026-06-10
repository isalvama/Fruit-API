package cat.itacademy.s04.s02.n01.fruit.domain.model;

import cat.itacademy.s04.s02.n01.common.domain.value_object.Name;
import cat.itacademy.s04.s02.n01.fruit.domain.value_object.Weight;
import cat.itacademy.s04.s02.n01.provider.domain.model.Provider;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Fruit {
    private final Long id;
    private Name name;
    private Weight weightInKg;
    private Provider provider;

    public Fruit(Long id, Name name, Weight weightInKg, Provider provider) {
        this.id = id;
        this.name = name;
        this.weightInKg = weightInKg;
        this.provider = provider;
    }

    public static Fruit create (Name name, Weight weightInKg, Provider provider){
        return new Fruit(
                null,
                name,
                weightInKg,
                provider
        );
    }
}
