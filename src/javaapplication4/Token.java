/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication4;

/**
 * Represents a token in the lexical analysis
 * @author Gerardo
 */
public class Token {
    
    public static enum TokenType {
        PALABRA_RESERVADA(1),
        
        FUNCION_SALIDA(2),
        
        FUNCION_ENTRADA(3),
        
        TIPO_DATO(4),
        
        CONDICIONAL(30),
        
        BUCLE(31),
        
        SALTO(32),
        
        BOOLEANO(33),
        
        TIPO_ENTERO(40),
        TIPO_FLOTANTE(41),
        TIPO_CADENA_TIPO(42),
        TIPO_CARACTER(43),
        TIPO_BOOLEANO(44),
        TIPO_ARREGLO(45),
        
        EXCEPCION(60),
        
        LITERAL_NUMERICO(5),
        LITERAL_CADENA(6),
        LITERAL_CARACTER(43),
        
        OPERADOR_ASIGNACION(7),
        OPERADOR_ARITMETICO(8),
        OPERADOR_COMPARACION(20),
        OPERADOR_LOGICO(21),
        
        APERTURA(9),
        CIERRE(9),
        AGRUPADOR(9),
        PUNTO_Y_COMA(10),
        COMA(22),
        
        IDENTIFICADOR(11),
        
        ENTERO(50),
        DECIMAL(51),
        CADENA(52),
        VARIABLE(53),
        
        ERROR(100),
        EOF(999);
        
        private final int code;
        
        TokenType(int code) {
            this.code = code;
        }
        
        public int getCode() {
            return code;
        }
    }
    
    private String lexeme;
    private TokenType type;
    private int line;
    private int column;
    private String errorMessage;
    
    public Token(String lexeme, TokenType type, int line, int column) {
        this.lexeme = lexeme;
        this.type = type;
        this.line = line;
        this.column = column;
        this.errorMessage = null;
    }
    
    public Token(String lexeme, String errorMessage, int line, int column) {
        this.lexeme = lexeme;
        this.type = TokenType.ERROR;
        this.line = line;
        this.column = column;
        this.errorMessage = errorMessage;
    }
    
    public String getLexeme() {
        return lexeme;
    }
    
    public String getLexema() {
        return lexeme;
    }
    
    public TokenType getType() {
        return type;
    }
    
    public int getCode() {
        return type.getCode();
    }
    
    public String getTypeName() {
        return type.name();
    }
    
    public int getLine() {
        return line;
    }
    
    public int getColumn() {
        return column;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public boolean isError() {
        return type == TokenType.ERROR;
    }
    
    @Override
    public String toString() {
        if (isError()) {
            return String.format("Error en linea %d, col %d: %s - %s", 
                line, column, lexeme, errorMessage);
        }
        return String.format("Token[%s, '%s', code=%d, line=%d, col=%d]", 
            type.name(), lexeme, type.getCode(), line, column);
    }
}
