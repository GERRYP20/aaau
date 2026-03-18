/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication4;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract Syntax Tree Node for syntactic analysis
 * Represents nodes in the parse tree
 * @author Gerardo
 */
public class ASTNode {
    
    public enum NodeType {
        PROGRAM,
        STRUCTURE,
        FUNCTION,
        PARAMETER_LIST,
        STATEMENT_LIST,
        DECLARATION,
        ASSIGNMENT,
        IF_STATEMENT,
        WHILE_STATEMENT,
        FOR_STATEMENT,
        TRY_CATCH,
        THROW_STATEMENT,
        BREAK_STATEMENT,
        CONTINUE_STATEMENT,
        RETURN_STATEMENT,
        EXPRESSION,
        TERM,
        FACTOR,
        IDENTIFIER,
        NUMBER_LITERAL,
        STRING_LITERAL,
        CHAR_LITERAL,
        BOOL_LITERAL,
        FUNCTION_CALL,
        ARGUMENT_LIST,
        ARRAY_INITIALIZER,
        TYPE,
        ACCESS_MODIFIER,
        ERROR
    }
    
    public static enum DataType {
        INT("int"),
        FLOAT("float"),
        DOUBLE("double"),
        STRING("string"),
        CHAR("char"),
        BOOL("bool"),
        NUMBER("number"),
        ARRAY("array"),
        UNKNOWN(null);
        
        private final String typeName;
        
        DataType(String typeName) {
            this.typeName = typeName;
        }
        
        public String getTypeName() {
            return typeName;
        }
        
        public static DataType fromString(String typeName) {
            if (typeName == null) {
                return UNKNOWN;
            }
            
            switch (typeName.toLowerCase()) {
                case "int": return INT;
                case "float": return FLOAT;
                case "double": return DOUBLE;
                case "string": return STRING;
                case "char": return CHAR;
                case "bool": return BOOL;
                case "number": return NUMBER;
                case "array": return ARRAY;
                default: return UNKNOWN;
            }
        }
        
        public boolean isPrimitive() {
            return this == INT || this == FLOAT || this == DOUBLE || 
                   this == STRING || this == CHAR || this == BOOL || this == NUMBER;
        }

        public static class TypeInfo {
            private final DataType type;
            private final DataType arrayBaseType;
            private final int arraySize;

            private TypeInfo(DataType type, DataType arrayBaseType, int arraySize) {
                this.type = type;
                this.arrayBaseType = arrayBaseType;
                this.arraySize = arraySize;
            }

            public static TypeInfo createPrimitiveType(DataType primitiveType) {
                return new TypeInfo(primitiveType, null, 0);
            }

            public static TypeInfo createArrayType(DataType baseType, int size) {
                if (baseType == null || !baseType.isPrimitive()) {
                    throw new IllegalArgumentException("Array base type must be a primitive type");
                }
                if (size <= 0) {
                    throw new IllegalArgumentException("Array size must be positive");
                }
                return new TypeInfo(DataType.ARRAY, baseType, size);
            }

            public DataType getType() { return type; }
            public DataType getArrayBaseType() { return arrayBaseType; }
            public int getArraySize() { return arraySize; }

            public boolean isArray() {
                return type == DataType.ARRAY && arrayBaseType != null && arraySize > 0;
            }

            public boolean isPrimitiveType() {
                return type != null && type.isPrimitive();
            }

            @Override
            public String toString() {
                if (isArray()) {
                    return arrayBaseType.getTypeName() + "[" + arraySize + "]";
                }
                return type != null ? type.getTypeName() : "unknown";
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (!(obj instanceof TypeInfo)) return false;
                TypeInfo other = (TypeInfo) obj;
                if (type != other.type) return false;
                if (arrayBaseType != other.arrayBaseType) return false;
                return arraySize == other.arraySize;
            }

            @Override
            public int hashCode() {
                int result = type != null ? type.hashCode() : 0;
                result = 31 * result + (arrayBaseType != null ? arrayBaseType.hashCode() : 0);
                result = 31 * result + arraySize;
                return result;
            }
        }
    }
    
    private NodeType type;
    private Token token;
    private List<ASTNode> children;
    private int line;
    private int column;
    private String value;
    private boolean isConstant;
    private DataType dataType;
    private int arraySize;
    
    public ASTNode(NodeType type) {
        this.type = type;
        this.children = new ArrayList<>();
        this.line = 0;
        this.column = 0;
        this.value = null;
        this.isConstant = false;
        this.dataType = DataType.UNKNOWN;
        this.arraySize = 0;
    }
    
    public ASTNode(NodeType type, Token token) {
        this.type = type;
        this.token = token;
        this.children = new ArrayList<>();
        this.line = token != null ? token.getLine() : 0;
        this.column = token != null ? token.getColumn() : 0;
        this.value = token != null ? token.getLexeme() : null;
        this.isConstant = false;
        this.dataType = DataType.UNKNOWN;
        this.arraySize = 0;
    }
    
    public ASTNode(NodeType type, String value, int line, int column) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.column = column;
        this.children = new ArrayList<>();
        this.isConstant = false;
        this.dataType = DataType.UNKNOWN;
        this.arraySize = 0;
    }
    
    public NodeType getType() { return type; }
    public void setType(NodeType type) { this.type = type; }
    public Token getToken() { return token; }
    public void setToken(Token token) {
        this.token = token;
        if (token != null) {
            this.line = token.getLine();
            this.column = token.getColumn();
            this.value = token.getLexeme();
        }
    }
    public List<ASTNode> getChildren() { return children; }
    public void addChild(ASTNode child) {
        if (child != null) {
            children.add(child);
        }
    }
    public ASTNode getChild(int index) {
        if (index >= 0 && index < children.size()) {
            return children.get(index);
        }
        return null;
    }
    public int getChildCount() { return children.size(); }
    public int getLine() { return line; }
    public void setLine(int line) { this.line = line; }
    public int getColumn() { return column; }
    public void setColumn(int column) { this.column = column; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public boolean isConstant() { return isConstant; }
    public void setConstant(boolean isConstant) { this.isConstant = isConstant; }
    public DataType getDataType() { return dataType; }
    public void setDataType(DataType dataType) { this.dataType = dataType; }
    public void setDataType(String typeName) {
        this.dataType = DataType.fromString(typeName);
    }
    public int getArraySize() { return arraySize; }
    public void setArraySize(int arraySize) { this.arraySize = arraySize; }
    public boolean isArray() { return arraySize > 0; }
    public boolean isError() { return type == NodeType.ERROR; }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(type.name());
        if (value != null && !value.isEmpty()) {
            sb.append(" [").append(value).append("]");
        }
        sb.append(" (line:").append(line).append(")");
        return sb.toString();
    }
    
    public void printTree(String indent) {
        System.out.println(indent + this.toString());
        for (ASTNode child : children) {
            child.printTree(indent + "  ");
        }
    }
}
