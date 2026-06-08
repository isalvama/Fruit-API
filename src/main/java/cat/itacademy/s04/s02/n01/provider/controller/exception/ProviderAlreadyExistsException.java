package cat.itacademy.s04.s02.n01.provider.controller.exception;

public class ProviderAlreadyExistsException extends RuntimeException {
    public ProviderAlreadyExistsException(String message) {
        super("Provider Already Exists: " + message);
    }
}
