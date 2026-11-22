package service;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    } // Custom Exception TO DEAL WITH OUR BUSINUS LOGIC
}