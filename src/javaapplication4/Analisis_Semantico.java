/*
 * Analisis_Semantico - Semantic analysis for assignment statements
 * Step 1: Validates that identifiers exist in the symbol table
 * Step 2: Validates that identifiers are in accessible scope
 * Step 3: Validates that identifiers are modifiable (not constant)
 * Step 4: Validates type compatibility between LHS and RHS
 * Step 5: Validates binary expression type compatibility
 */
package javaapplication4;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Analisis_Semantico {
    
    public static class SymbolTable {
        private Set<String> identifiers;
        private Map<String, Integer> identifierScopes;
        private Map<String, Boolean> constantIdentifiers;
        private Map<String, ASTNode.DataType> identifierTypes;
        private Map<String, Integer> identifierArraySizes;
        private int currentScope;
        private Set<Integer> globalScopes;
        
        public SymbolTable() {
            this.identifiers = new HashSet<>();
            this.identifierScopes = new HashMap<>();
            this.constantIdentifiers = new HashMap<>();
            this.identifierTypes = new HashMap<>();
            this.identifierArraySizes = new HashMap<>();
            this.currentScope = 0;
            this.globalScopes = new HashSet<>();
        }
        
        public void add(String identifier) {
            addInCurrentScope(identifier);
        }
        
        public void addInCurrentScope(String identifier) {
            addInCurrentScope(identifier, false, ASTNode.DataType.UNKNOWN);
        }
        
        public void addInCurrentScope(String identifier, boolean isConstant) {
            addInCurrentScope(identifier, isConstant, ASTNode.DataType.UNKNOWN);
        }
        
        public void addInCurrentScope(String identifier, boolean isConstant, String type) {
            ASTNode.DataType dt = (type != null) ? ASTNode.DataType.fromString(type) : ASTNode.DataType.UNKNOWN;
            addInCurrentScope(identifier, isConstant, dt);
        }
        
        public void addInCurrentScope(String identifier, boolean isConstant, ASTNode.DataType type) {
            addInCurrentScope(identifier, isConstant, type, 0);
        }
        
        public void addInCurrentScope(String identifier, boolean isConstant, ASTNode.DataType type, int arraySize) {
            if (identifier != null && !identifier.isEmpty()) {
                if (isInCurrentScope(identifier)) {
                    throw new IllegalArgumentException("Duplicate declaration: '" + identifier + "' already declared in current scope");
                }
                identifiers.add(identifier);
                identifierScopes.put(identifier, currentScope);
                constantIdentifiers.put(identifier, isConstant);
                identifierTypes.put(identifier, type);
                identifierArraySizes.put(identifier, arraySize);
            }
        }
        
        public void addAsGlobal(String identifier) {
            addAsGlobal(identifier, false, ASTNode.DataType.UNKNOWN);
        }
        
        public void addAsGlobal(String identifier, boolean isConstant) {
            addAsGlobal(identifier, isConstant, ASTNode.DataType.UNKNOWN);
        }
        
        public void addAsGlobal(String identifier, boolean isConstant, String type) {
            ASTNode.DataType dt = (type != null) ? ASTNode.DataType.fromString(type) : ASTNode.DataType.UNKNOWN;
            addAsGlobal(identifier, isConstant, dt);
        }
        
        private void addAsGlobal(String identifier, boolean isConstant, ASTNode.DataType type) {
            addAsGlobal(identifier, isConstant, type, 0);
        }
        
        private void addAsGlobal(String identifier, boolean isConstant, ASTNode.DataType type, int arraySize) {
            if (identifier != null && !identifier.isEmpty()) {
                if (globalScopes.contains(0) && contains(identifier) && getScopeLevel(identifier) == 0) {
                    throw new IllegalArgumentException("Duplicate declaration: '" + identifier + "' already declared in global scope");
                }
                identifiers.add(identifier);
                identifierScopes.put(identifier, 0);
                constantIdentifiers.put(identifier, isConstant);
                identifierTypes.put(identifier, type);
                identifierArraySizes.put(identifier, arraySize);
                globalScopes.add(0);
            }
        }
        
        public boolean contains(String identifier) {
            return identifier != null && identifiers.contains(identifier);
        }
        
        public int getScopeLevel(String identifier) {
            return identifierScopes.getOrDefault(identifier, -1);
        }
        
        public boolean isAccessible(String identifier, int fromScope) {
            if (!contains(identifier)) {
                return false;
            }
            int declScope = getScopeLevel(identifier);
            return declScope <= fromScope;
        }
        
        public boolean isInCurrentScope(String identifier) {
            return contains(identifier) && getScopeLevel(identifier) == currentScope;
        }
        
        public boolean isInInnerScope(String identifier, int fromScope) {
            if (!contains(identifier)) {
                return false;
            }
            int declScope = getScopeLevel(identifier);
            return declScope > fromScope;
        }
        
        public boolean isConstant(String identifier) {
            return constantIdentifiers.getOrDefault(identifier, false);
        }
        
        public boolean isMutable(String identifier) {
            return !isConstant(identifier);
        }
        
        public ASTNode.DataType getType(String identifier) {
            return identifierTypes.getOrDefault(identifier, ASTNode.DataType.UNKNOWN);
        }
        
        public int getArraySize(String identifier) {
            return identifierArraySizes.getOrDefault(identifier, 0);
        }
        
        public boolean isArray(String identifier) {
            return getArraySize(identifier) > 0;
        }
        
        public void enterScope() {
            currentScope++;
        }
        
        public void enterScope(int scopeLevel) {
            if (scopeLevel > currentScope) {
                currentScope = scopeLevel;
            }
        }
        
        public void exitScope() {
            if (currentScope > 0) {
                currentScope--;
            }
        }
        
        public int getCurrentScope() {
            return currentScope;
        }
        
        public void setCurrentScope(int scope) {
            this.currentScope = scope;
        }
        
        public void clear() {
            identifiers.clear();
            identifierScopes.clear();
            constantIdentifiers.clear();
            identifierTypes.clear();
            identifierArraySizes.clear();
            currentScope = 0;
            globalScopes.clear();
        }
        
        public int size() {
            return identifiers.size();
        }
        
        public boolean isEmpty() {
            return identifiers.isEmpty();
        }
        
        public Map<String, Integer> getAllIdentifierScopes() {
            return new HashMap<>(identifierScopes);
        }
    }
    
    public static class SemanticError {
        private String identifier;
        private String message;
        private int line;
        private int column;
        
        public SemanticError(String identifier, String message, int line, int column) {
            this.identifier = identifier;
            this.message = message;
            this.line = line;
            this.column = column;
        }
        
        public String getIdentifier() { return identifier; }
        public String getMessage() { return message; }
        public int getLine() { return line; }
        public int getColumn() { return column; }
        
        @Override
        public String toString() {
            return String.format("Error en linea %d, columna %d: %s (identificador: '%s')",
                line, column, message, identifier);
        }
    }
    
    private enum OperatorCategory {
        ARITHMETIC,
        LOGICAL,
        RELATIONAL,
        EQUALITY
    }
    
    private SymbolTable symbolTable;
    private List<SemanticError> errors;
    private boolean success;
    
    public Analisis_Semantico() {
        this.symbolTable = new SymbolTable();
        this.errors = new ArrayList<>();
        this.success = false;
    }
    
    public boolean validarIdentificadorAsignacion(String identifierName, SymbolTable table) {
        if (identifierName == null || identifierName.isEmpty()) {
            return false;
        }
        
        if (!table.contains(identifierName)) {
            SemanticError error = new SemanticError(
                identifierName,
                "La variable no ha sido declarada",
                0,
                0
            );
            errors.add(error);
            return false;
        }
        
        return true;
    }
    
    public boolean validarAlcanceAsignacion(String identifierName, int currentScope, SymbolTable table) {
        if (identifierName == null || identifierName.isEmpty()) {
            return false;
        }
        
        if (!table.contains(identifierName)) {
            return false;
        }
        
        int declScope = table.getScopeLevel(identifierName);
        
        if (declScope > currentScope) {
            SemanticError error = new SemanticError(
                identifierName,
                "La variable '" + identifierName + "' esta fuera de alcance (declarada en un bloque interno)",
                0,
                0
            );
            errors.add(error);
            return false;
        }
        
        return true;
    }
    
    public boolean validarModificabilidadAsignacion(String identifierName, SymbolTable table) {
        if (identifierName == null || identifierName.isEmpty()) {
            return false;
        }
        
        if (!table.contains(identifierName)) {
            return false;
        }
        
        if (table.isConstant(identifierName)) {
            SemanticError error = new SemanticError(
                identifierName,
                "La variable '" + identifierName + "' es una constante y no puede ser modificada",
                0,
                0
            );
            errors.add(error);
            return false;
        }
        
        return true;
    }
    
    public boolean validarTiposAsignacion(String identifierName, ASTNode expressionNode, SymbolTable table) {
        if (identifierName == null || identifierName.isEmpty() || expressionNode == null) {
            return false;
        }
        
        if (!table.contains(identifierName)) {
            return false;
        }
        
        ASTNode.DataType declaredType = table.getType(identifierName);
        ASTNode.DataType expressionType = inferExpressionType(expressionNode, table);
        
        if (declaredType == null || expressionType == null || 
            declaredType == ASTNode.DataType.UNKNOWN || expressionType == ASTNode.DataType.UNKNOWN) {
            return false;
        }
        
        if (declaredType != expressionType) {
            if (!isTypeCompatible(declaredType, expressionType)) {
                SemanticError error = new SemanticError(
                    identifierName,
                    "Tipo incompatible: se esperaba '" + declaredType.getTypeName() + "' pero se obtuvo '" + expressionType.getTypeName() + "'",
                    0,
                    0
                );
                errors.add(error);
                return false;
            }
            return true;
        }
        
        return true;
    }
    
    private boolean validateExpressionScope(ASTNode node, int currentScope, SymbolTable table) {
        if (node == null) {
            return true;
        }
        
        switch (node.getType()) {
            case IDENTIFIER:
                String identifierName = node.getValue();
                if (identifierName != null) {
                    if (!table.contains(identifierName)) {
                        SemanticError error = new SemanticError(
                            identifierName,
                            "La variable '" + identifierName + "' no ha sido declarada",
                            node.getLine(),
                            node.getColumn()
                        );
                        errors.add(error);
                        return false;
                    }
                    int declScope = table.getScopeLevel(identifierName);
                    if (declScope > currentScope) {
                        SemanticError error = new SemanticError(
                            identifierName,
                            "La variable '" + identifierName + "' esta fuera de alcance (declarada en un bloque interno)",
                            node.getLine(),
                            node.getColumn()
                        );
                        errors.add(error);
                        return false;
                    }
                }
                return true;
                
            case NUMBER_LITERAL:
            case STRING_LITERAL:
            case CHAR_LITERAL:
            case BOOL_LITERAL:
                return true;
                
            case EXPRESSION:
            case TERM:
                for (ASTNode child : node.getChildren()) {
                    if (!validateExpressionScope(child, currentScope, table)) {
                        return false;
                    }
                }
                return true;
                
            default:
                return true;
        }
    }
    
    private ASTNode.DataType inferExpressionType(ASTNode node, SymbolTable table) {
        if (node == null) {
            return ASTNode.DataType.UNKNOWN;
        }
        
        switch (node.getType()) {
            case IDENTIFIER:
                return table.getType(node.getValue());
                
            case NUMBER_LITERAL:
                return inferNumericLiteralType(node);
                
            case STRING_LITERAL:
                return ASTNode.DataType.STRING;
                
            case CHAR_LITERAL:
                return ASTNode.DataType.CHAR;
                
            case BOOL_LITERAL:
                return ASTNode.DataType.BOOL;
                
            case EXPRESSION:
            case TERM:
                return inferBinaryExpressionType(node, table);
                
            default:
                return ASTNode.DataType.UNKNOWN;
        }
    }
    
    private ASTNode.DataType inferNumericLiteralType(ASTNode node) {
        if (node == null || node.getValue() == null) {
            return ASTNode.DataType.NUMBER;
        }
        
        String value = node.getValue();
        
        if (value.contains(".") || value.contains("e") || value.contains("E")) {
            if (value.toLowerCase().endsWith("f")) {
                return ASTNode.DataType.FLOAT;
            }
            return ASTNode.DataType.DOUBLE;
        }
        
        try {
            Long.parseLong(value);
            return ASTNode.DataType.INT;
        } catch (NumberFormatException e) {
            return ASTNode.DataType.NUMBER;
        }
    }
    
    private ASTNode.DataType inferBinaryExpressionType(ASTNode node, SymbolTable table) {
        if (node.getChildCount() < 2) {
            return ASTNode.DataType.UNKNOWN;
        }
        
        ASTNode left = node.getChild(0);
        ASTNode right = node.getChild(1);
        
        ASTNode.DataType leftType = inferExpressionType(left, table);
        ASTNode.DataType rightType = inferExpressionType(right, table);
        
        if (leftType == ASTNode.DataType.UNKNOWN || rightType == ASTNode.DataType.UNKNOWN) {
            return ASTNode.DataType.UNKNOWN;
        }
        
        String operator = node.getToken() != null ? node.getToken().getLexeme() : null;
        if (operator == null) {
            return ASTNode.DataType.UNKNOWN;
        }
        
        OperatorCategory category = getOperatorCategory(operator);
        
        switch (category) {
            case ARITHMETIC:
                return validateArithmeticOperation(operator, leftType, rightType, node);
            case LOGICAL:
                return validateLogicalOperation(operator, leftType, rightType, node);
            case RELATIONAL:
                return validateRelationalOperation(operator, leftType, rightType, node);
            case EQUALITY:
                return validateEqualityOperation(operator, leftType, rightType, node);
            default:
                return ASTNode.DataType.UNKNOWN;
        }
    }
    
    private ASTNode.DataType validateArithmeticOperation(String operator, ASTNode.DataType leftType, ASTNode.DataType rightType, ASTNode node) {
        if (!isNumericType(leftType) || !isNumericType(rightType)) {
            generateBinaryOpError(operator, leftType, rightType, node);
            return ASTNode.DataType.UNKNOWN;
        }
        
        if (leftType != rightType) {
            generateBinaryOpError(operator, leftType, rightType, node);
            return ASTNode.DataType.UNKNOWN;
        }
        
        return leftType;
    }
    
    private ASTNode.DataType validateLogicalOperation(String operator, ASTNode.DataType leftType, ASTNode.DataType rightType, ASTNode node) {
        if (leftType != ASTNode.DataType.BOOL || rightType != ASTNode.DataType.BOOL) {
            generateBinaryOpError(operator, leftType, rightType, node);
            return ASTNode.DataType.UNKNOWN;
        }
        
        return ASTNode.DataType.BOOL;
    }
    
    private ASTNode.DataType validateRelationalOperation(String operator, ASTNode.DataType leftType, ASTNode.DataType rightType, ASTNode node) {
        if (!isNumericType(leftType) || !isNumericType(rightType)) {
            generateBinaryOpError(operator, leftType, rightType, node);
            return ASTNode.DataType.UNKNOWN;
        }
        
        if (leftType != rightType) {
            generateBinaryOpError(operator, leftType, rightType, node);
            return ASTNode.DataType.UNKNOWN;
        }
        
        return ASTNode.DataType.BOOL;
    }
    
    private ASTNode.DataType validateEqualityOperation(String operator, ASTNode.DataType leftType, ASTNode.DataType rightType, ASTNode node) {
        if (leftType != rightType) {
            generateBinaryOpError(operator, leftType, rightType, node);
            return ASTNode.DataType.UNKNOWN;
        }
        
        return ASTNode.DataType.BOOL;
    }
    
    private void generateBinaryOpError(String operator, ASTNode.DataType leftType, ASTNode.DataType rightType, ASTNode node) {
        String errorMsg = "Operador '" + operator + "' no aplicable a operandos de tipo '" + 
                         leftType.getTypeName() + "' y '" + rightType.getTypeName() + "'";
        
        int line = node.getToken() != null ? node.getToken().getLine() : 0;
        int column = node.getToken() != null ? node.getToken().getColumn() : 0;
        
        SemanticError error = new SemanticError("expresion", errorMsg, line, column);
        errors.add(error);
    }
    
    private boolean isNumericType(ASTNode.DataType type) {
        return type == ASTNode.DataType.INT || type == ASTNode.DataType.FLOAT || 
               type == ASTNode.DataType.DOUBLE || type == ASTNode.DataType.NUMBER;
    }
    
    private boolean isTypeCompatible(ASTNode.DataType declaredType, ASTNode.DataType expressionType) {
        if (declaredType == expressionType) {
            return true;
        }
        
        if (declaredType == ASTNode.DataType.FLOAT && expressionType == ASTNode.DataType.DOUBLE) {
            return true;
        }
        
        return false;
    }
    
    private OperatorCategory getOperatorCategory(String operator) {
        if (operator == null) {
            return null;
        }
        
        switch (operator) {
            case "+":
            case "-":
            case "*":
            case "/":
                return OperatorCategory.ARITHMETIC;
            case "&&":
            case "||":
                return OperatorCategory.LOGICAL;
            case "<":
            case ">":
            case "<=":
            case ">=":
                return OperatorCategory.RELATIONAL;
            case "==":
            case "!=":
                return OperatorCategory.EQUALITY;
            default:
                return null;
        }
    }
    
    public boolean analizar(ASTNode ast) {
        errors.clear();
        symbolTable.clear();
        success = false;
        
        if (ast == null) {
            return false;
        }
        
        buildSymbolTable(ast);
        validateAssignments(ast);
        
        success = errors.isEmpty();
        return success;
    }
    
    private void buildSymbolTable(ASTNode node) {
        if (node == null) return;
        
        if (node.getType() == ASTNode.NodeType.DECLARATION) {
            boolean isConst = node.isConstant();
            ASTNode.DataType varType = node.getDataType();
            String identifierName = null;
            ASTNode identifierNode = null;
            int arraySize = node.getArraySize();
            ASTNode.DataType baseType = ASTNode.DataType.UNKNOWN;
            
            for (int i = 0; i < node.getChildCount(); i++) {
                ASTNode child = node.getChild(i);
                if (child.getType() == ASTNode.NodeType.TYPE) {
                    String typeName = child.getValue();
                    if (typeName != null) {
                        baseType = ASTNode.DataType.fromString(typeName);
                    }
                    break;
                }
            }
            
            if (arraySize > 0) {
                if (baseType == ASTNode.DataType.UNKNOWN) {
                    SemanticError error = new SemanticError(
                        identifierName != null ? identifierName : "unknown",
                        "Tipo de array inválido",
                        node.getLine(),
                        node.getColumn()
                    );
                    errors.add(error);
                } else if (baseType == ASTNode.DataType.ARRAY) {
                    SemanticError error = new SemanticError(
                        identifierName != null ? identifierName : "unknown",
                        "No se permiten arrays de arrays (arrays multidimensionales)",
                        node.getLine(),
                        node.getColumn()
                    );
                    errors.add(error);
                } else if (!baseType.isPrimitive()) {
                    SemanticError error = new SemanticError(
                        identifierName != null ? identifierName : "unknown",
                        "El tipo base del array debe ser un tipo primitivo (int, float, double, string, char, bool)",
                        node.getLine(),
                        node.getColumn()
                    );
                    errors.add(error);
                }
                
                varType = ASTNode.DataType.ARRAY;
            }
            
            for (int i = 0; i < node.getChildCount(); i++) {
                ASTNode child = node.getChild(i);
                if (child.getType() == ASTNode.NodeType.IDENTIFIER) {
                    identifierName = child.getValue();
                    identifierNode = child;
                    if (identifierName != null) {
                        try {
                            symbolTable.addInCurrentScope(identifierName, isConst, varType, arraySize);
                        } catch (IllegalArgumentException e) {
                            SemanticError error = new SemanticError(
                                identifierName,
                                e.getMessage(),
                                node.getLine(),
                                node.getColumn()
                            );
                            errors.add(error);
                        }
                    }
                }
            }
            
            if (identifierName != null && varType != null) {
                for (int i = 0; i < node.getChildCount(); i++) {
                    ASTNode child = node.getChild(i);
                    if (child.getType() == ASTNode.NodeType.EXPRESSION || 
                        child.getType() == ASTNode.NodeType.NUMBER_LITERAL ||
                        child.getType() == ASTNode.NodeType.STRING_LITERAL ||
                        child.getType() == ASTNode.NodeType.CHAR_LITERAL ||
                        child.getType() == ASTNode.NodeType.BOOL_LITERAL ||
                        child.getType() == ASTNode.NodeType.IDENTIFIER ||
                        child.getType() == ASTNode.NodeType.TERM ||
                        child.getType() == ASTNode.NodeType.FACTOR ||
                        child.getType() == ASTNode.NodeType.ARRAY_INITIALIZER) {
                        
                        if (child.getType() == ASTNode.NodeType.ARRAY_INITIALIZER) {
                            int initCount = child.getChildCount();
                            if (arraySize > 0 && initCount != arraySize) {
                                SemanticError error = new SemanticError(
                                    identifierName,
                                    "El número de elementos en la inicialización (" + initCount + ") debe coincidir con el tamaño del array (" + arraySize + ")",
                                    child.getLine(),
                                    child.getColumn()
                                );
                                errors.add(error);
                            }
                            for (int j = 0; j < child.getChildCount(); j++) {
                                ASTNode element = child.getChild(j);
                                ASTNode.DataType elementType = inferExpressionType(element, symbolTable);
                                if (elementType != ASTNode.DataType.UNKNOWN && baseType != ASTNode.DataType.UNKNOWN && elementType != baseType) {
                                    if (!isTypeCompatible(baseType, elementType)) {
                                        SemanticError error = new SemanticError(
                                            identifierName,
                                            "Tipo incompatible en elemento " + (j+1) + ": se esperaba '" + baseType.getTypeName() + "' pero se obtuvo '" + elementType.getTypeName() + "'",
                                            element.getLine(),
                                            element.getColumn()
                                        );
                                        errors.add(error);
                                    }
                                }
                            }
                        } else {
                            ASTNode.DataType initType = inferExpressionType(child, symbolTable);

                            if (varType != ASTNode.DataType.UNKNOWN && initType != ASTNode.DataType.UNKNOWN && varType != initType) {
                                if (!isTypeCompatible(varType, initType)) {
                                    SemanticError error = new SemanticError(
                                        identifierName,
                                        "Tipo incompatible: se esperaba '" + varType.getTypeName() + "' pero se obtuvo '" + initType.getTypeName() + "'",
                                        node.getLine(),
                                        node.getColumn()
                                    );
                                    errors.add(error);
                                }
                            }
                        }
                    }
                }
            }
        }
        
        if (isBlockNode(node.getType())) {
            symbolTable.enterScope();
        }
        
        for (ASTNode child : node.getChildren()) {
            buildSymbolTable(child);
        }
        
        if (isBlockNode(node.getType())) {
            symbolTable.exitScope();
        }
    }
    
    private boolean isBlockNode(ASTNode.NodeType type) {
        return type == ASTNode.NodeType.IF_STATEMENT ||
               type == ASTNode.NodeType.WHILE_STATEMENT ||
               type == ASTNode.NodeType.FOR_STATEMENT ||
               type == ASTNode.NodeType.TRY_CATCH ||
               type == ASTNode.NodeType.FUNCTION;
    }
    
    private void validateAssignments(ASTNode node) {
        if (node == null) return;
        
        if (node.getType() == ASTNode.NodeType.ASSIGNMENT) {
            if (node.getChildCount() > 0) {
                ASTNode identifierNode = node.getChild(0);
                if (identifierNode.getType() == ASTNode.NodeType.IDENTIFIER) {
                    String identifierName = identifierNode.getValue();
                    
                    boolean step1Valid = validarIdentificadorAsignacion(identifierName, symbolTable);
                    
                    if (step1Valid) {
                        int currentScope = symbolTable.getCurrentScope();
                        boolean step2Valid = validarAlcanceAsignacion(identifierName, currentScope, symbolTable);
                        
                        if (!step2Valid) {
                            SemanticError error = new SemanticError(
                                identifierName,
                                "La variable '" + identifierName + "' esta fuera de alcance",
                                identifierNode.getLine(),
                                identifierNode.getColumn()
                            );
                            errors.add(error);
                        } else {
                            boolean step3Valid = validarModificabilidadAsignacion(identifierName, symbolTable);
                            
                            if (!step3Valid) {
                                SemanticError error = new SemanticError(
                                    identifierName,
                                    "La variable '" + identifierName + "' es una constante y no puede ser modificada",
                                    identifierNode.getLine(),
                                    identifierNode.getColumn()
                                );
                                errors.add(error);
                            } else {
                                if (node.getChildCount() > 1) {
                                    ASTNode expressionNode = node.getChild(1);
                                    boolean exprScopeValid = validateExpressionScope(expressionNode, currentScope, symbolTable);
                                    
                                    if (exprScopeValid) {
                                        boolean step4Valid = validarTiposAsignacion(identifierName, expressionNode, symbolTable);

                                        if (!step4Valid) {
                                            SemanticError error = new SemanticError(
                                                identifierName,
                                                "Tipo incompatible en la asignacion",
                                                identifierNode.getLine(),
                                                identifierNode.getColumn()
                                            );
                                            errors.add(error);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        SemanticError error = new SemanticError(
                            identifierName,
                            "La variable '" + identifierName + "' no ha sido declarada",
                            identifierNode.getLine(),
                            identifierNode.getColumn()
                        );
                        errors.add(error);
                    }
                }
            }
        }
        
        if (isBlockNode(node.getType())) {
            symbolTable.enterScope();
        }
        
        for (ASTNode child : node.getChildren()) {
            validateAssignments(child);
        }
        
        if (isBlockNode(node.getType())) {
            symbolTable.exitScope();
        }
    }
    
    public List<SemanticError> getErrors() {
        return errors;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public SymbolTable getSymbolTable() {
        return symbolTable;
    }
}
