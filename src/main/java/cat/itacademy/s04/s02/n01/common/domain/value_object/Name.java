package cat.itacademy.s04.s02.n01.common.domain.value_object;
import cat.itacademy.s04.s02.n01.fruit.domain.exception.InvalidNameException;

import java.util.Arrays;
import java.util.stream.Collectors;

public record Name(String name) {
    public Name {
        validate(name);
        name = format(name);
    }

    private void validate(String name){
        if (name == null) {
            throw new InvalidNameException("Name can not be null");
        }
        if (name.isBlank()) {
            throw new InvalidNameException("Name can not be blank");
        }
        if (name.length() < 2 || name.length() > 100){
            throw new InvalidNameException("Name can not have less than 2 letters or more than 100");
        }
    }

    private String format (String name){
        return Arrays.stream(name.split(" ")).map(word -> word.substring(0, 1).toUpperCase()+word.substring(1).toLowerCase()).collect(Collectors.joining(" "));
    }

    public static Name of(String name){
        return new Name(name);
    }
}
