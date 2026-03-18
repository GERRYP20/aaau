/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication4;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Analisis_Lexico - Main lexical analysis class using the new Scanner
 * This class provides backward compatibility with the existing GUI while
 * using the new robust scanning engine.
 * @author Gerardo
 */
public class Analisis_Lexico
{
    private static class Scanner {
        private final String source;
        private final List<Token> tokens;
        private int start;
        private int current;
        private int line;
        private int column;
        private int startColumn;
        
        private static final Map<String, Token.TokenType> reservedWords;
        private static final Map<String, Token.TokenType> operators;
        
        static {
            reservedWords = new HashMap<>();
            reservedWords.put("structure", Token.TokenType.PALABRA_RESERVADA);
            reservedWords.put("public", Token.TokenType.PALABRA_RESERVADA);
            reservedWords.put("const", Token.TokenType.PALABRA_RESERVADA);
            reservedWords.put("void", Token.TokenType.PALABRA_RESERVADA);
            reservedWords.put("main", Token.TokenType.PALABRA_RESERVADA);
            reservedWords.put("print", Token.TokenType.FUNCION_SALIDA);
            reservedWords.put("input", Token.TokenType.FUNCION_ENTRADA);
            reservedWords.put("number", Token.TokenType.TIPO_DATO);
            reservedWords.put("if", Token.TokenType.CONDICIONAL);
            reservedWords.put("else", Token.TokenType.CONDICIONAL);
            reservedWords.put("elseif", Token.TokenType.CONDICIONAL);
            reservedWords.put("while", Token.TokenType.BUCLE);
            reservedWords.put("for", Token.TokenType.BUCLE);
            reservedWords.put("break", Token.TokenType.SALTO);
            reservedWords.put("continue", Token.TokenType.SALTO);
            reservedWords.put("return", Token.TokenType.SALTO);
            reservedWords.put("true", Token.TokenType.BOOLEANO);
            reservedWords.put("false", Token.TokenType.BOOLEANO);
            reservedWords.put("int", Token.TokenType.TIPO_ENTERO);
            reservedWords.put("float", Token.TokenType.TIPO_FLOTANTE);
            reservedWords.put("double", Token.TokenType.TIPO_FLOTANTE);
            reservedWords.put("string", Token.TokenType.TIPO_CADENA_TIPO);
            reservedWords.put("char", Token.TokenType.TIPO_CARACTER);
            reservedWords.put("bool", Token.TokenType.TIPO_BOOLEANO);
            reservedWords.put("array", Token.TokenType.TIPO_ARREGLO);
            reservedWords.put("try", Token.TokenType.EXCEPCION);
            reservedWords.put("catch", Token.TokenType.EXCEPCION);
            reservedWords.put("throw", Token.TokenType.EXCEPCION);
            reservedWords.put("finally", Token.TokenType.EXCEPCION);
            
            operators = new HashMap<>();
            operators.put("=", Token.TokenType.OPERADOR_ASIGNACION);
            operators.put("+", Token.TokenType.OPERADOR_ARITMETICO);
            operators.put("-", Token.TokenType.OPERADOR_ARITMETICO);
            operators.put("*", Token.TokenType.OPERADOR_ARITMETICO);
            operators.put("/", Token.TokenType.OPERADOR_ARITMETICO);
            operators.put("%", Token.TokenType.OPERADOR_ARITMETICO);
            operators.put("==", Token.TokenType.OPERADOR_COMPARACION);
            operators.put("!=", Token.TokenType.OPERADOR_COMPARACION);
            operators.put("<", Token.TokenType.OPERADOR_COMPARACION);
            operators.put(">", Token.TokenType.OPERADOR_COMPARACION);
            operators.put("<=", Token.TokenType.OPERADOR_COMPARACION);
            operators.put(">=", Token.TokenType.OPERADOR_COMPARACION);
            operators.put("&&", Token.TokenType.OPERADOR_LOGICO);
            operators.put("||", Token.TokenType.OPERADOR_LOGICO);
            operators.put("!", Token.TokenType.OPERADOR_LOGICO);
        }
        
