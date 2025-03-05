package lvc.interpreter;

import lvc.interpreter.exception.SyntaxException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Tokenizer class.
 */
public class TokenizerTest {
    
    @Test
    public void testCreateTokens() {
        String code = "+-><.,[]";
        ArrayList<Token> tokens = Tokenizer.createTokens(code);
        
        assertEquals(8, tokens.size());
        assertEquals(Token.INCREMENT, tokens.get(0));
        assertEquals(Token.DECREMENT, tokens.get(1));
        assertEquals(Token.MOVE_POINT_RIGHT, tokens.get(2));
        assertEquals(Token.MOVE_POINT_LEFT, tokens.get(3));
        assertEquals(Token.PRINT, tokens.get(4));
        assertEquals(Token.INPUT, tokens.get(5));
        assertEquals(Token.START_LOOP, tokens.get(6));
        assertEquals(Token.END_LOOP, tokens.get(7));
    }
    
    @Test
    public void testIgnoreInvalidCharacters() {
        String code = "Hello, World! +-><";
        ArrayList<Token> tokens = Tokenizer.createTokens(code);
        
        assertEquals(4, tokens.size());
        assertEquals(Token.INCREMENT, tokens.get(0));
        assertEquals(Token.DECREMENT, tokens.get(1));
        assertEquals(Token.MOVE_POINT_RIGHT, tokens.get(2));
        assertEquals(Token.MOVE_POINT_LEFT, tokens.get(3));
    }
    
    @Test
    public void testEmptyCode() {
        String code = "";
        ArrayList<Token> tokens = Tokenizer.createTokens(code);
        
        assertTrue(tokens.isEmpty());
    }
    
    @Test
    public void testOnlyInvalidCharacters() {
        String code = "Hello, World!";
        ArrayList<Token> tokens = Tokenizer.createTokens(code);
        
        assertTrue(tokens.isEmpty());
    }
    
    @Test
    public void testUnmatchedOpeningBracket() {
        String code = "[++";
        
        assertThrows(SyntaxException.class, () -> Tokenizer.createTokens(code));
    }
    
    @Test
    public void testUnmatchedClosingBracket() {
        String code = "++]";
        
        assertThrows(SyntaxException.class, () -> Tokenizer.createTokens(code));
    }
    
    @Test
    public void testNestedBrackets() {
        String code = "[+[++]+]";
        ArrayList<Token> tokens = Tokenizer.createTokens(code);
        
        assertEquals(8, tokens.size());
        assertEquals(Token.START_LOOP, tokens.get(0));
        assertEquals(Token.INCREMENT, tokens.get(1));
        assertEquals(Token.START_LOOP, tokens.get(2));
        assertEquals(Token.INCREMENT, tokens.get(3));
        assertEquals(Token.INCREMENT, tokens.get(4));
        assertEquals(Token.END_LOOP, tokens.get(5));
        assertEquals(Token.INCREMENT, tokens.get(6));
        assertEquals(Token.END_LOOP, tokens.get(7));
    }
}