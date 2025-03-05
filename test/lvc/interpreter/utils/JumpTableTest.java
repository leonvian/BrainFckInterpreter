package lvc.interpreter.utils;

import lvc.interpreter.Token;
import lvc.interpreter.exception.SyntaxException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the JumpTable class.
 */
public class JumpTableTest {
    
    @Test
    public void testSimpleLoop() {
        ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(Token.START_LOOP);
        tokens.add(Token.INCREMENT);
        tokens.add(Token.END_LOOP);
        
        JumpTable jumpTable = new JumpTable(tokens);
        
        assertEquals(2, jumpTable.getJumpForward(0));
        assertEquals(0, jumpTable.getJumpBackward(2));
    }
    
    @Test
    public void testNestedLoops() {
        ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(Token.START_LOOP);       // 0
        tokens.add(Token.INCREMENT);        // 1
        tokens.add(Token.START_LOOP);       // 2
        tokens.add(Token.INCREMENT);        // 3
        tokens.add(Token.END_LOOP);         // 4
        tokens.add(Token.DECREMENT);        // 5
        tokens.add(Token.END_LOOP);         // 6
        
        JumpTable jumpTable = new JumpTable(tokens);
        
        assertEquals(6, jumpTable.getJumpForward(0));
        assertEquals(0, jumpTable.getJumpBackward(6));
        assertEquals(4, jumpTable.getJumpForward(2));
        assertEquals(2, jumpTable.getJumpBackward(4));
    }
    
    @Test
    public void testUnmatchedOpeningBracket() {
        ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(Token.START_LOOP);
        tokens.add(Token.INCREMENT);
        
        assertThrows(SyntaxException.class, () -> new JumpTable(tokens));
    }
    
    @Test
    public void testUnmatchedClosingBracket() {
        ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(Token.INCREMENT);
        tokens.add(Token.END_LOOP);
        
        assertThrows(SyntaxException.class, () -> new JumpTable(tokens));
    }
    
    @Test
    public void testMultipleLoops() {
        ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(Token.START_LOOP);       // 0
        tokens.add(Token.INCREMENT);        // 1
        tokens.add(Token.END_LOOP);         // 2
        tokens.add(Token.START_LOOP);       // 3
        tokens.add(Token.DECREMENT);        // 4
        tokens.add(Token.END_LOOP);         // 5
        
        JumpTable jumpTable = new JumpTable(tokens);
        
        assertEquals(2, jumpTable.getJumpForward(0));
        assertEquals(0, jumpTable.getJumpBackward(2));
        assertEquals(5, jumpTable.getJumpForward(3));
        assertEquals(3, jumpTable.getJumpBackward(5));
    }
    
    @Test
    public void testEmptyTokenList() {
        ArrayList<Token> tokens = new ArrayList<>();
        
        JumpTable jumpTable = new JumpTable(tokens);
        
        assertEquals(-1, jumpTable.getJumpForward(0));
        assertEquals(-1, jumpTable.getJumpBackward(0));
    }
    
    @Test
    public void testNoLoops() {
        ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(Token.INCREMENT);
        tokens.add(Token.DECREMENT);
        tokens.add(Token.PRINT);
        
        JumpTable jumpTable = new JumpTable(tokens);
        
        assertEquals(-1, jumpTable.getJumpForward(0));
        assertEquals(-1, jumpTable.getJumpBackward(2));
    }
}