# Programming Language Reference (English Version)

## Overview

This is a comprehensive programming language with support for multiple data types, control flow, exception handling, and object-oriented structures.

## Keywords

### Reserved Words
| Keyword | Description | Token Code |
|---------|-------------|------------|
| `structure` | Structure/class definition | 1 |
| `public` | Public access modifier | 1 |
| `const` | Constant declaration | 1 |
| `void` | No return value | 1 |
| `main` | Entry point function | 1 |

### Functions
| Keyword | Description | Token Code |
|---------|-------------|------------|
| `print` | Output function | 2 |
| `input` | Input function | 3 |

## Data Types

### Complete Type System

| Keyword | Description | Size/Precision | Token Code |
|---------|-------------|----------------|------------|
| `number` | Generic numeric type (backward compatible) | Variable | 4 |
| `int` | 32-bit signed integer | 4 bytes | 40 |
| `float` | 32-bit floating point | 4 bytes | 41 |
| `double` | 64-bit double precision | 8 bytes | 41 |
| `string` | Text/string type | Variable | 42 |
| `char` | Single character | 2 bytes | 43 |
| `bool` | Boolean type | 1 byte | 44 |
| `array` | Array/collection type | Variable | 45 |

### Type Declaration Examples

```
// Generic number
number genericValue;
genericValue = 42;
genericValue = 3.14159;

// Integer type
int age;
int count;
age = 25;
count = 100;

// Floating point types
float price;
float rate;
price = 19.99;
rate = 3.14;

double pi;
double scientific;
pi = 3.14159265359;
scientific = 1.5e10;

// String type
string name;
string message;
name = "John Doe";
message = "Hello, World!";

// Character type
char grade;
char symbol;
grade = 'A';
symbol = '$';

// Boolean type
bool isActive;
bool isValid;
isActive = true;
isValid = false;

// Array types
array int scores;
array float prices;
array string names;
```

## Control Flow

### Conditionals
| Keyword | Description | Token Code |
|---------|-------------|------------|
| `if` | Conditional statement | 30 |
| `else` | Alternative branch | 30 |
| `elseif` | Else-if branch | 30 |

### Loops
| Keyword | Description | Token Code |
|---------|-------------|------------|
| `while` | While loop | 31 |
| `for` | For loop | 31 |

### Jump Statements
| Keyword | Description | Token Code |
|---------|-------------|------------|
| `break` | Exit loop | 32 |
| `continue` | Skip to next iteration | 32 |
| `return` | Return from function | 32 |

### Boolean Literals
| Keyword | Description | Token Code |
|---------|-------------|------------|
| `true` | Boolean true | 33 |
| `false` | Boolean false | 33 |

## Exception Handling

| Keyword | Description | Token Code |
|---------|-------------|------------|
| `try` | Try block start | 60 |
| `catch` | Exception handler | 60 |
| `throw` | Throw exception | 60 |
| `finally` | Finally block | 60 |

### Exception Handling Syntax

```
// Basic try-catch
try {
    // Code that might fail
    int divisor;
    divisor = 0;
    
    if (divisor == 0) {
        throw "Division by zero error";
    }
    
    int result;
    result = 100 / divisor;
} catch {
    print["Error caught and handled"];
}

// Try-catch-finally
try {
    print["Attempting operation..."];
    throw "Simulated error";
} catch {
    print["Exception handled"];
} finally {
    print["Cleanup executed"];
}

// Nested try blocks
try {
    print["Outer try"];
    
    try {
        print["Inner try"];
        throw "Inner error";
    } catch {
        print["Inner catch"];
    }
    
    throw "Outer error";
} catch {
    print["Outer catch"];
}
```

## Operators

### Arithmetic Operators
- `+` Addition
- `-` Subtraction
- `*` Multiplication
- `/` Division
- `%` Modulo (remainder)

### Comparison Operators
- `==` Equal to
- `!=` Not equal to
- `<` Less than
- `>` Greater than
- `<=` Less than or equal to
- `>=` Greater than or equal to

### Logical Operators
- `&&` Logical AND
- `||` Logical OR
- `!` Logical NOT

### Assignment
- `=` Assignment

## Delimiters
- `(` `)` Parentheses (opening/closing)
- `[` `]` Brackets (grouping)
- `{` `}` Braces (code blocks)
- `;` Semicolon (statement terminator)
- `,` Comma (separator)

## Literals

### Numeric Literals
- **Integers:** `10`, `42`, `0`, `-5`
- **Floats:** `3.14`, `20.5`, `1.5e10`
- **Doubles:** `3.14159265359`, `2.5e-5`

### String Literals
- **Double quotes:** `"Hello World"`, `"Text with \"quotes\""`
- **Escape sequences:**
  - `\n` Newline
  - `\t` Tab
  - `\"` Double quote
  - `\'` Single quote
  - `\\` Backslash

### Character Literals
- **Single quotes:** `'A'`, `'5'`, `'$'`, `'\n'`

### Boolean Literals
- `true` - True value
- `false` - False value

