package cat.itacademy.s04.s02.n01.fruit;

public class Fruit {
    private final Long id;
    private final String name;
    private int weightInKilos;

    public Fruit(Long id, String name, int weightInKilos) {
        this.id = id;
        this.name = name;
        this.weightInKilos = weightInKilos;
    }
}
