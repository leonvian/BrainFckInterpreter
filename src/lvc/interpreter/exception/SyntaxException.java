package lvc.interpreter.exception;

/**
 * Exception thrown when there is a syntax error in the Brainfuck code.
 */
public class SyntaxException extends BrainfuckException {
    
    private final int position;
    
    public SyntaxException(String message, int position) {
        super(message);
        this.position = position;
    }
    
    public int getPosition() {
        return position;
    }
}