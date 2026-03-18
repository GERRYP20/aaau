/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication4;

import java.util.List;
import java.util.ArrayList;

/**
 * Analisis_Sintactico - Syntactic analysis wrapper class
 * Provides a simple interface for performing syntactic analysis
 * @author Gerardo
 */
public class Analisis_Sintactico {
    
    public static class SyntaxError {
        private String message;
        private int line;
        private int column;
        private String expected;
        private String found;
        
        public SyntaxError(String message, int line, int column, String expected, String found) {
            this.message = message;
            this.line = line;
            this.column = column;
            this.expected = expected;
            this.found = found;
        }
        
        public String getMessage() { return message; }
        public int getLine() { return line; }
        public int getColumn() { return column; }
        public String getExpected() { return expected; }
        public String getFound() { return found; }
        
        @Override
        public String toString() {
            return String.format("Error la linea %d, columna %d: %s (Esperaba: %s, Encontró: %s)",
                line, column, message, expected, found);
        }
    }
    
    public static class ParseException extends RuntimeException {
        public ParseException(String message) {
            super(message);
        }
    }
    
    public static class Parser {
        private List<Token> tokens;
        private int current;
        private List<SyntaxError> errors;
        private ASTNode ast;
        
        public Parser(List<Token> tokens) {
            this.tokens = tokens;
            this.current = 0;
            this.errors = new ArrayList<>();
            this.ast = null;
        }
        
        public ASTNode parse() {
            try {
                ast = parseProgram();
                if (!isAtEnd()) {
                    error("Tokens inesperados después del fin del programa", peek());
                }
                return ast;
            } catch (ParseException e) {
                return null;
            }
        }
        
        private ASTNode parseProgram() {
            ASTNode program = new ASTNode(ASTNode.NodeType.PROGRAM);
            ASTNode structure = parseStructure();
            if (structure != null) {
                program.addChild(structure);
            }
            return program;
        }
        
        private ASTNode parseStructure() {
            ASTNode structure = new ASTNode(ASTNode.NodeType.STRUCTURE);
            
            if (!match(Token.TokenType.PALABRA_RESERVADA, "structure")) {
                error("Se esperaba la palabra clave 'structure' al inicio del programa", peek());
                return null;
            }
            
            Token nameToken = consume(Token.TokenType.IDENTIFICADOR, "Se esperaba nombre de estructura");
            if (nameToken != null) {
                if (!nameToken.getLexeme().equals("Main")) {
                    error("Se esperaba 'Main' como nombre de la estructura, pero se encontró '" + nameToken.getLexeme() + "'", nameToken);
                } else {
                    structure.addChild(new ASTNode(ASTNode.NodeType.IDENTIFIER, nameToken));
                }
            }
            
            consume(Token.TokenType.APERTURA, "Se esperaba '(' después del nombre de estructura");
            
            ASTNode members = parseMemberList();
            if (members != null) {
                structure.addChild(members);
            }
            
            consume(Token.TokenType.CIERRE, "Se esperaba ')' para cerrar la estructura");
            
            return structure;
        }
        
        private ASTNode parseMemberList() {
            ASTNode members = new ASTNode(ASTNode.NodeType.STATEMENT_LIST);
            
            while (!check(Token.TokenType.CIERRE) && !isAtEnd()) {
                ASTNode member = null;
                
                if (isType(peek())) {
                    if (isFunctionDefinition()) {
                        member = parseFunction();
                    } else {
                        member = parseDeclaration();
                    }
                } else if (check(Token.TokenType.PALABRA_RESERVADA, "public") || 
                           check(Token.TokenType.PALABRA_RESERVADA, "private")) {
                    member = parseFunction();
                }
                
                if (member != null) {
                    members.addChild(member);
                } else {
                    error("Token inesperado en el cuerpo de la estructura", peek());
                    advance();
                }
            }
            
            return members;
        }
        
        private boolean isFunctionDefinition() {
            int save = current;
            boolean result = false;
            
            if (match(Token.TokenType.PALABRA_RESERVADA, "public") || 
                match(Token.TokenType.PALABRA_RESERVADA, "private")) {
            }
            
            if (match(Token.TokenType.PALABRA_RESERVADA, "const")) {
            }
            
            if (isType(peek())) {
                advance();
                if (check(Token.TokenType.IDENTIFICADOR) || check(Token.TokenType.PALABRA_RESERVADA)) {
                    Token next = peek();
                    if (next.getLexeme().equals("main")) {
                        result = true;
                    }
                }
            }
            
            current = save;
            return result;
        }
        
