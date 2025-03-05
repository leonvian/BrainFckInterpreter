package lvc.interpreter;

import lvc.interpreter.exception.BrainfuckException;
import lvc.interpreter.utils.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Main class for the Brainfuck interpreter.
 * Provides command-line interface for executing Brainfuck programs.
 */
public class Main {

    private static final String HELP_MESSAGE = """
            Brainfuck Interpreter
            Usage: java -jar brainfuck.jar [options] [file]
            
            Options:
              -h, --help           Show this help message
              -c, --code <code>    Execute Brainfuck code directly
              -m, --memory <size>  Set memory size (default: 30000)
              -d, --debug          Enable debug mode
              -n, --no-optimize    Disable code optimization
              -i, --interactive    Run in interactive mode
            
            Examples:
              java -jar brainfuck.jar program.bf
              java -jar brainfuck.jar -c "++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.>."
              java -jar brainfuck.jar -i
            """;

    public static void main(String[] args) {
        Configuration config = new Configuration();
        String code = null;
        String filePath = null;
        boolean interactiveMode = false;
        
        // Parse command line arguments
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-h", "--help" -> {
                    System.out.println(HELP_MESSAGE);
                    return;
                }
                case "-c", "--code" -> {
                    if (i + 1 < args.length) {
                        code = args[++i];
                    } else {
                        System.err.println("Error: Missing code argument");
                        return;
                    }
                }
                case "-m", "--memory" -> {
                    if (i + 1 < args.length) {
                        try {
                            int memorySize = Integer.parseInt(args[++i]);
                            config.setMemorySize(memorySize);
                        } catch (NumberFormatException e) {
                            System.err.println("Error: Invalid memory size");
                            return;
                        }
                    } else {
                        System.err.println("Error: Missing memory size argument");
                        return;
                    }
                }
                case "-d", "--debug" -> config.setDebugMode(true)
                case "-n", "--no-optimize" -> config.setOptimizationEnabled(false)
                case "-i", "--interactive" -> interactiveMode = true
                default -> {
                    if (!args[i].startsWith("-")) {
                        filePath = args[i];
                    } else {
                        System.err.println("Error: Unknown option " + args[i]);
                        return;
                    }
                }
            }
        }
        
        Interpreter interpreter = new Interpreter(config);
        
        try {
            if (interactiveMode) {
                runInteractiveMode(interpreter);
            } else if (code != null) {
                interpreter.execute(code);
            } else if (filePath != null) {
                String fileContent = readFile(filePath);
                interpreter.execute(fileContent);
            } else {
                // Default example if no arguments provided
                System.out.println("Running default example program (outputs 'HI'):");
                interpreter.execute("+++++[>+++++++++++++<-]>.>+++++[>+++++++++++++<-]>+.");
                System.out.println("\n\nUse --help to see available options.");
            }
        } catch (BrainfuckException e) {
            System.err.println("Error: " + e.getMessage());
            if (config.isDebugMode()) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
    
    /**
     * Run the interpreter in interactive mode.
     *
     * @param interpreter The interpreter instance
     */
    private static void runInteractiveMode(Interpreter interpreter) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Brainfuck Interpreter Interactive Mode");
        System.out.println("Type 'exit' to quit, 'help' for commands");
        
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                break;
            } else if (input.equalsIgnoreCase("help")) {
                System.out.println("Available commands:");
                System.out.println("  exit, quit - Exit the interpreter");
                System.out.println("  help       - Show this help message");
                System.out.println("  load <file> - Load and execute a Brainfuck file");
                System.out.println("  memory     - Display memory info");
                System.out.println("  reset      - Reset the interpreter state");
                System.out.println("  Any other input will be executed as Brainfuck code");
            } else if (input.startsWith("load ")) {
                String fileName = input.substring(5).trim();
                try {
                    String fileContent = readFile(fileName);
                    System.out.println("Executing file: " + fileName);
                    interpreter.execute(fileContent);
                    System.out.println(); // Add newline after execution
                } catch (IOException e) {
                    System.err.println("Error reading file: " + e.getMessage());
                }
            } else if (input.equals("memory")) {
                System.out.println("Data pointer: " + interpreter.getDataPointer());
                byte[] memory = interpreter.getMemory();
                System.out.println("Memory size: " + memory.length + " cells");
                System.out.println("First 10 memory cells: ");
                for (int i = 0; i < Math.min(10, memory.length); i++) {
                    System.out.printf("Cell %d: %d (ASCII: %c)%n", i, memory[i], (char) memory[i]);
                }
            } else if (input.equals("reset")) {
                interpreter.reset();
                System.out.println("Interpreter state reset");
            } else if (!input.isEmpty()) {
                try {
                    interpreter.execute(input);
                    System.out.println(); // Add newline after execution
                } catch (BrainfuckException e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }
        }
        
        scanner.close();
    }
    
    /**
     * Reads the content of a file.
     *
     * @param filePath The path to the file
     * @return The content of the file as a string
     * @throws IOException if there is an error reading the file
     */
    private static String readFile(String filePath) throws IOException {
        return Files.readString(Paths.get(filePath));
    }
}