## Comments
- **Single-line:** `// This is a comment`
- **Multi-line:** `/* This is a multi-line comment */`

## Complete Example Program

```
structure Main (
    public const void main [ ] (
        // Data type declarations
        int age;
        float price;
        string name;
        bool isActive;
        
        // Variable assignments
        age = 25;
        price = 19.99;
        name = "John Doe";
        isActive = true;
        
        // Conditional statement
        if (age >= 18) {
            print["Adult"];
        } else {
            print["Minor"];
        }
        
        // Loop
        int i;
        i = 1;
        while (i <= 5) {
            print[i];
            i = i + 1;
        }
        
        // Exception handling
        try {
            if (price < 0) {
                throw "Invalid price";
            }
            print["Price is valid"];
        } catch {
            print["Error: Negative price"];
        }
    )
)
```

## Token Types Reference

| Code | Token Type | Description | Examples |
|------|-----------|-------------|----------|
| 1 | PALABRA_RESERVADA | Reserved words | structure, public, const, void, main |
| 2 | FUNCION_SALIDA | Output function | print |
| 3 | FUNCION_ENTRADA | Input function | input |
| 4 | TIPO_DATO | Generic data type | number |
| 5 | LITERAL_NUMERICO | Numeric literals | 10, 3.14, 1.5e10 |
| 6 | LITERAL_CADENA | String literals | "Hello" |
| 7 | OPERADOR_ASIGNACION | Assignment | = |
| 8 | OPERADOR_ARITMETICO | Arithmetic | +, -, *, / |
| 9 | APERTURA/CIERRE/AGRUPADOR | Delimiters | (, ), [, ] |
| 10 | PUNTO_Y_COMA | Statement end | ; |
| 11 | IDENTIFICADOR | Identifiers | num1, result |
| 20 | OPERADOR_COMPARACION | Comparison | ==, !=, <, >, <=, >= |
| 21 | OPERADOR_LOGICO | Logical | &&, \|\|, ! |
| 22 | COMA | Separator | , |
| 30 | CONDICIONAL | Conditionals | if, else, elseif |
| 31 | BUCLE | Loops | while, for |
| 32 | SALTO | Jump statements | break, continue, return |
| 33 | BOOLEANO | Boolean literals | true, false |
| 40 | TIPO_ENTERO | Integer type | int |
| 41 | TIPO_FLOTANTE | Float types | float, double |
| 42 | TIPO_CADENA_TIPO | String type | string |
| 43 | TIPO_CARACTER | Character type | char |
| 44 | TIPO_BOOLEANO | Boolean type | bool |
| 45 | TIPO_ARREGLO | Array type | array |
| 50 | ENTERO | Integer literals | 42, 100 |
| 51 | DECIMAL | Decimal literals | 3.14, 2.5 |
| 52 | CADENA | String values | "text" |
| 60 | EXCEPCION | Exception handling | try, catch, throw, finally |
| 100 | ERROR | Invalid tokens | - |
| 999 | EOF | End of file | - |

## Features

### Line Numbers
The text editor displays line numbers on the left side:
- Same font as the text
- Current line highlighted
- Width auto-expands for more digits
- Synchronized scrolling

### Lexical Analysis Features
- Precise token classification with unique codes
- Type-specific validation at lexical level
- Support for multiple numeric formats
- Escape sequence processing
- Comment recognition and skipping

## Files Structure

### Source Files
- **src/javaapplication4/TokenType.java** - Token type definitions
- **src/javaapplication4/Token.java** - Token class
- **src/javaapplication4/Scanner.java** - Lexical scanner
- **src/javaapplication4/Analisis_Lexico.java** - Legacy compatibility
- **src/javaapplication4/PrbLexico.java** - Main GUI
- **src/javaapplication4/LineNumberPanel.java** - Line numbers feature
- **src/javaapplication4/Archivos.java** - File I/O

### Test Files
- **Datos/archivo.txt** - Comprehensive test examples

## Compilation

To compile the project:
```bash
cd "JavaApplication4"
javac -d build/classes -sourcepath src src/javaapplication4/*.java
```

To run the application:
```bash
cd "JavaApplication4"
java -cp build/classes javaapplication4.PrbLexico
```

## Language Evolution

### Version History
1. **Initial Version** - Spanish keywords, basic lexical analysis
2. **English Translation** - All keywords translated to English
3. **Line Numbers** - Added line number display
4. **Control Flow** - Added conditionals and loops
5. **Data Types & Exceptions** - Added specific types and exception handling

### Current Capabilities
✅ Multiple data types (int, float, double, string, char, bool, array)  
✅ Exception handling (try, catch, throw, finally)  
✅ Control flow (if/else, while, for, break, continue)  
✅ Line numbers in editor  
✅ Precise lexical analysis with unique token codes  
✅ Comprehensive error detection  
✅ Full backward compatibility  

## Notes

- The `number` type is retained for backward compatibility
- Both `float` and `double` share token code 41 (same category)
- Exception handling supports nested try blocks
- All keywords are case-sensitive
- Array syntax: `array <type> <name>;`
