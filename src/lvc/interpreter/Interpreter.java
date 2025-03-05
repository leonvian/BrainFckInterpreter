package lvc.interpreter;

import lvc.interpreter.exception.MemoryException;
import lvc.interpreter.exception.SyntaxException;
import lvc.interpreter.utils.BrainfuckOptimizer;
import lvc.interpreter.utils.Configuration;
import lvc.interpreter.utils.JumpTable;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A Brainfuck language interpreter.
 * Executes Brainfuck programs according to the language specification.
 */
public class Interpreter {

    private final Configuration config;
    private byte[] memory;
    private ArrayList<Token> tokens;
    private JumpTable jumpTable;
    private final InputStream input;
    private final PrintStream output;
    
    private int dataPointer;
    private int instructionPointer;
    private boolean optimizationEnabled;
    private List<BrainfuckOptimizer.TokenCount> optimizedTokens;
    
    /**
     * Creates a new interpreter with default configuration.
     */
    public Interpreter() {
        this(new Configuration(), System.in, System.out);
    }
    
    /**
     * Creates a new interpreter with the specified configuration.
     *
     * @param config The configuration for the interpreter
     */
    public Interpreter(Configuration config) {
        this(config, System.in, System.out);
    }
    
    /**
     * Creates a new interpreter with the specified configuration and I/O streams.
     *
     * @param config The configuration for the interpreter
     * @param input The input stream for reading input (',' command)
     * @param output The output stream for printing output ('.' command)
     */
    public Interpreter(Configuration config, InputStream input, PrintStream output) {
        this.config = config;
        this.input = input;
        this.output = output;
        this.optimizationEnabled = config.isOptimizationEnabled();
        reset();
    }
    
    /**
     * Resets the interpreter state.
     */
    public void reset() {
        memory = new byte[config.getMemorySize()];
        dataPointer = 0;
        instructionPointer = 0;
    }
    
    /**
     * Executes the given Brainfuck code.
     *
     * @param code The Brainfuck code to execute
     * @throws SyntaxException if there's a syntax error in the code
     * @throws MemoryException if there's a memory access error during execution
     */
    public void execute(String code) {
        tokens = Tokenizer.createTokens(code);
        jumpTable = new JumpTable(tokens);
        
        if (optimizationEnabled) {
            optimizedTokens = BrainfuckOptimizer.optimize(tokens);
            executeOptimized();
        } else {
            executeStandard();
        }
    }
    
    /**
     * Executes the program using the standard (non-optimized) execution model.
     */
    private void executeStandard() {
        reset();
        int size = tokens.size();
        
        while (instructionPointer < size) {
            Token token = tokens.get(instructionPointer);
            
            switch (token) {
                case INCREMENT -> increment(1);
                case DECREMENT -> decrement(1);
                case MOVE_POINT_LEFT -> moveLeft(1);
                case MOVE_POINT_RIGHT -> moveRight(1);
                case PRINT -> print();
                case INPUT -> input();
                case START_LOOP -> startLoop();
                case END_LOOP -> endLoop();
            }
            
            instructionPointer++;
        }
    }
    
    /**
     * Executes the program using the optimized execution model.
     */
    private void executeOptimized() {
        reset();
        int size = optimizedTokens.size();
        
        for (instructionPointer = 0; instructionPointer < size; instructionPointer++) {
            BrainfuckOptimizer.TokenCount tokenCount = optimizedTokens.get(instructionPointer);
            Token token = tokenCount.getToken();
            int count = tokenCount.getCount();
            
            switch (token) {
                case INCREMENT -> increment(count);
                case DECREMENT -> decrement(count);
                case MOVE_POINT_LEFT -> moveLeft(count);
                case MOVE_POINT_RIGHT -> moveRight(count);
                case PRINT -> print();
                case INPUT -> input();
                case START_LOOP -> startLoop();
                case END_LOOP -> endLoop();
            }
        }
    }
    
