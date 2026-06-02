package cat.itacademy.s04.s02.n01.fruit.domain.model;

import cat.itacademy.s04.s02.n01.fruit.domain.exception.InvalidWeightException;
import lombok.Getter;

public enum Magnitude {
    KILOGRAMS("Kg", 600),
    POUNDS("Lbs", 1322);

    @Getter
    private final String symbol;
    private final double maxLimit;

    Magnitude(String symbol, double maxLimit) {
        this.symbol = symbol;
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

}
