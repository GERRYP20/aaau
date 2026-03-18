/*
 * IntermediateWindow - Modal dialog for displaying intermediate code results
 * Shows quadruples, prefix and postfix notations
 */
package javaapplication4;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Gerardo
 */
public class IntermediateWindow extends javax.swing.JDialog {

    private DefaultTableModel quadrupleModel;
    private IntermediateCodeGenerator generator;
    
    /**
     * Creates new form IntermediateWindow
     */
    public IntermediateWindow(java.awt.Frame parent, boolean modal, IntermediateCodeGenerator generator) {
        super(parent, modal);
        this.generator = generator;
        initComponents();
        populateData();
        this.setLocationRelativeTo(parent);
    }

    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        QTbQuadruplos = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TxPrefijo = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TxPosfijo = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CODIGO INTERMEDIO");
        setModal(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(900, 600));
        setPreferredSize(new java.awt.Dimension(900, 600));

        quadrupleModel = new DefaultTableModel();
        String[] columns = {"No.", "Operador", "Arg 1", "Arg 2", "Resultado"};
        quadrupleModel.setColumnIdentifiers(columns);
        QTbQuadruplos.setModel(quadrupleModel);
        
        QTbQuadruplos.setFont(new java.awt.Font("STXihei", 0, 12));
        QTbQuadruplos.setRowHeight(22);
        QTbQuadruplos.getColumnModel().getColumn(0).setPreferredWidth(40);
        QTbQuadruplos.getColumnModel().getColumn(1).setPreferredWidth(80);
        QTbQuadruplos.getColumnModel().getColumn(2).setPreferredWidth(150);
        QTbQuadruplos.getColumnModel().getColumn(3).setPreferredWidth(150);
        QTbQuadruplos.getColumnModel().getColumn(4).setPreferredWidth(150);
        
        jScrollPane1.setViewportView(QTbQuadruplos);

        jLabel1.setFont(new java.awt.Font("STXihei", 0, 18));
        jLabel1.setText("Cuádruplos");

        jLabel2.setFont(new java.awt.Font("STXihei", 0, 14));
        jLabel2.setText("Notación Prefija (PRE)");

        TxPrefijo.setColumns(20);
        TxPrefijo.setEditable(false);
        TxPrefijo.setFont(new java.awt.Font("STXihei", 0, 12));
        TxPrefijo.setRows(10);
        jScrollPane2.setViewportView(TxPrefijo);

        jLabel3.setFont(new java.awt.Font("STXihei", 0, 14));
        jLabel3.setText("Notación Posfija (POST)");

        TxPosfijo.setColumns(20);
        TxPosfijo.setEditable(false);
        TxPosfijo.setFont(new java.awt.Font("STXihei", 0, 12));
        TxPosfijo.setRows(10);
        jScrollPane3.setViewportView(TxPosfijo);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 850, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE))
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE))))
                .addGap(20, 20, 20))
        );
        
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );

        pack();
    }
    
    private void populateData() {
        if (generator == null) {
            return;
        }
        
        // Populate quadruples table
        java.util.List<IntermediateCodeGenerator.Quadruple> quads = generator.getQuadruples();
        int counter = 1;
        for (IntermediateCodeGenerator.Quadruple quad : quads) {
            Object[] row = new Object[5];
            row[0] = counter++;
            row[1] = quad.getOperator();
            row[2] = quad.getOperand1();
            row[3] = quad.getOperand2();
            row[4] = quad.getResult();
            quadrupleModel.addRow(row);
        }
        
        // Populate prefix notation
        StringBuilder prefixBuilder = new StringBuilder();
        java.util.List<IntermediateCodeGenerator.ExpressionNotation> expressions = generator.getExpressions();
        for (IntermediateCodeGenerator.ExpressionNotation expr : expressions) {
            prefixBuilder.append(expr.getPrefix()).append("\n");
        }
        TxPrefijo.setText(prefixBuilder.toString());
        
        // Populate postfix notation
        StringBuilder postfixBuilder = new StringBuilder();
        for (IntermediateCodeGenerator.ExpressionNotation expr : expressions) {
            postfixBuilder.append(expr.getPostfix()).append("\n");
        }
        TxPosfijo.setText(postfixBuilder.toString());
    }

    // Variables declaration
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable QTbQuadruplos;
    private javax.swing.JTextArea TxPrefijo;
    private javax.swing.JTextArea TxPosfijo;
}