        private ASTNode parseFunction() {
            ASTNode function = new ASTNode(ASTNode.NodeType.FUNCTION);
            
            if (match(Token.TokenType.PALABRA_RESERVADA, "public") || 
                match(Token.TokenType.PALABRA_RESERVADA, "private")) {
                Token access = previous();
                function.addChild(new ASTNode(ASTNode.NodeType.ACCESS_MODIFIER, access));
            }
            
            consume(Token.TokenType.PALABRA_RESERVADA, "const", "Se esperaba 'const' en la definición de función");
            
            Token returnType = parseType();
            if (returnType != null) {
                function.addChild(new ASTNode(ASTNode.NodeType.TYPE, returnType));
            }
            
            Token funcName = null;
            if (match(Token.TokenType.PALABRA_RESERVADA, "main")) {
                funcName = previous();
            } else if (check(Token.TokenType.IDENTIFICADOR)) {
                funcName = consume(Token.TokenType.IDENTIFICADOR);
            } else {
                error("Se esperaba nombre de función (main o identificador)", peek());
            }
            
            if (funcName != null) {
                function.addChild(new ASTNode(ASTNode.NodeType.IDENTIFIER, funcName));
            }
            
            consume(Token.TokenType.AGRUPADOR, "[", "Se esperaba '[' después del nombre de función");
            consume(Token.TokenType.AGRUPADOR, "]", "Se esperaba ']' después de '['");
            
            consume(Token.TokenType.APERTURA, "Se esperaba '(' para iniciar el cuerpo de la función");
            
            ASTNode body = parseStatementList();
            if (body != null) {
                function.addChild(body);
            }
            
            consume(Token.TokenType.CIERRE, "Se esperaba ')' para cerrar el cuerpo de la función");
            
            return function;
        }
        
        private ASTNode parseStatementList() {
            ASTNode statements = new ASTNode(ASTNode.NodeType.STATEMENT_LIST);
            
            while (!check(Token.TokenType.CIERRE) && !isAtEnd()) {
                ASTNode statement = parseStatement();
                if (statement != null) {
                    statements.addChild(statement);
                }
            }
            
            return statements;
        }
        
        private ASTNode parseStatement() {
            if (isAtEnd()) return null;
            
            Token token = peek();
            
            if (match(Token.TokenType.EXCEPCION, "try")) {
                return parseTryCatch();
            }
            
            if (match(Token.TokenType.SALTO, "throw")) {
                return parseThrowStatement();
            }
            
            if (match(Token.TokenType.SALTO, "break")) {
                return parseBreakStatement();
            }
            
            if (match(Token.TokenType.SALTO, "continue")) {
                return parseContinueStatement();
            }
            
            if (match(Token.TokenType.SALTO, "return")) {
                return parseReturnStatement();
            }
            
            if (match(Token.TokenType.CONDICIONAL, "if")) {
                return parseIfStatement();
            }
            
            if (match(Token.TokenType.BUCLE, "while")) {
                return parseWhileStatement();
            }
            
            if (match(Token.TokenType.BUCLE, "for")) {
                return parseForStatement();
            }
            
            if (canStartDeclaration(token)) {
                int save = current;
                advance();
                if (check(Token.TokenType.IDENTIFICADOR)) {
                    advance();
                    if (check(Token.TokenType.OPERADOR_ASIGNACION)) {
                        current = save;
                        return parseDeclaration();
                    } else {
                        current = save;
                        return parseDeclaration();
                    }
                }
                current = save;
                return parseDeclaration();
            }
            
            if (check(Token.TokenType.IDENTIFICADOR)) {
                int save = current;
                advance();
                if (check(Token.TokenType.OPERADOR_ASIGNACION)) {
                    current = save;
                    return parseAssignment();
                }
                current = save;
                return parseExpressionStatement();
            }
            
            if (match(Token.TokenType.FUNCION_SALIDA, "print")) {
                return parsePrintStatement();
            }
            
            if (match(Token.TokenType.FUNCION_ENTRADA, "input")) {
                return parseInputStatement();
            }
            
            error("Token inesperado en la instrucción", token);
            advance();
            return null;
        }
        
