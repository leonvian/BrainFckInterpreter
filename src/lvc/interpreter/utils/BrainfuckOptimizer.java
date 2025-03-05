package lvc.interpreter.utils;

import lvc.interpreter.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Optimizes Brainfuck code by combining consecutive identical operations.
 */
public class BrainfuckOptimizer {
    
    /**
     * Combines consecutive increment/decrement and move operations to reduce the number of operations.
     * This implementation preserves loop structures and only optimizes linear sequences.
     * 
     * @param tokens The original token list
     * @return A new optimized token list with count information
     */
    public static List<TokenCount> optimize(ArrayList<Token> tokens) {
        List<TokenCount> optimized = new ArrayList<>();
        
        if (tokens.isEmpty()) {
            return optimized;
        }
        
        Token currentToken = tokens.get(0);
        int count = 1;
        
        for (int i = 1; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            
            // Only optimize +, -, <, > operations (not loops or I/O)
            if (token == currentToken && 
                    (token == Token.INCREMENT || token == Token.DECREMENT || 
                     token == Token.MOVE_POINT_LEFT || token == Token.MOVE_POINT_RIGHT)) {
                count++;
            } else {
                optimized.add(new TokenCount(currentToken, count));
                currentToken = token;
                count = 1;
            }
        }
        
        // Add the last token
        optimized.add(new TokenCount(currentToken, count));
        
        return optimized;
    }
    
    /**
     * Represents a token with a count of how many times it should be executed.
     */
    public static class TokenCount {
        private final Token token;
        private final int count;
        
        public TokenCount(Token token, int count) {
            this.token = token;
            this.count = count;
        }
        
        public Token getToken() {
            return token;
        }
        
        public int getCount() {
            return count;
        }
    }
}