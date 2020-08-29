package pl.edu.wat.wcy.isi.app.configuration.exception;

public class SizeException extends Exception {

    public SizeException(String message) {
        super(message);
    }

    public SizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SizeException(Throwable cause) {
        super(cause);
    }
}
