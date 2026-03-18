/*
 * IntermediateCodeGenerator - Generates intermediate code from AST
 * Produces quadruples, prefix, and postfix notations
 */
package javaapplication4;

import java.util.ArrayList;
import java.util.List;

/**
 * Intermediate Code Generator that converts AST expressions into
 * intermediate representations (quadruples, prefix, postfix)
 * @author Gerardo
 */
public class IntermediateCodeGenerator {
    
    public static class Quadruple {
        private String operator;
        private String operand1;
        private String operand2;
        private String result;
        
        public Quadruple(String operator, String operand1, String operand2, String result) {
            this.operator = operator;
            this.operand1 = operand1;
            this.operand2 = operand2;
            this.result = result;
        }
        
        public String getOperator() { return operator; }
        public String getOperand1() { return operand1; }
        public String getOperand2() { return operand2; }
        public String getResult() { return result; }
        
        @Override
        public String toString() {
            return "(" + operator + ", " + operand1 + ", " + operand2 + ", " + result + ")";
        }
        
        public String toTableRow() {
            return operator + " | " + operand1 + " | " + operand2 + " | " + result;
        }
    }
    
    public static class ExpressionNotation {
        private String prefix;
        private String postfix;
        private String resultVar;
        
        public ExpressionNotation(String prefix, String postfix, String resultVar) {
            this.prefix = prefix;
            this.postfix = postfix;
            this.resultVar = resultVar;
        }
        
        public String getPrefix() { return prefix; }
        public String getPostfix() { return postfix; }
        public String getResultVar() { return resultVar; }
        
        @Override
        public String toString() {
            return "Prefix: " + prefix + " | Postfix: " + postfix + " | Result: " + resultVar;
        }
    }
    
    private int tempCounter;
    private List<Quadruple> quadruples;
    private List<ExpressionNotation> expressions;
    private List<String> errors;
    
    public IntermediateCodeGenerator() {
        this.tempCounter = 0;
        this.quadruples = new ArrayList<>();
        this.expressions = new ArrayList<>();
        this.errors = new ArrayList<>();
    }
    
    public void generate(ASTNode ast) {
        if (ast == null) {
            errors.add("AST is null");
            return;
        }
        
        tempCounter = 0;
        quadruples.clear();
        expressions.clear();
        errors.clear();
        
        processNode(ast);
    }
    
    private void processNode(ASTNode node) {
        if (node == null) return;
        
        switch (node.getType()) {
            case PROGRAM:
            case STRUCTURE:
            case FUNCTION:
            case STATEMENT_LIST:
                for (ASTNode child : node.getChildren()) {
                    processNode(child);
                }
                break;
                
            case ASSIGNMENT:
                processAssignment(node);
                break;
                
            case DECLARATION:
                for (ASTNode child : node.getChildren()) {
                    processNode(child);
                }
                break;
                
            case IF_STATEMENT:
            case WHILE_STATEMENT:
            case FOR_STATEMENT:
            case TRY_CATCH:
                for (ASTNode child : node.getChildren()) {
                    processNode(child);
                }
                break;
                
            case EXPRESSION:
            case TERM:
                processExpression(node);
                break;
                
            default:
                break;
        }
    }
    
    private boolean isSimpleValue(ASTNode node) {
        if (node == null) return false;
        
        ASTNode.NodeType nodeType = node.getType();
        return nodeType == ASTNode.NodeType.NUMBER_LITERAL ||
               nodeType == ASTNode.NodeType.IDENTIFIER ||
               nodeType == ASTNode.NodeType.BOOL_LITERAL ||
               nodeType == ASTNode.NodeType.STRING_LITERAL ||
               nodeType == ASTNode.NodeType.CHAR_LITERAL;
    }
    
    private void processAssignment(ASTNode node) {
        if (node.getChildCount() < 2) return;
        
        ASTNode identifierNode = node.getChild(0);
        ASTNode expressionNode = node.getChild(1);
        
        if (identifierNode == null || expressionNode == null) return;
        if (identifierNode.getType() != ASTNode.NodeType.IDENTIFIER) return;
        
        if (isSimpleValue(expressionNode)) {
            return;
        }
        
        String targetVar = identifierNode.getValue();
        
        String resultVar = processExpressionNode(expressionNode);
        
        Quadruple assignQuad = new Quadruple("=", resultVar, "_", targetVar);
        quadruples.add(assignQuad);
    }
    
    private String processExpressionNode(ASTNode node) {
        if (node == null) return "";
        
        switch (node.getType()) {
            case EXPRESSION:
            case TERM:
                return processBinaryExpression(node);
                
            case IDENTIFIER:
                return node.getValue();
                
            case NUMBER_LITERAL:
                return node.getValue();
                
            case BOOL_LITERAL:
                return node.getValue();
                
            case STRING_LITERAL:
                return node.getValue();
                
            case CHAR_LITERAL:
                return node.getValue();
                
            default:
                return "";
        }
    }
    