    /**
     * Increments the value at the current data pointer by the specified amount.
     *
     * @param count The amount to increment by
     */
    private void increment(int count) {
        checkMemoryBounds();
        memory[dataPointer] += count;
    }
    
    /**
     * Decrements the value at the current data pointer by the specified amount.
     *
     * @param count The amount to decrement by
     */
    private void decrement(int count) {
        checkMemoryBounds();
        memory[dataPointer] -= count;
    }
    
    /**
     * Moves the data pointer to the right by the specified amount.
     *
     * @param count The number of cells to move
     */
    private void moveRight(int count) {
        dataPointer += count;
        checkMemoryBounds();
    }
    
    /**
     * Moves the data pointer to the left by the specified amount.
     *
     * @param count The number of cells to move
     */
    private void moveLeft(int count) {
        dataPointer -= count;
        checkMemoryBounds();
    }
    
    /**
     * Outputs the character represented by the value at the current data pointer.
     */
    private void print() {
        checkMemoryBounds();
        output.print((char) memory[dataPointer]);
    }
    
    /**
     * Reads a character from input and stores it at the current data pointer.
     */
    private void input() {
        checkMemoryBounds();
        try {
            memory[dataPointer] = (byte) input.read();
        } catch (IOException e) {
            memory[dataPointer] = 0;
        }
    }
    
    /**
     * Executes the start loop operation.
     * If the value at the current cell is zero, jumps to the matching end loop.
     */
    private void startLoop() {
        checkMemoryBounds();
        
        if (memory[dataPointer] == 0) {
            if (optimizationEnabled) {
                // Find the matching end loop in the optimized tokens
                int depth = 1;
                while (depth > 0 && instructionPointer < optimizedTokens.size() - 1) {
                    instructionPointer++;
                    Token token = optimizedTokens.get(instructionPointer).getToken();
                    if (token == Token.START_LOOP) {
                        depth++;
                    } else if (token == Token.END_LOOP) {
                        depth--;
                    }
                }
            } else {
                // Use jump table for standard execution
                instructionPointer = jumpTable.getJumpForward(instructionPointer);
            }
        }
    }
    
    /**
     * Executes the end loop operation.
     * If the value at the current cell is nonzero, jumps back to the matching start loop.
     */
    private void endLoop() {
        checkMemoryBounds();
        
        if (memory[dataPointer] != 0) {
            if (optimizationEnabled) {
                // Find the matching start loop in the optimized tokens
                int depth = 1;
                while (depth > 0 && instructionPointer > 0) {
                    instructionPointer--;
                    Token token = optimizedTokens.get(instructionPointer).getToken();
                    if (token == Token.END_LOOP) {
                        depth++;
                    } else if (token == Token.START_LOOP) {
                        depth--;
                    }
                }
                // Compensate for the increment in the main loop
                instructionPointer--;
            } else {
                // Use jump table for standard execution
                instructionPointer = jumpTable.getJumpBackward(instructionPointer);
            }
        }
    }
    
    /**
     * Checks if the data pointer is within valid memory bounds.
     *
     * @throws MemoryException if the data pointer is out of bounds
     */
    private void checkMemoryBounds() {
        if (dataPointer < 0 || dataPointer >= memory.length) {
            throw new MemoryException("Memory access out of bounds: " + dataPointer, dataPointer);
        }
    }
    
    /**
     * Get the current state of the memory.
     * This method is mainly used for debugging and testing.
     *
     * @return A copy of the current memory state
     */
    public byte[] getMemory() {
        byte[] memoryCopy = new byte[memory.length];
        System.arraycopy(memory, 0, memoryCopy, 0, memory.length);
        return memoryCopy;
    }
    
    /**
     * Get the current data pointer.
     * This method is mainly used for debugging and testing.
     *
     * @return The current data pointer
     */
    public int getDataPointer() {
        return dataPointer;
    }
    
    /**
     * Toggle optimization mode.
     *
     * @param enabled Whether optimization should be enabled
     */
    public void setOptimizationEnabled(boolean enabled) {
        this.optimizationEnabled = enabled;
    }
}