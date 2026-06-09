package cat.itacademy.s04.s02.n01.fruit.controller.exception;

public class FruitNotFoundException extends RuntimeException {
    public FruitNotFoundException(String message) {
        super("Fruit Not Found: " + message);
    }
}
