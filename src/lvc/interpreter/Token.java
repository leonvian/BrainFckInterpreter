package lvc.interpreter;

/**
 * Enum representing the Brainfuck language commands.
 */
public enum Token {
    INCREMENT('+'),      // Increment the value at the current cell
    DECREMENT('-'),      // Decrement the value at the current cell
    MOVE_POINT_RIGHT('>'), // Move the data pointer right
    MOVE_POINT_LEFT('<'), // Move the data pointer left
    PRINT('.'),          // Print the character at the current cell
    INPUT(','),          // Read a character from input and store it in the current cell
    START_LOOP('['),     // Start a loop
    END_LOOP(']');       // End a loop
    
    private final char symbol;
    
    Token(char symbol) {
        this.symbol = symbol;
    }
    
    public char getSymbol() {
        return symbol;
    }
    
    /**
     * Get a Token from its character representation.
     * 
     * @param c The character to convert to a token
     * @return The corresponding Token, or null if the character is not a valid Brainfuck command
     */
    public static Token fromChar(char c) {
        for (Token token : Token.values()) {
            if (token.symbol == c) {
                return token;
            }
        }
        return null;
    }
}