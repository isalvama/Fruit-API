package cat.itacademy.s04.s02.n01.fruit.domain.model;

import cat.itacademy.s04.s02.n01.fruit.controller.exception.InvalidRequestException;
import cat.itacademy.s04.s02.n01.fruit.domain.exception.InvalidWeightException;
import lombok.Getter;

import java.util.Arrays;

public enum Magnitude {
    KILOGRAMS("Kg", 1.0, 600.0),
    POUNDS("Lbs", 0.453, 1322.0);

    @Getter
    private final String symbol;
    @Getter
    private final double toKgRatio;
    private final double maxLimit;

    Magnitude(String symbol, double toKgRatio, double maxLimit) {
        this.symbol = symbol;
        this.toKgRatio = toKgRatio;
        this.maxLimit = maxLimit;
    }

    public void validate(double amount) {
        if (amount <= 0) {
            throw new InvalidWeightException("Weight can not be negative");
        }
        if (amount > maxLimit) {
            throw new InvalidWeightException(String.format("Weight cannot be greater than %s %s", maxLimit, symbol));
        }
    }

    public double convertToKg(double amount){
        return amount * this.toKgRatio;
    }

    public static Magnitude fromString(String value){
        try {
            return Magnitude.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidWeightException("Magnitude is invalid");
        }
    }

    public static boolean validateMagnitude(String magnitude){
        return Arrays.stream(Magnitude.values())
                .anyMatch(m -> magnitude.equalsIgnoreCase(m.name()));
    }

}
