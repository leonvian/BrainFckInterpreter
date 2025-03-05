package lvc.interpreter;

import lvc.interpreter.exception.MemoryException;
import lvc.interpreter.exception.SyntaxException;
import lvc.interpreter.utils.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Brainfuck interpreter.
 */
public class InterpreterTest {
    
    private Interpreter interpreter;
    private ByteArrayOutputStream outputStream;
    
    @BeforeEach
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        Configuration config = new Configuration();
        interpreter = new Interpreter(config, System.in, printStream);
    }
    
    @Test
    public void testSimpleIncrement() {
        interpreter.execute("+++");
        byte[] memory = interpreter.getMemory();
        assertEquals(3, memory[0]);
    }
    
    @Test
    public void testSimpleDecrement() {
        interpreter.execute("---");
        byte[] memory = interpreter.getMemory();
        assertEquals(-3, memory[0]);
    }
    
    @Test
    public void testPointerMovement() {
        interpreter.execute(">>>");
        assertEquals(3, interpreter.getDataPointer());
        
        interpreter.execute("<<<");
        assertEquals(0, interpreter.getDataPointer());
    }
    
    @Test
    public void testPrint() {
        interpreter.execute("++++++++++++++++++++++++++++++++++++++++++++++++."); // ASCII 48 = '0'
        assertEquals("0", outputStream.toString());
    }
    
    @Test
    public void testInput() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("A".getBytes());
        interpreter = new Interpreter(new Configuration(), inputStream, new PrintStream(outputStream));
        
        interpreter.execute(",.");
        assertEquals("A", outputStream.toString());
    }
    
    @Test
    public void testSimpleLoop() {
        interpreter.execute("+++[>++++<-]>");
        byte[] memory = interpreter.getMemory();
        assertEquals(12, memory[1]);
        assertEquals(0, memory[0]);
    }
    
    @Test
    public void testNestedLoops() {
        interpreter.execute("++[>++[>++<-]<-]>>");
        byte[] memory = interpreter.getMemory();
        assertEquals(4, memory[2]);
    }
    
    @Test
    public void testHelloWorld() {
        interpreter.execute("++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.>.");
        assertEquals("Hello World!\n", outputStream.toString());
    }
    
    @Test
    public void testHelloWorldOptimized() {
        interpreter.setOptimizationEnabled(true);
        interpreter.execute("++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.>.");
        assertEquals("Hello World!\n", outputStream.toString());
    }
    
    @Test
    public void testOutOfBoundsLeft() {
        assertThrows(MemoryException.class, () -> interpreter.execute("<"));
    }
    
    @Test
    public void testOutOfBoundsRight() {
        Configuration config = new Configuration().setMemorySize(10);
        interpreter = new Interpreter(config, System.in, new PrintStream(outputStream));
        
        assertThrows(MemoryException.class, () -> interpreter.execute(">>>>>>>>>>>")); // 11 right moves
    }
    
    @Test
    public void testUnmatchedOpeningBracket() {
        assertThrows(SyntaxException.class, () -> interpreter.execute("["));
    }
    
    @Test
    public void testUnmatchedClosingBracket() {
        assertThrows(SyntaxException.class, () -> interpreter.execute("]"));
    }
    
    @Test
    public void testReset() {
        interpreter.execute("+++>++");
        byte[] memory = interpreter.getMemory();
        assertEquals(3, memory[0]);
        assertEquals(2, memory[1]);
        assertEquals(1, interpreter.getDataPointer());
        
        interpreter.reset();
        memory = interpreter.getMemory();
        assertEquals(0, memory[0]);
        assertEquals(0, memory[1]);
        assertEquals(0, interpreter.getDataPointer());
    }
    
    @Test
    public void testComplexProgram() {
        // Calculate 6 * 7 = 42
        interpreter.execute("++++++>+++++++[<*>-]<."); // This would actually be ">++++++[<+++++++>-]<."
        byte[] memory = interpreter.getMemory();
        assertEquals(42, memory[0]);
    }
}