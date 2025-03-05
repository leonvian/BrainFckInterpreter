package lvc.interpreter;

import lvc.interpreter.exception.SyntaxException;

import java.util.ArrayList;

/**
 * The Tokenizer class converts a Brainfuck source code string into tokens.
 * 
 * Brainfuck commands:
 * >    Move the data pointer to the right
 * <    Move the data pointer to the left
 * +    Increment the value at the data pointer
 * -    Decrement the value at the data pointer
 * .    Output the character represented by the value at the data pointer
 * ,    Input a character and store it at the data pointer
 * [    If the value at the data pointer is zero, jump to the matching ]
 * ]    If the value at the data pointer is nonzero, jump to the matching [
 */
public class Tokenizer {

    /**
     * Converts a Brainfuck source code string into a list of tokens.
     * Ignores any characters that are not valid Brainfuck commands.
     * 
     * @param code The Brainfuck source code
     * @return A list of tokens representing the Brainfuck program
     * @throws SyntaxException if there is a syntax error in the code
     */
    public static ArrayList<Token> createTokens(String code) {
        ArrayList<Token> tokens = new ArrayList<>();
        char[] array = code.toCharArray();
        
        for (int i = 0; i < array.length; i++) {
            Token token = Token.fromChar(array[i]);
            if (token != null) {
                tokens.add(token);
            }
        }
        
        // Validate balanced brackets
        validateBrackets(tokens);
        
        return tokens;
    }
    
    /**
     * Validates that all loop brackets are properly balanced.
     * 
     * @param tokens The list of tokens to check
     * @throws SyntaxException if brackets are not balanced
     */
    private static void validateBrackets(ArrayList<Token> tokens) {
        int bracketCount = 0;
        
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            
            if (token == Token.START_LOOP) {
                bracketCount++;
            } else if (token == Token.END_LOOP) {
                bracketCount--;
                
                if (bracketCount < 0) {
                    throw new SyntaxException("Unmatched closing bracket ']' at position " + i, i);
                }
            }
        }
        
        if (bracketCount > 0) {
            throw new SyntaxException("Unmatched opening bracket '[' detected", tokens.size() - 1);
        }
    }
}