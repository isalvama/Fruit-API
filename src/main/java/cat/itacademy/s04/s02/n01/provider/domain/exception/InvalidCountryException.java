package cat.itacademy.s04.s02.n01.provider.domain.exception;

import cat.itacademy.s04.s02.n01.common.domain.exception.DomainException;

public class InvalidCountryException extends DomainException {
    public InvalidCountryException(String message) {
        super(message);
    }
}
