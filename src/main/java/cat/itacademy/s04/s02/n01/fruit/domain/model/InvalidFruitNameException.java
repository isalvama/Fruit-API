package cat.itacademy.s04.s02.n01.fruit.domain.model;

public class InvalidFruitNameException extends DomainException {
    public InvalidFruitNameException(String message) {
        super(message);
    }
}
