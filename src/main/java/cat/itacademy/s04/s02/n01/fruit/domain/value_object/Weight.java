package cat.itacademy.s04.s02.n01.fruit.domain.value_object;

import cat.itacademy.s04.s02.n01.fruit.domain.exception.InvalidWeightException;

public record Weight(double amount, Magnitude magnitude) {

    public Weight {
        if (magnitude == null) {throw new InvalidWeightException("Weight's magnitude can not be null");}
        magnitude.validateMaxLimit(amount);
    }

    public Weight convertToKgWeight(){
        return new Weight(magnitude.convertToKg(this.amount), Magnitude.KILOGRAMS);
    }

    public static Weight toDomainWeight(double weightAmount, Magnitude magnitude) {
        return new Weight(weightAmount, magnitude);
    }

    public static Weight inKiloGrams(double amount){
        return new Weight(amount, Magnitude.KILOGRAMS);
    }

    public static Weight inPounds(double amount){
        return new Weight(amount, Magnitude.POUNDS);
    }
}