    private String processBinaryExpression(ASTNode node) {
        if (node.getChildCount() < 2) {
            return processExpressionNode(node);
        }
        
        ASTNode leftChild = node.getChild(0);
        ASTNode rightChild = node.getChild(1);
        
        String leftResult = processExpressionNode(leftChild);
        String rightResult = processExpressionNode(rightChild);
        
        String operator = "";
        if (node.getToken() != null) {
            operator = node.getToken().getLexeme();
        }
        
        String tempVar = getNextTemp();
        
        Quadruple quad = new Quadruple(operator, leftResult, rightResult, tempVar);
        quadruples.add(quad);
        
        String prefix = operator + " " + leftResult + " " + rightResult;
        String postfix = leftResult + " " + rightResult + " " + operator;
        
        ExpressionNotation notation = new ExpressionNotation(prefix, postfix, tempVar);
        expressions.add(notation);
        
        return tempVar;
    }
    
    private void processExpression(ASTNode node) {
        if (node.getType() != ASTNode.NodeType.EXPRESSION && 
            node.getType() != ASTNode.NodeType.TERM) {
            return;
        }
        
        String prefix = generatePrefix(node);
        String postfix = generatePostfix(node);
        String resultVar = processExpressionNode(node);
        
        ExpressionNotation notation = new ExpressionNotation(prefix, postfix, resultVar);
        expressions.add(notation);
    }
    
    public String generatePrefix(ASTNode node) {
        if (node == null) return "";
        
        switch (node.getType()) {
            case EXPRESSION:
            case TERM:
                String operator = "";
                if (node.getToken() != null) {
                    operator = node.getToken().getLexeme();
                }
                String leftPrefix = generatePrefix(node.getChild(0));
                String rightPrefix = generatePrefix(node.getChild(1));
                if (leftPrefix.isEmpty() && rightPrefix.isEmpty()) {
                    return operator;
                }
                return operator + " " + leftPrefix + " " + rightPrefix;
                
            case IDENTIFIER:
                return node.getValue() != null ? node.getValue() : "";
                
            case NUMBER_LITERAL:
                return node.getValue() != null ? node.getValue() : "";
                
            case BOOL_LITERAL:
                return node.getValue() != null ? node.getValue() : "";
                
            case STRING_LITERAL:
                return node.getValue() != null ? node.getValue() : "";
                
            case CHAR_LITERAL:
                return node.getValue() != null ? node.getValue() : "";
                
            default:
                return "";
        }
    }
    
    public String generatePostfix(ASTNode node) {
        if (node == null) return "";
        
        switch (node.getType()) {
            case EXPRESSION:
            case TERM:
                String operator = "";
                if (node.getToken() != null) {
                    operator = node.getToken().getLexeme();
                }
                String leftPostfix = generatePostfix(node.getChild(0));
                String rightPostfix = generatePostfix(node.getChild(1));
                if (leftPostfix.isEmpty() && rightPostfix.isEmpty()) {
                    return operator;
                }
                return leftPostfix + " " + rightPostfix + " " + operator;
                
            case IDENTIFIER:
                return node.getValue() != null ? node.getValue() : "";
                
            case NUMBER_LITERAL:
                return node.getValue() != null ? node.getValue() : "";
                
            case BOOL_LITERAL:
                return node.getValue() != null ? node.getValue() : "";
                
            case STRING_LITERAL:
                return node.getValue() != null ? node.getValue() : "";
                
            case CHAR_LITERAL:
                return node.getValue() != null ? node.getValue() : "";
                
            default:
                return "";
        }
    }
    
    private String getNextTemp() {
        tempCounter++;
        return "t" + tempCounter;
    }
    
    public int getTempCounter() {
        return tempCounter;
    }
    
    public List<Quadruple> getQuadruples() {
        return new ArrayList<>(quadruples);
    }
    
    public List<ExpressionNotation> getExpressions() {
        return new ArrayList<>(expressions);
    }
    
    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }
    
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
    
    public String getQuadruplesString() {
        StringBuilder sb = new StringBuilder();
        for (Quadruple quad : quadruples) {
            sb.append(quad.toString()).append("\n");
        }
        return sb.toString();
    }
    
    public String getPrefixNotationsString() {
        StringBuilder sb = new StringBuilder();
        for (ExpressionNotation expr : expressions) {
            sb.append(expr.getPrefix()).append("\n");
        }
        return sb.toString();
    }
    
    public String getPostfixNotationsString() {
        StringBuilder sb = new StringBuilder();
        for (ExpressionNotation expr : expressions) {
            sb.append(expr.getPostfix()).append("\n");
        }
        return sb.toString();
    }
    
    public void printQuadruples() {
        System.out.println("=== Quadruples ===");
        for (Quadruple quad : quadruples) {
            System.out.println(quad.toString());
        }
    }
    
    public void printNotations() {
        System.out.println("=== Expressions (Prefix / Postfix) ===");
        for (ExpressionNotation expr : expressions) {
            System.out.println(expr.toString());
        }
    }
}
