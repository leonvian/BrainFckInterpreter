package lvc.interpreter.exception;

/**
 * Exception thrown when a memory access error occurs during Brainfuck program execution.
 */
public class MemoryException extends BrainfuckException {
    
    private final int dataPointer;
    
    public MemoryException(String message, int dataPointer) {
        super(message);
        this.dataPointer = dataPointer;
    }
    
    public int getDataPointer() {
        return dataPointer;
    }
}