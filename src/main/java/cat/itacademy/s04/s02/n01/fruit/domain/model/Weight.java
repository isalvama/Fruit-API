package cat.itacademy.s04.s02.n01.fruit.domain.model;

public record Weight(double amount, Magnitude magnitude) {

    public Weight {
        if (magnitude == null) {throw new InvalidWeightException("Weight's magnitude can not be null");}
        magnitude.validate(amount);
    }

    public static Weight inKiloGrams(double amount){
        return new Weight(amount, Magnitude.KILOGRAMS);
    }

    public static Weight inPounds(double amount){
        return new Weight(amount, Magnitude.POUNDS);
    }
}