        public Scanner(String source) {
            this.source = source;
            this.tokens = new ArrayList<>();
            this.start = 0;
            this.current = 0;
            this.line = 1;
            this.column = 1;
            this.startColumn = 1;
        }
        
        public List<Token> scanTokens() {
            while (!isAtEnd()) {
                start = current;
                startColumn = column;
                scanToken();
            }
            
            tokens.add(new Token("", Token.TokenType.EOF, line, column));
            return tokens;
        }
        
        private void scanToken() {
            char c = advance();
            
            switch (c) {
                case '+':
                case '-':
                    addToken(operators.get(String.valueOf(c)));
                    break;
                case '*':
                    addToken(Token.TokenType.OPERADOR_ARITMETICO);
                    break;
                case '/':
                    if (match('/')) {
                        while (peek() != '\n' && !isAtEnd()) advance();
                    } else if (match('*')) {
                        skipMultiLineComment();
                    } else {
                        addToken(Token.TokenType.OPERADOR_ARITMETICO);
                    }
                    break;
                case '%':
                    addToken(Token.TokenType.OPERADOR_ARITMETICO);
                    break;
                case '=':
                    if (match('=')) {
                        addToken(Token.TokenType.OPERADOR_COMPARACION);
                    } else {
                        addToken(Token.TokenType.OPERADOR_ASIGNACION);
                    }
                    break;
                case '!':
                    if (match('=')) {
                        addToken(Token.TokenType.OPERADOR_COMPARACION);
                    } else {
                        addToken(Token.TokenType.OPERADOR_LOGICO);
                    }
                    break;
                case '<':
                    addToken(Token.TokenType.OPERADOR_COMPARACION);
                    break;
                case '>':
                    addToken(Token.TokenType.OPERADOR_COMPARACION);
                    break;
                case '&':
                    if (match('&')) {
                        addToken(Token.TokenType.OPERADOR_LOGICO);
                    } else {
                        addError("Unexpected character '&'");
                    }
                    break;
                case '|':
                    if (match('|')) {
                        addToken(Token.TokenType.OPERADOR_LOGICO);
                    } else {
                        addError("Unexpected character '|'");
                    }
                    break;
                case '(':
                    addToken(Token.TokenType.APERTURA);
                    break;
                case ')':
                    addToken(Token.TokenType.CIERRE);
                    break;
                case '[':
                case ']':
                case '{':
                case '}':
                    addToken(Token.TokenType.AGRUPADOR);
                    break;
                case ';':
                    addToken(Token.TokenType.PUNTO_Y_COMA);
                    break;
                case ',':
                    addToken(Token.TokenType.COMA);
                    break;
                case '"':
                    string();
                    break;
                case '\'':
                    singleQuoteString();
                    break;
                case ' ':
                case '\r':
                case '\t':
                    break;
                case '\n':
                    line++;
                    column = 1;
                    break;
                default:
                    if (isDigit(c)) {
                        number();
                    } else if (isAlpha(c)) {
                        identifier();
                    } else {
                        addError("Unexpected character: '" + c + "'");
                    }
                    break;
            }
        }
        
        private void string() {
            StringBuilder value = new StringBuilder();
            
            while (peek() != '"' && !isAtEnd()) {
                if (peek() == '\n') {
                    line++;
                    column = 1;
                }
                if (peek() == '\\') {
                    advance();
                    char escaped = advance();
                    switch (escaped) {
                        case 'n': value.append('\n'); break;
                        case 't': value.append('\t'); break;
                        case 'r': value.append('\r'); break;
                        case '"': value.append('"'); break;
                        case '\\': value.append('\\'); break;
                        default: 
                            addError("Invalid escape sequence: \\'" + escaped + "'");
                            return;
                    }
                } else {
                    value.append(advance());
                }
            }
            
            if (isAtEnd()) {
                addError("Unterminated string");
                return;
            }
            
            advance();
            tokens.add(new Token("\"" + value.toString() + "\"", Token.TokenType.LITERAL_CADENA, line, startColumn));
        }
        