        private ASTNode parseDeclaration() {
            ASTNode declaration = new ASTNode(ASTNode.NodeType.DECLARATION);
            
            boolean isConst = false;
            if (match(Token.TokenType.PALABRA_RESERVADA, "const")) {
                isConst = true;
                declaration.setConstant(true);
            }
            
            Token typeToken = parseType();
            String typeName = null;
            ASTNode.DataType baseType = ASTNode.DataType.UNKNOWN;
            if (typeToken != null) {
                typeName = typeToken.getLexeme();
                baseType = ASTNode.DataType.fromString(typeName);
                declaration.addChild(new ASTNode(ASTNode.NodeType.TYPE, typeToken));
                declaration.setDataType(typeName);
            }
            
            Token name = consume(Token.TokenType.IDENTIFICADOR, "Se esperaba nombre de variable después del tipo");
            if (name != null) {
                declaration.addChild(new ASTNode(ASTNode.NodeType.IDENTIFIER, name));
            }
            
            int arraySize = 0;
            if (check(Token.TokenType.AGRUPADOR) && peek().getLexeme().equals("[")) {
                consume(Token.TokenType.AGRUPADOR, "[", "Se esperaba '[' después del identificador");
                
                Token sizeToken = consume(Token.TokenType.ENTERO, "Se esperaba tamaño de array (número entero positivo)");
                if (sizeToken != null) {
                    try {
                        arraySize = Integer.parseInt(sizeToken.getLexeme());
                        if (arraySize <= 0) {
                            error("El tamaño del array debe ser un número entero positivo", sizeToken);
                            arraySize = 0;
                        }
                    } catch (NumberFormatException e) {
                        error("El tamaño del array debe ser un número entero positivo", sizeToken);
                        arraySize = 0;
                    }
                } else {
                    error("Se esperaba tamaño de array (número entero positivo)", peek());
                }
                
                consume(Token.TokenType.AGRUPADOR, "]", "Se esperaba ']' después del tamaño del array");
                
                declaration.setArraySize(arraySize);
                if (arraySize > 0 && baseType != ASTNode.DataType.UNKNOWN) {
                    declaration.setDataType(ASTNode.DataType.ARRAY);
                }
            }
            
            if (match(Token.TokenType.OPERADOR_ASIGNACION, "=")) {
                if (check(Token.TokenType.AGRUPADOR) && peek().getLexeme().equals("{")) {
                    consume(Token.TokenType.AGRUPADOR, "{", "Se esperaba '{' para inicialización del array");
                    
                    ASTNode initList = parseArrayInitializerList();
                    
                    if (initList != null) {
                        declaration.addChild(initList);
                    }
                    
                    consume(Token.TokenType.AGRUPADOR, "}", "Se esperaba '}' después de la inicialización del array");
                } else {
                    ASTNode init = parseExpression();
                    if (init != null) {
                        declaration.addChild(init);
                    }
                }
            }
            
            consume(Token.TokenType.PUNTO_Y_COMA, "Se esperaba ';' después de la declaración");
            
            return declaration;
        }
        
        private ASTNode parseArrayInitializerList() {
            ASTNode list = new ASTNode(ASTNode.NodeType.ARRAY_INITIALIZER);
            
            if (check(Token.TokenType.AGRUPADOR) && peek().getLexeme().equals("}")) {
                return list;
            }
            
            ASTNode first = parseExpression();
            if (first != null) {
                list.addChild(first);
            }
            
            while (match(Token.TokenType.COMA, ",")) {
                ASTNode next = parseExpression();
                if (next != null) {
                    list.addChild(next);
                }
            }
            
            return list;
        }
        
        private ASTNode parseAssignment() {
            ASTNode assignment = new ASTNode(ASTNode.NodeType.ASSIGNMENT);
            
            Token name = consume(Token.TokenType.IDENTIFICADOR, "Se esperaba nombre de variable");
            if (name != null) {
                assignment.addChild(new ASTNode(ASTNode.NodeType.IDENTIFIER, name));
            }
            
            consume(Token.TokenType.OPERADOR_ASIGNACION, "Se esperaba '=' en la asignación");
            
            ASTNode expr = parseExpression();
            if (expr != null) {
                assignment.addChild(expr);
            }
            
            consume(Token.TokenType.PUNTO_Y_COMA, "Se esperaba ';' después de la asignación");
            
            return assignment;
        }
        
