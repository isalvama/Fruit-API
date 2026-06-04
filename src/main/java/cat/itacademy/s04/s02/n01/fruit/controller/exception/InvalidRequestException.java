package cat.itacademy.s04.s02.n01.fruit.controller.exception;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}
