package lvc.interpreter.exception;

/**
 * Base exception class for all Brainfuck interpreter exceptions.
 */
public class BrainfuckException extends RuntimeException {
    
    public BrainfuckException(String message) {
        super(message);
    }
    
    public BrainfuckException(String message, Throwable cause) {
        super(message, cause);
    }
}