        private void singleQuoteString() {
            StringBuilder value = new StringBuilder();
            
            while (peek() != '\'' && !isAtEnd()) {
                if (peek() == '\n') {
                    line++;
                    column = 1;
                }
                if (peek() == '\\') {
                    advance();
                    char escaped = advance();
                    switch (escaped) {
                        case 'n': value.append('\n'); break;
                        case 't': value.append('\t'); break;
                        case 'r': value.append('\r'); break;
                        case '\'': value.append('\''); break;
                        case '\\': value.append('\\'); break;
                        default:
                            addError("Invalid escape sequence: \\'" + escaped + "'");
                            return;
                    }
                } else {
                    value.append(advance());
                }
            }
            
            if (isAtEnd()) {
                addError("Unterminated string");
                return;
            }
            
            advance();
            tokens.add(new Token("'" + value.toString() + "'", Token.TokenType.LITERAL_CARACTER, line, startColumn));
        }
        
        private void number() {
            while (isDigit(peek())) advance();
            
            boolean isDecimal = false;
            if (peek() == '.' && isDigit(peekNext())) {
                isDecimal = true;
                advance();
                while (isDigit(peek())) advance();
            }
            
            if (peek() == 'e' || peek() == 'E') {
                int savePos = current;
                advance();
                if (peek() == '+' || peek() == '-') advance();
                if (isDigit(peek())) {
                    isDecimal = true;
                    while (isDigit(peek())) advance();
                } else {
                    current = savePos;
                    column = startColumn + (current - start);
                }
            }
            
            String numberStr = source.substring(start, current);
            Token.TokenType type = isDecimal ? Token.TokenType.DECIMAL : Token.TokenType.ENTERO;
            tokens.add(new Token(numberStr, type, line, startColumn));
        }
        
        private void identifier() {
            while (isAlphaNumeric(peek())) advance();
            
            char nextChar = peek();
            if (!isValidDelimiter(nextChar)) {
                advance();
                String errorText = source.substring(start, current);
                addError("Identificador invalido: caracter no permitido '" + nextChar + "' despues del identificador");
                return;
            }
            
            String text = source.substring(start, current);
            Token.TokenType type = reservedWords.getOrDefault(text, Token.TokenType.IDENTIFICADOR);
            addToken(type);
        }
        
        private boolean isValidDelimiter(char c) {
            return c == '\0' || c == ' ' || c == '\t' || c == '\n' || c == '\r' ||
                   c == '=' || c == '+' || c == '-' || c == '*' || c == '/' ||
                   c == '(' || c == ')' || c == '[' || c == ']' || c == '{' || c == '}' ||
                   c == ';' || c == ',' || c == '<' || c == '>' || c == '!' ||
                   c == '&' || c == '|' || c == '"' || c == '\'';
        }
        
        private void skipMultiLineComment() {
            int nesting = 1;
            while (nesting > 0 && !isAtEnd()) {
                if (peek() == '/' && peekNext() == '*') {
                    advance();
                    advance();
                    nesting++;
                } else if (peek() == '*' && peekNext() == '/') {
                    advance();
                    advance();
                    nesting--;
                } else {
                    if (peek() == '\n') {
                        line++;
                        column = 1;
                    }
                    advance();
                }
            }
            
            if (nesting > 0) {
                addError("Unterminated multi-line comment");
            }
        }
        
        private boolean match(char expected) {
            if (isAtEnd()) return false;
            if (source.charAt(current) != expected) return false;
            current++;
            column++;
            return true;
        }
        
        private char peek() {
            if (isAtEnd()) return '\0';
            return source.charAt(current);
        }
        
        private char peekNext() {
            if (current + 1 >= source.length()) return '\0';
            return source.charAt(current + 1);
        }
        
        private char advance() {
            char c = source.charAt(current);
            current++;
            column++;
            return c;
        }
        
        private boolean isAtEnd() {
            return current >= source.length();
        }
        
        private boolean isDigit(char c) {
            return c >= '0' && c <= '9';
        }
        
        private boolean isAlpha(char c) {
            return (c >= 'a' && c <= 'z') || 
                   (c >= 'A' && c <= 'Z') || 
                   c == '_' ||
                   (c >= 'á' && c <= 'ú') ||
                   (c >= 'Á' && c <= 'Ú') ||
                   c == 'ñ' || c == 'Ñ';
        }
        
        private boolean isAlphaNumeric(char c) {
            return isAlpha(c) || isDigit(c);
        }
        