        private ASTNode parseIfStatement() {
            ASTNode ifStmt = new ASTNode(ASTNode.NodeType.IF_STATEMENT);
            
            consume(Token.TokenType.APERTURA, "Se esperaba '(' después de 'if'");
            
            ASTNode condition = parseExpression();
            if (condition != null) {
                ifStmt.addChild(condition);
            }
            
            consume(Token.TokenType.CIERRE, "Se esperaba ')' después de la condición if");
            consume(Token.TokenType.APERTURA, "Se esperaba '(' para iniciar el cuerpo del if");
            
            ASTNode thenBranch = parseStatementList();
            if (thenBranch != null) {
                ifStmt.addChild(thenBranch);
            }
            
            consume(Token.TokenType.CIERRE, "Se esperaba ')' para cerrar el cuerpo del if");
            
            if (match(Token.TokenType.CONDICIONAL, "else")) {
                ASTNode elseBranch = parseElsePart();
                if (elseBranch != null) {
                    ifStmt.addChild(elseBranch);
                }
            } else if (match(Token.TokenType.CONDICIONAL, "elseif")) {
                ASTNode elseIfBranch = parseIfStatement();
                if (elseIfBranch != null) {
                    elseIfBranch.setType(ASTNode.NodeType.IF_STATEMENT);
                    ifStmt.addChild(elseIfBranch);
                }
            }
            
            return ifStmt;
        }
        
        private ASTNode parseElsePart() {
            consume(Token.TokenType.APERTURA, "Se esperaba '(' después de 'else'");
            
            ASTNode elseBody = parseStatementList();
            
            consume(Token.TokenType.CIERRE, "Se esperaba ')' para cerrar el cuerpo del else");
            
            return elseBody;
        }
        
        private ASTNode parseWhileStatement() {
            ASTNode whileStmt = new ASTNode(ASTNode.NodeType.WHILE_STATEMENT);
            
            consume(Token.TokenType.APERTURA, "Se esperaba '(' después de 'while'");
            
            ASTNode condition = parseExpression();
            if (condition != null) {
                whileStmt.addChild(condition);
            }
            
            consume(Token.TokenType.CIERRE, "Se esperaba ')' después de la condición while");
            consume(Token.TokenType.APERTURA, "Se esperaba '(' para iniciar el cuerpo del while");
            
            ASTNode body = parseStatementList();
            if (body != null) {
                whileStmt.addChild(body);
            }
            
            consume(Token.TokenType.CIERRE, "Se esperada ')' para cerrar el cuerpo del while");
            
            return whileStmt;
        }
        
        private ASTNode parseForStatement() {
            ASTNode forStmt = new ASTNode(ASTNode.NodeType.FOR_STATEMENT);
            
            consume(Token.TokenType.APERTURA, "Se esperaba '(' después de 'for'");
            
            ASTNode init = parseAssignment();
            if (init != null) {
                forStmt.addChild(init);
            }
            
            ASTNode condition = parseExpression();
            if (condition != null) {
                forStmt.addChild(condition);
            }
            
            consume(Token.TokenType.PUNTO_Y_COMA, "Se esperaba ';' después de la condición del for");
            
            ASTNode increment = parseAssignment();
            if (increment != null) {
                forStmt.addChild(increment);
            }
            
            consume(Token.TokenType.CIERRE, "Se esperaba ')' después de las cláusulas del for");
            consume(Token.TokenType.APERTURA, "Se esperaba '(' para iniciar el cuerpo del for");
            
            ASTNode body = parseStatementList();
            if (body != null) {
                forStmt.addChild(body);
            }
            
            consume(Token.TokenType.CIERRE, "Se esperaba ')' para cerrar el cuerpo del for");
            
            return forStmt;
        }
        
