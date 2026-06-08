package cat.itacademy.s04.s02.n01.provider.controller.exception;

public class ProviderNotFoundException extends RuntimeException {
    public ProviderNotFoundException(String message) {
        super("Provider Not Found: " + message);
    }
}
