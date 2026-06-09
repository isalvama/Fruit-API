package cat.itacademy.s04.s02.n01.provider.domain.model;

import cat.itacademy.s04.s02.n01.provider.domain.exception.InvalidCountryException;

public record Country(String name) {
    public Country {
        validate(name);
        name = name.toUpperCase();
    }

    private void validate(String name){
        if (name == null) {
            throw new InvalidCountryException("Country can not be null");
        }
        if (name.isBlank()) {
            throw new InvalidCountryException("Country can not be blank");
        }
        if (name.length() != 2){
            throw new InvalidCountryException("Country should have 2 letters");
        }
    }

    public static Country of(String name){
        return new Country(name);
    }
}
