package cat.itacademy.s04.s02.n01.provider.domain.model;

import cat.itacademy.s04.s02.n01.common.domain.value_object.Name;
import cat.itacademy.s04.s02.n01.provider.domain.value_object.Country;
import lombok.Getter;
import lombok.Setter;

public class Provider {
    @Getter
    private final Long id;
    @Getter
    @Setter
    private Name name;
    @Setter
    @Getter
    private Country country;

    public Provider(Long id, Name name, Country country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public static Provider create(Name name, Country country) {
        return new Provider(
                null,
                name,
                country
        );
    }
}

