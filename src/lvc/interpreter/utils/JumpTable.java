package lvc.interpreter.utils;

import lvc.interpreter.Token;
import lvc.interpreter.exception.SyntaxException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Utility class to precompute jump positions for loop operations in Brainfuck code.
 * This improves the execution performance by avoiding linear searches for matching brackets.
 */
public class JumpTable {
    
    private final Map<Integer, Integer> jumpForward;  // Maps [ positions to matching ] positions
    private final Map<Integer, Integer> jumpBackward; // Maps ] positions to matching [ positions
    
    /**
     * Constructs a jump table for the given list of tokens.
     * 
     * @param tokens The list of tokens representing a Brainfuck program
     * @throws SyntaxException if there are unmatched brackets in the program
     */
    public JumpTable(ArrayList<Token> tokens) {
        jumpForward = new HashMap<>();
        jumpBackward = new HashMap<>();
        
        Stack<Integer> loopStartStack = new Stack<>();
        
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            
            if (token == Token.START_LOOP) {
                loopStartStack.push(i);
            } else if (token == Token.END_LOOP) {
                if (loopStartStack.isEmpty()) {
                    throw new SyntaxException("Unmatched closing bracket ']' at position " + i, i);
                }
                
                int loopStart = loopStartStack.pop();
                jumpForward.put(loopStart, i);
                jumpBackward.put(i, loopStart);
            }
        }
        
        if (!loopStartStack.isEmpty()) {
            throw new SyntaxException("Unmatched opening bracket '[' at position " + loopStartStack.peek(), 
                    loopStartStack.peek());
        }
    }
    
    /**
     * Get the position to jump to when encountering a START_LOOP token with zero at data pointer.
     * 
     * @param position The current position in the token list
     * @return The position to jump to (after the matching END_LOOP)
     */
    public int getJumpForward(int position) {
        return jumpForward.getOrDefault(position, -1);
    }
    
    /**
     * Get the position to jump to when encountering an END_LOOP token with non-zero at data pointer.
     * 
     * @param position The current position in the token list
     * @return The position to jump to (to the matching START_LOOP)
     */
    public int getJumpBackward(int position) {
        return jumpBackward.getOrDefault(position, -1);
    }
}