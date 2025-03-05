package lvc.interpreter.utils;

/**
 * Configuration class for the Brainfuck interpreter.
 * Contains all configurable parameters of the interpreter.
 */
public class Configuration {
    
    /** Default memory size in cells */
    private static final int DEFAULT_MEMORY_SIZE = 30000;
    
    /** Default debug mode setting */
    private static final boolean DEFAULT_DEBUG_MODE = false;
    
    /** Default optimization setting */
    private static final boolean DEFAULT_OPTIMIZATION = true;
    
    private int memorySize;
    private boolean debugMode;
    private boolean optimizationEnabled;
    
    /**
     * Creates a new configuration with default settings.
     */
    public Configuration() {
        this.memorySize = DEFAULT_MEMORY_SIZE;
        this.debugMode = DEFAULT_DEBUG_MODE;
        this.optimizationEnabled = DEFAULT_OPTIMIZATION;
    }
    
    /**
     * Creates a new configuration with specified settings.
     *
     * @param memorySize Size of memory in cells
     * @param debugMode Whether to enable debug mode
     * @param optimizationEnabled Whether to enable code optimization
     */
    public Configuration(int memorySize, boolean debugMode, boolean optimizationEnabled) {
        this.memorySize = memorySize;
        this.debugMode = debugMode;
        this.optimizationEnabled = optimizationEnabled;
    }
    
    /**
     * Get the memory size in cells.
     * 
     * @return The memory size
     */
    public int getMemorySize() {
        return memorySize;
    }
    
    /**
     * Set the memory size in cells.
     * 
     * @param memorySize The new memory size
     * @return This configuration instance for method chaining
     */
    public Configuration setMemorySize(int memorySize) {
        this.memorySize = memorySize;
        return this;
    }
    
    /**
     * Check if debug mode is enabled.
     * 
     * @return True if debug mode is enabled, otherwise false
     */
    public boolean isDebugMode() {
        return debugMode;
    }
    
    /**
     * Enable or disable debug mode.
     * 
     * @param debugMode Whether to enable debug mode
     * @return This configuration instance for method chaining
     */
    public Configuration setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
        return this;
    }
    
    /**
     * Check if code optimization is enabled.
     * 
     * @return True if optimization is enabled, otherwise false
     */
    public boolean isOptimizationEnabled() {
        return optimizationEnabled;
    }
    
    /**
     * Enable or disable code optimization.
     * 
     * @param optimizationEnabled Whether to enable optimization
     * @return This configuration instance for method chaining
     */
    public Configuration setOptimizationEnabled(boolean optimizationEnabled) {
        this.optimizationEnabled = optimizationEnabled;
        return this;
    }
}