        private ASTNode parseTryCatch() {
            ASTNode tryCatch = new ASTNode(ASTNode.NodeType.TRY_CATCH);
            
            consume(Token.TokenType.APERTURA, "Se esperaba '(' después de 'try'");
            
            ASTNode tryBody = parseStatementList();
            if (tryBody != null) {
                tryCatch.addChild(tryBody);
            }
            
            consume(Token.TokenType.CIERRE, "Se esperaba ')' para cerrar el cuerpo del try");
            consume(Token.TokenType.EXCEPCION, "catch", "Se esperaba 'catch' después del bloque try");
            consume(Token.TokenType.APERTURA, "Se esperaba '(' después de 'catch'");
            
            ASTNode catchBody = parseStatementList();
            if (catchBody != null) {
                tryCatch.addChild(catchBody);
            }
            
            consume(Token.TokenType.CIERRE, "Se esperaba ')' para cerrar el cuerpo del catch");
            
            if (match(Token.TokenType.EXCEPCION, "finally")) {
                consume(Token.TokenType.APERTURA, "Se esperaba '(' después de 'finally'");
                
                ASTNode finallyBody = parseStatementList();
                if (finallyBody != null) {
                    tryCatch.addChild(finallyBody);
                }
                
                consume(Token.TokenType.CIERRE, "Se esperaba ')' para cerrar el cuerpo del finally");
            }
            
            return tryCatch;
        }
        
        private ASTNode parseThrowStatement() {
            ASTNode throwStmt = new ASTNode(ASTNode.NodeType.THROW_STATEMENT);
            
            ASTNode expr = parseExpression();
            if (expr != null) {
                throwStmt.addChild(expr);
            }
            
            consume(Token.TokenType.PUNTO_Y_COMA, "Se esperaba ';' después de la instrucción throw");
            
            return throwStmt;
        }
        
        private ASTNode parseBreakStatement() {
            ASTNode breakStmt = new ASTNode(ASTNode.NodeType.BREAK_STATEMENT, previous());
            consume(Token.TokenType.PUNTO_Y_COMA, "Se esperaba ';' después de break");
            return breakStmt;
        }
        
        private ASTNode parseContinueStatement() {
            ASTNode continueStmt = new ASTNode(ASTNode.NodeType.CONTINUE_STATEMENT, previous());
            consume(Token.TokenType.PUNTO_Y_COMA, "Se esperaba ';' después de continue");
            return continueStmt;
        }
        
        private ASTNode parseReturnStatement() {
            ASTNode returnStmt = new ASTNode(ASTNode.NodeType.RETURN_STATEMENT, previous());
            
            if (!check(Token.TokenType.PUNTO_Y_COMA)) {
                ASTNode expr = parseExpression();
                if (expr != null) {
                    returnStmt.addChild(expr);
                }
            }
            
            consume(Token.TokenType.PUNTO_Y_COMA, "Se esperaba ';' después de return");
            return returnStmt;
        }
        
        private ASTNode parsePrintStatement() {
            ASTNode printStmt = new ASTNode(ASTNode.NodeType.FUNCTION_CALL, previous());
            
            consume(Token.TokenType.AGRUPADOR, "[", "Se esperaba '[' después de 'print'");
            
            ASTNode args = parseExpressionList();
            if (args != null) {
                printStmt.addChild(args);
            }
            
            consume(Token.TokenType.AGRUPADOR, "]", "Se esperaba ']' después de los argumentos de print");
            consume(Token.TokenType.PUNTO_Y_COMA, "Se esperaba ';' después de la instrucción print");
            
            return printStmt;
        }
        
        private ASTNode parseInputStatement() {
            ASTNode inputStmt = new ASTNode(ASTNode.NodeType.FUNCTION_CALL, previous());
            
            Token varName = consume(Token.TokenType.IDENTIFICADOR, "Se esperaba nombre de variable después de 'input'");
            if (varName != null) {
                inputStmt.addChild(new ASTNode(ASTNode.NodeType.IDENTIFIER, varName));
            }
            
            consume(Token.TokenType.PUNTO_Y_COMA, "Se esperaba ';' después de la instrucción input");
            
            return inputStmt;
        }
        
        private ASTNode parseExpressionStatement() {
            ASTNode expr = parseExpression();
            consume(Token.TokenType.PUNTO_Y_COMA, "Se esperaba ';' después de la expresión");
            return expr;
        }
        