        private void addToken(Token.TokenType type) {
            String text = source.substring(start, current);
            tokens.add(new Token(text, type, line, startColumn));
        }
        
        private void addError(String message) {
            String text = source.substring(start, current);
            tokens.add(new Token(text, message, line, startColumn));
        }
    }

    public String lexema;
    public String nombre;
    public int numero;
    
    String[][] diccionario = {
        {"structure", "PALABRA_RESERVADA", "1"},
        {"public", "PALABRA_RESERVADA", "1"},
        {"const", "PALABRA_RESERVADA", "1"},
        {"void", "PALABRA_RESERVADA", "1"},
        {"main", "PALABRA_RESERVADA", "1"},
        {"print", "FUNCION_SALIDA", "2"},
        {"input", "FUNCION_ENTRADA", "3"},
        {"number", "TIPO_DATO", "4"},
        {"10", "LITERAL_NUMERICO", "5"},
        {"\"..\"", "LITERAL_CADENA", "6"},
        {"=", "OPERADOR_ASIGNACION", "7"},
        {"+", "OPERADOR_ARITMETICO", "8"},
        {"-", "OPERADOR_ARITMETICO", "8"},
        {"*", "OPERADOR_ARITMETICO", "8"},
        {"/", "OPERADOR_ARITMETICO", "8"},
        {"%", "OPERADOR_ARITMETICO", "8"},
        {"(", "APERTURA", "9"},
        {")", "CIERRE", "9"},
        {"[", "AGRUPADOR", "9"},
        {"]", "AGRUPADOR", "9"},
        {";", "PUNTO_Y_COMA", "10"},
        {",", "SEPARADOR", "22"},
        {"==", "OPERADOR_COMPARACION", "20"},
        {"!=", "OPERADOR_COMPARACION", "20"},
        {"<", "OPERADOR_COMPARACION", "20"},
        {">", "OPERADOR_COMPARACION", "20"},
        {"<=", "OPERADOR_COMPARACION", "20"},
        {">=", "OPERADOR_COMPARACION", "20"},
        {"&&", "OPERADOR_LOGICO", "21"},
        {"||", "OPERADOR_LOGICO", "21"},
        {"!", "OPERADOR_LOGICO", "21"},
        {"if", "CONDICIONAL", "30"},
        {"else", "CONDICIONAL", "30"},
        {"elseif", "CONDICIONAL", "30"},
        {"while", "BUCLE", "31"},
        {"for", "BUCLE", "31"},
        {"break", "SALTO", "32"},
        {"continue", "SALTO", "32"},
        {"return", "SALTO", "32"},
        {"true", "BOOLEANO", "33"},
        {"false", "BOOLEANO", "33"},
        {"int", "TIPO_ENTERO", "40"},
        {"float", "TIPO_FLOTANTE", "41"},
        {"double", "TIPO_FLOTANTE", "41"},
        {"string", "TIPO_CADENA_TIPO", "42"},
        {"char", "TIPO_CARACTER", "43"},
        {"bool", "TIPO_BOOLEANO", "44"},
        {"array", "TIPO_ARREGLO", "45"},
        {"try", "EXCEPCION", "60"},
        {"catch", "EXCEPCION", "60"},
        {"throw", "EXCEPCION", "60"},
        {"finally", "EXCEPCION", "60"},
    };
    
    @Deprecated
    public Analisis_Lexico Analiza(String palabra) {
        Analisis_Lexico objLexico = new Analisis_Lexico();
        objLexico.lexema = palabra;
        
        int index = 0;
        boolean bandera = false;
        
        while (index < diccionario.length) {
            if (palabra.equals(diccionario[index][0])) {
                bandera = true;
                objLexico.nombre = diccionario[index][1];
                objLexico.numero = Integer.parseInt(diccionario[index][2]);
                break;
            }
            index++;
        }
        
        if (bandera) {
            return objLexico;
        }
        
        return analyzeWithFSA(objLexico, palabra);
    }
    
    public List<Token> scan(String source) {
        Scanner scanner = new Scanner(source);
        return scanner.scanTokens();
    }
    
