# BrainFck Interpreter

A robust, optimized Java interpreter for the Brainfuck esoteric programming language.

## Features

- Full implementation of all Brainfuck commands, including input support
- Error handling with descriptive messages
- Configurable memory size
- Performance optimizations for faster execution
- Interactive mode for testing and learning
- Command-line interface with various options
- File loading capabilities for running existing Brainfuck programs
- Detailed documentation

## Usage

### Running the Interpreter

```bash
java -jar brainfuck.jar [options] [file]
```

### Command-Line Options

- `-h, --help`: Show help message
- `-c, --code <code>`: Execute Brainfuck code directly
- `-m, --memory <size>`: Set memory size (default: 30000)
- `-d, --debug`: Enable debug mode
- `-n, --no-optimize`: Disable code optimization
- `-i, --interactive`: Run in interactive mode

### Examples

Run a Brainfuck file:
```bash
java -jar brainfuck.jar program.bf
```

Execute code directly:
```bash
java -jar brainfuck.jar -c "++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.>."
```

Run in interactive mode:
```bash
java -jar brainfuck.jar -i
```

## Brainfuck Language

Brainfuck is a minimalist esoteric programming language with only 8 commands:

- `>`: Move the data pointer to the right
- `<`: Move the data pointer to the left
- `+`: Increment the value at the data pointer
- `-`: Decrement the value at the data pointer
- `.`: Output the character represented by the value at the data pointer
- `,`: Input a character and store it at the data pointer
- `[`: If the value at the data pointer is zero, jump to the matching `]`
- `]`: If the value at the data pointer is nonzero, jump to the matching `[`

## Example Programs

Hello World:
```brainfuck
++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.>.
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.