        private ASTNode parseExpressionList() {
            ASTNode list = new ASTNode(ASTNode.NodeType.ARGUMENT_LIST);
            
            if (check(Token.TokenType.AGRUPADOR) && peek().getLexeme().equals("]")) {
                return list;
            }
            
            ASTNode first = parseExpression();
            if (first != null) {
                list.addChild(first);
            }
            
            while (match(Token.TokenType.COMA, ",")) {
                ASTNode next = parseExpression();
                if (next != null) {
                    list.addChild(next);
                }
            }
            
            return list;
        }
        
        private ASTNode parseExpression() {
            ASTNode left = parseTerm();
            
            while (match(Token.TokenType.OPERADOR_ARITMETICO, "+") || 
                   match(Token.TokenType.OPERADOR_ARITMETICO, "-") ||
                   match(Token.TokenType.OPERADOR_COMPARACION, "==") ||
                   match(Token.TokenType.OPERADOR_COMPARACION, "!=") ||
                   match(Token.TokenType.OPERADOR_COMPARACION, "<") ||
                   match(Token.TokenType.OPERADOR_COMPARACION, ">") ||
                   match(Token.TokenType.OPERADOR_COMPARACION, "<=") ||
                   match(Token.TokenType.OPERADOR_COMPARACION, ">=") ||
                   match(Token.TokenType.OPERADOR_LOGICO, "&&") ||
                   match(Token.TokenType.OPERADOR_LOGICO, "||")) {
                Token operator = previous();
                ASTNode right = parseTerm();
                
                ASTNode binaryOp = new ASTNode(ASTNode.NodeType.EXPRESSION, operator);
                if (left != null) binaryOp.addChild(left);
                if (right != null) binaryOp.addChild(right);
                left = binaryOp;
            }
            
            return left;
        }
        
        private ASTNode parseTerm() {
            ASTNode left = parseFactor();
            
            while (match(Token.TokenType.OPERADOR_ARITMETICO, "*") || 
                   match(Token.TokenType.OPERADOR_ARITMETICO, "/") ||
                   match(Token.TokenType.OPERADOR_ARITMETICO, "%")) {
                Token operator = previous();
                ASTNode right = parseFactor();
                
                ASTNode binaryOp = new ASTNode(ASTNode.NodeType.TERM, operator);
                if (left != null) binaryOp.addChild(left);
                if (right != null) binaryOp.addChild(right);
                left = binaryOp;
            }
            
            return left;
        }
        
        private ASTNode parseFactor() {
            if (match(Token.TokenType.BOOLEANO, "true") || match(Token.TokenType.BOOLEANO, "false")) {
                return new ASTNode(ASTNode.NodeType.BOOL_LITERAL, previous());
            }
            
            if (check(Token.TokenType.LITERAL_CADENA)) {
                return new ASTNode(ASTNode.NodeType.STRING_LITERAL, advance());
            }
            
            if (check(Token.TokenType.LITERAL_CARACTER)) {
                return new ASTNode(ASTNode.NodeType.CHAR_LITERAL, advance());
            }
            
            if (check(Token.TokenType.LITERAL_NUMERICO) || check(Token.TokenType.ENTERO) || check(Token.TokenType.DECIMAL)) {
                return new ASTNode(ASTNode.NodeType.NUMBER_LITERAL, advance());
            }
            
            if (check(Token.TokenType.IDENTIFICADOR)) {
                return new ASTNode(ASTNode.NodeType.IDENTIFIER, advance());
            }
            
            if (match(Token.TokenType.APERTURA, "(")) {
                ASTNode expr = parseExpression();
                consume(Token.TokenType.CIERRE, "Se esperaba ')' después de la expresión");
                return expr;
            }
            
            if (match(Token.TokenType.OPERADOR_LOGICO, "!")) {
                ASTNode factor = parseFactor();
                ASTNode negation = new ASTNode(ASTNode.NodeType.EXPRESSION, previous());
                if (factor != null) negation.addChild(factor);
                return negation;
            }
            
            error("Se esperaba una expresión", peek());
            return null;
        }
        
        private Token parseType() {
            if (isType(peek())) {
                return advance();
            }
            error("Se esperaba declaración de tipo", peek());
            return null;
        }
        
