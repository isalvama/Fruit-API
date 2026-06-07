package cat.itacademy.s04.s02.n01.fruit.domain.exception;

import cat.itacademy.s04.s02.n01.common.domain.exception.DomainException;

public class InvalidNameException extends DomainException {
    public InvalidNameException(String message) {
        super(message);
    }
}
