package pl.leastsquaresalgorithms.user.configuration.exception;

public class MessageException extends Exception {

    public MessageException(String message) {
        super(message);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageException(Throwable cause) {
        super(cause);
    }
}