        private boolean isType(Token token) {
            if (token == null) return false;
            if (token.getType() == Token.TokenType.TIPO_DATO ||
                token.getType() == Token.TokenType.TIPO_ENTERO ||
                token.getType() == Token.TokenType.TIPO_FLOTANTE ||
                token.getType() == Token.TokenType.TIPO_CADENA_TIPO ||
                token.getType() == Token.TokenType.TIPO_CARACTER ||
                token.getType() == Token.TokenType.TIPO_BOOLEANO ||
                token.getType() == Token.TokenType.TIPO_ARREGLO) {
                return true;
            }
            if (token.getType() == Token.TokenType.PALABRA_RESERVADA && 
                token.getLexeme().equals("void")) {
                return true;
            }
            return false;
        }
        
        private boolean canStartDeclaration(Token token) {
            if (token == null) return false;
            
            if (isType(token)) {
                return true;
            }
            
            if (token.getType() == Token.TokenType.PALABRA_RESERVADA && 
                token.getLexeme().equals("const")) {
                return true;
            }
            
            return false;
        }
        
        private boolean match(Token.TokenType type, String lexeme) {
            if (check(type, lexeme)) {
                advance();
                return true;
            }
            return false;
        }
        
        private boolean check(Token.TokenType type) {
            if (isAtEnd()) return false;
            return peek().getType() == type;
        }
        
        private boolean check(Token.TokenType type, String lexeme) {
            if (isAtEnd()) return false;
            Token token = peek();
            return token.getType() == type && token.getLexeme().equals(lexeme);
        }
        
        private Token advance() {
            if (!isAtEnd()) current++;
            return previous();
        }
        
        private boolean isAtEnd() {
            return current >= tokens.size();
        }
        
        private Token peek() {
            if (current >= tokens.size()) return tokens.get(tokens.size() - 1);
            return tokens.get(current);
        }
        
        private Token previous() {
            if (current == 0) return tokens.get(0);
            return tokens.get(current - 1);
        }
        
        private Token consume(Token.TokenType type) {
            return consume(type, "Se esperaba tipo de token: " + type.name());
        }
        
        private Token consume(Token.TokenType type, String errorMessage) {
            if (check(type)) return advance();
            error(errorMessage, peek());
            return null;
        }
        
        private Token consume(Token.TokenType type, String lexeme, String errorMessage) {
            if (check(type, lexeme)) return advance();
            error(errorMessage, peek());
            return null;
        }
        
        private void error(String message, Token token) {
            String found = token != null ? token.getLexeme() : "EOF";
            int line = token != null ? token.getLine() : 0;
            int column = token != null ? token.getColumn() : 0;
            
            SyntaxError error = new SyntaxError(message, line, column, "ver mensaje de error", found);
            errors.add(error);
        }
        
        public boolean hasErrors() {
            return !errors.isEmpty();
        }
        
        public List<SyntaxError> getErrors() {
            return errors;
        }
        
        public ASTNode getAST() {
            return ast;
        }
    }
    
    private Parser parser;
    private ASTNode ast;
    private List<SyntaxError> errors;
    private boolean success;
    
    public Analisis_Sintactico() {
        this.parser = null;
        this.ast = null;
        this.errors = null;
        this.success = false;
    }
    
    public boolean analizar(List<Token> tokens) {
        if (!tokens.isEmpty() && tokens.get(tokens.size() - 1).getType() == Token.TokenType.EOF) {
            tokens = tokens.subList(0, tokens.size() - 1);
        }
        
        parser = new Parser(tokens);
        ast = parser.parse();
        errors = parser.getErrors();
        success = !parser.hasErrors();
        
        return success;
    }
    
    public ASTNode getAST() {
        return ast;
    }
    
    public List<SyntaxError> getErrors() {
        return errors;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }
    
    public int getErrorCount() {
        return errors != null ? errors.size() : 0;
    }
    
    public void printAST() {
        if (ast != null) {
            System.out.println("=== Abstract Syntax Tree ===");
            ast.printTree("");
        } else {
            System.out.println("No AST available (parsing failed)");
        }
    }
    
    public void printErrors() {
        if (errors != null && !errors.isEmpty()) {
            System.out.println("=== Syntax Errors ===");
            for (SyntaxError error : errors) {
                System.out.println(error);
            }
        } else {
            System.out.println("No syntax errors found");
        }
    }
    
    public IntermediateCodeGenerator generateIntermediateCode() {
        if (ast != null) {
            IntermediateCodeGenerator generator = new IntermediateCodeGenerator();
            generator.generate(ast);
            return generator;
        }
        return null;
    }
}
