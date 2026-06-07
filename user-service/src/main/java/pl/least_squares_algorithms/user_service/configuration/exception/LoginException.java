package pl.least_squares_algorithms.user_service.configuration.exception;

public class LoginException extends Exception {

    public LoginException(String message) {
        super(message);
    }

    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginException(Throwable cause) {
        super(cause);
    }
}