    public List<Analisis_Lexico> scanToLegacyFormat(String source) {
        List<Token> tokens = scan(source);
        List<Analisis_Lexico> legacyList = new ArrayList<>();
        
        for (Token token : tokens) {
            if (token.getType() == Token.TokenType.EOF) continue;
            
            Analisis_Lexico legacy = new Analisis_Lexico();
            legacy.lexema = token.getLexeme();
            legacy.nombre = token.getTypeName();
            legacy.numero = token.getCode();
            legacyList.add(legacy);
        }
        
        return legacyList;
    }
    
    private Analisis_Lexico analyzeWithFSA(Analisis_Lexico objLexico, String palabra) {
        final int START = 0;
        final int IN_INTEGER = 1;
        final int IN_DECIMAL = 2;
        final int IN_STRING = 3;
        final int IN_IDENTIFIER = 4;
        final int ERROR = 5;
        
        int state = START;
        StringBuilder recognized = new StringBuilder();
        
        for (int i = 0; i < palabra.length(); i++) {
            char c = palabra.charAt(i);
            
            switch (state) {
                case START:
                    if (isDigit(c)) {
                        state = IN_INTEGER;
                        recognized.append(c);
                    } else if (c == '"') {
                        state = IN_STRING;
                        recognized.append(c);
                    } else if (isAlpha(c)) {
                        state = IN_IDENTIFIER;
                        recognized.append(c);
                    } else {
                        state = ERROR;
                    }
                    break;
                case IN_INTEGER:
                    if (isDigit(c)) {
                        recognized.append(c);
                    } else if (c == '.' && i + 1 < palabra.length() && isDigit(palabra.charAt(i + 1))) {
                        state = IN_DECIMAL;
                        recognized.append(c);
                    } else {
                        state = ERROR;
                    }
                    break;
                case IN_DECIMAL:
                    if (isDigit(c)) {
                        recognized.append(c);
                    } else if (c == 'e' || c == 'E') {
                        if (i + 1 < palabra.length() && 
                            (palabra.charAt(i + 1) == '+' || palabra.charAt(i + 1) == '-' || isDigit(palabra.charAt(i + 1)))) {
                            recognized.append(c);
                        } else {
                            state = ERROR;
                        }
                    } else if ((c == '+' || c == '-') && i > 0 && 
                              (palabra.charAt(i - 1) == 'e' || palabra.charAt(i - 1) == 'E')) {
                        recognized.append(c);
                    } else {
                        state = ERROR;
                    }
                    break;
                case IN_STRING:
                    recognized.append(c);
                    if (c == '"' && i > 0) {
                        state = START;
                    }
                    break;
                case IN_IDENTIFIER:
                    if (isAlphaNumeric(c)) {
                        recognized.append(c);
                    } else {
                        state = ERROR;
                    }
                    break;
                case ERROR:
                    break;
            }
        }
        
        switch (state) {
            case IN_INTEGER:
                objLexico.nombre = "ENTERO";
                objLexico.numero = 50;
                break;
            case IN_DECIMAL:
                objLexico.nombre = "DECIMAL";
                objLexico.numero = 51;
                break;
            case IN_STRING:
                if (recognized.length() > 1 && recognized.charAt(recognized.length() - 1) == '"') {
                    objLexico.nombre = "LITERAL_CADENA";
                    objLexico.numero = 6;
                } else {
                    objLexico.nombre = "ERROR_CADENA";
                    objLexico.numero = 102;
                }
                break;
            case IN_IDENTIFIER:
                objLexico.nombre = "IDENTIFICADOR";
                objLexico.numero = 11;
                break;
            default:
                objLexico.nombre = "ERROR_DESCONOCIDO";
                objLexico.numero = 100;
        }
        
        return objLexico;
    }
    
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
    
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || 
               (c >= 'A' && c <= 'Z') || 
               c == '_' ||
               (c >= 'á' && c <= 'ú') ||
               (c >= 'Á' && c <= 'Ú') ||
               c == 'ñ' || c == 'Ñ';
    }
    
    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }
    
    public boolean CaracterEspecial(char caracter) {
        char[] PI = {'%', '[', ']', '_', '!'};
        for (char c : PI) {
            if (caracter == c) return true;
        }
        return false;
    }
}
