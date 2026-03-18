/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package javaapplication4;

import javax.swing.table.DefaultTableModel;
import java.util.List;
// Uncomment these imports after adding RSyntaxTextArea library to project:
// import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
// import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
// import org.fife.ui.rtextarea.RTextScrollPane;

/**
 *
 * @author Gerardo
 */
public class PrbLexico extends javax.swing.JFrame
{
    DefaultTableModel mt = new DefaultTableModel();
    DefaultTableModel syntaxErrorModel = new DefaultTableModel();
    DefaultTableModel semanticErrorModel = new DefaultTableModel();
    int vecSal[];
    private LineNumberPanel lineNumberPanel;

    /**
     * Creates new form PrbLexico
     */
    public PrbLexico()
    {
        initComponents();
        setupLineNumbers();
        setupSyntaxErrorTable();
        setupSemanticErrorTable();
        this.setLocationRelativeTo(null);
        String campos[] = {"Lexema", "Nombre", "Numero"};
        mt.setColumnIdentifiers(campos);
        TbAnalisis.setModel(mt);
    }

    /**
     * Setup syntax error table
     */
    private void setupSyntaxErrorTable()
    {
        String errorColumns[] = {"Linea", "Columna", "Error", "Esperado", "Encontrado"};
        syntaxErrorModel.setColumnIdentifiers(errorColumns);
        TbErroresSintacticos.setModel(syntaxErrorModel);
        
        // Set column widths - Error column wider to show full messages
        TbErroresSintacticos.getColumnModel().getColumn(0).setPreferredWidth(50);   // Linea
        TbErroresSintacticos.getColumnModel().getColumn(1).setPreferredWidth(70);   // Columna
        TbErroresSintacticos.getColumnModel().getColumn(2).setPreferredWidth(350);  // Error (wider!)
        TbErroresSintacticos.getColumnModel().getColumn(3).setPreferredWidth(100);  // Esperado
        TbErroresSintacticos.getColumnModel().getColumn(4).setPreferredWidth(100);  // Encontrado
    }

    private void setupSemanticErrorTable()
    {
        String semanticColumns[] = {"Linea", "Columna", "Error", "Descripcion"};
        semanticErrorModel.setColumnIdentifiers(semanticColumns);
        TbErroresSemanticos.setModel(semanticErrorModel);
        
        TbErroresSemanticos.getColumnModel().getColumn(0).setPreferredWidth(50);   // Linea
        TbErroresSemanticos.getColumnModel().getColumn(1).setPreferredWidth(70);   // Columna
        TbErroresSemanticos.getColumnModel().getColumn(2).setPreferredWidth(200);   // Error
        TbErroresSemanticos.getColumnModel().getColumn(3).setPreferredWidth(300);   // Descripcion
    }

    /**
     * Setup line numbers for the text editor using Swing's native RowHeaderView
     * This creates a physical link between the line numbers and text area scrolling
     */
    private void setupLineNumbers()
    {
        // Create line number panel - it will be attached to scroll pane as row header
        lineNumberPanel = new LineNumberPanel(TxAnalisis);

        // Set text area as the main viewport view
        jScrollPane1.setViewportView(TxAnalisis);

        // Assign line number panel as RowHeaderView
        // Swing automatically synchronizes scrolling between row header and viewport
        jScrollPane1.setRowHeaderView(lineNumberPanel);
    }

    /**
     * Configures the editor with RSyntaxTextArea for syntax highlighting.
     * Call this method in the constructor after initComponents().
     * 
     * IMPORTANT: You must add the RSyntaxTextArea library to your project first:
     * 1. Download rsyntaxtextarea-3.x.x.jar from https://github.com/bobbylight/RSyntaxTextArea/releases
     * 2. In NetBeans: Right-click project → Properties → Libraries → Compile → Add JAR/Folder
     * 3. Uncomment the imports at the top of this file
     * 4. Uncomment this method's body
     */

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TxAnalisis = new javax.swing.JTextArea();
        BtnCargar = new javax.swing.JButton();
        BtnGuardar = new javax.swing.JButton();
        BtnGuardarComo = new javax.swing.JButton();
        BtnGenera = new javax.swing.JButton();
        BtnSintactico = new javax.swing.JButton();
        BtnSemantico = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        TbAnalisis = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        TbErroresSintacticos = new javax.swing.JTable();
        TbErroresSemanticos = new javax.swing.JTable();
        BtnIntermedio = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("STXihei", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Tabla de análisis");

        TxAnalisis.setColumns(20);
        TxAnalisis.setRows(5);
        jScrollPane1.setViewportView(TxAnalisis);

        BtnCargar.setBackground(new java.awt.Color(102, 102, 102));
        BtnCargar.setFont(new java.awt.Font("STXihei", 0, 14)); // NOI18N
        BtnCargar.setForeground(new java.awt.Color(255, 255, 255));
        BtnCargar.setText("Cargar Texto");
        BtnCargar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnCargarMouseClicked(evt);
            }
        });
        BtnCargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCargarActionPerformed(evt);
            }
        });

        BtnGuardar.setBackground(new java.awt.Color(102, 102, 102));
        BtnGuardar.setFont(new java.awt.Font("STXihei", 0, 14)); // NOI18N
        BtnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        BtnGuardar.setText("Guardar");
        BtnGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnGuardarMouseClicked(evt);
            }
        });

        BtnGuardarComo.setBackground(new java.awt.Color(102, 102, 102));
        BtnGuardarComo.setFont(new java.awt.Font("STXihei", 0, 14)); // NOI18N
        BtnGuardarComo.setForeground(new java.awt.Color(255, 255, 255));
        BtnGuardarComo.setText("Guardar Como");
        BtnGuardarComo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnGuardarComoMouseClicked(evt);
            }
        });

        BtnGenera.setBackground(new java.awt.Color(102, 102, 102));
        BtnGenera.setFont(new java.awt.Font("STXihei", 0, 14)); // NOI18N
        BtnGenera.setForeground(new java.awt.Color(255, 255, 255));
        BtnGenera.setText("Análisis Léxico");
        BtnGenera.setEnabled(false);
        BtnGenera.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnGeneraMouseClicked(evt);
            }
        });
        BtnGenera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGeneraActionPerformed(evt);
            }
        });

        BtnSintactico.setBackground(new java.awt.Color(102, 102, 102));
        BtnSintactico.setFont(new java.awt.Font("STXihei", 0, 14)); // NOI18N
        BtnSintactico.setForeground(new java.awt.Color(255, 255, 255));
        BtnSintactico.setText("Análisis Sintáctico");
        BtnSintactico.setEnabled(false);
        BtnSintactico.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnSintacticoMouseClicked(evt);
            }
        });

        BtnSemantico.setBackground(new java.awt.Color(102, 102, 102));
        BtnSemantico.setFont(new java.awt.Font("STXihei", 0, 14)); // NOI18N
        BtnSemantico.setForeground(new java.awt.Color(255, 255, 255));
        BtnSemantico.setText("Análisis Semántico");
        BtnSemantico.setEnabled(false);
        BtnSemantico.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnSemanticoMouseClicked(evt);
            }
        });

        BtnIntermedio.setBackground(new java.awt.Color(102, 102, 102));
        BtnIntermedio.setFont(new java.awt.Font("STXihei", 0, 14)); // NOI18N
        BtnIntermedio.setForeground(new java.awt.Color(255, 255, 255));
        BtnIntermedio.setText("Generar Código Intermedio");
        BtnIntermedio.setEnabled(false);
        BtnIntermedio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnIntermedioMouseClicked(evt);
            }
        });

        TbAnalisis.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(TbAnalisis);

        TbErroresSintacticos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Linea", "Columna", "Error", "Esperado", "Encontrado"
            }
        ));
        jScrollPane3.setViewportView(TbErroresSintacticos);

        TbErroresSemanticos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Linea", "Columna", "Error", "Descripcion"
            }
        ));
        jScrollPane4.setViewportView(TbErroresSemanticos);

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("STXihei", 0, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Texto de Análisis");

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("STXihei", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Errores de Análisis Sintáctico");

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("STXihei", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Errores de Análisis Semántico");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication4/imagenes/azteca2.png"))); // NOI18N
        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
                            .addComponent(jScrollPane3)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(BtnGenera, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(BtnSintactico, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(BtnSemantico, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(BtnIntermedio, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel4)
                            .addComponent(jScrollPane3)
                            .addComponent(jLabel5)
                            .addComponent(jScrollPane4)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(147, 147, 147)
                        .addComponent(jLabel2)))
                .addGap(50, 50, 50)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(134, 134, 134)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(BtnGuardarComo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(BtnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(BtnCargar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(BtnGenera)
                            .addComponent(BtnSintactico)
                            .addComponent(BtnSemantico)
                            .addComponent(BtnIntermedio))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(BtnCargar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(BtnGuardar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(BtnGuardarComo)))))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnGeneraActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_BtnGeneraActionPerformed
    {//GEN-HEADEREND:event_BtnGeneraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnGeneraActionPerformed

    private void BtnCargarMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_BtnCargarMouseClicked
    {//GEN-HEADEREND:event_BtnCargarMouseClicked
        BtnGenera.setEnabled(true);
        BtnSintactico.setEnabled(false);
        BtnSemantico.setEnabled(false);
        mt.setRowCount(0);
        syntaxErrorModel.setRowCount(0);
        semanticErrorModel.setRowCount(0);
        TxAnalisis.setText(Archivos.cargarArchivo());
    }//GEN-LAST:event_BtnCargarMouseClicked

    private void BtnCargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCargarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCargarActionPerformed

    private void BtnGeneraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnGeneraMouseClicked
        // Clear previous results
        mt.setRowCount(0);
        BtnSintactico.setEnabled(true);
        
        // Create scanner and analyze the text
        Analisis_Lexico analyzer = new Analisis_Lexico();
        java.util.List<Token> tokens = analyzer.scan(TxAnalisis.getText());
        
        // Process tokens and populate table
        StringBuilder texto = new StringBuilder();
        java.util.List<Integer> vecSalList = new java.util.ArrayList<>();
        
        for (Token token : tokens) {
            // Skip EOF token
            if (token.getType() == Token.TokenType.EOF) continue;
            
            // Add to table
            if (token.isError()) {
                mt.addRow(new Object[]{
                    token.getLexeme(), 
                    "ERROR: " + token.getErrorMessage(), 
                    token.getCode()
                });
            } else {
                mt.addRow(new Object[]{
                    token.getLexeme(), 
                    token.getTypeName(), 
                    token.getCode()
                });
            }
            
            // Build text output
            texto.append(token.getLexema())
                 .append("\t")
                 .append(token.isError() ? "ERROR" : token.getTypeName())
                 .append("\t")
                 .append(token.getCode())
                 .append("\n");
            
            // Collect token codes
            vecSalList.add(token.getCode());
        }
        
        // Convert to array for backward compatibility
        vecSal = new int[vecSalList.size()];
        for (int i = 0; i < vecSalList.size(); i++) {
            vecSal[i] = vecSalList.get(i);
        }
    }//GEN-LAST:event_BtnGeneraMouseClicked

    private void BtnSintacticoMouseClicked(java.awt.event.MouseEvent evt) {
        // Clear previous syntax error results and disable semantic button
        syntaxErrorModel.setRowCount(0);
        BtnSemantico.setEnabled(false);
        
        // First run lexical analysis to get tokens
        Analisis_Lexico analyzer = new Analisis_Lexico();
        java.util.List<Token> tokens = analyzer.scan(TxAnalisis.getText());
        
        // Check if there are lexical errors
        boolean hasLexicalErrors = false;
        for (Token token : tokens) {
            if (token.isError()) {
                hasLexicalErrors = true;
                break;
            }
        }
        
        if (hasLexicalErrors) {
            syntaxErrorModel.addRow(new Object[]{
                "-", "-", 
                "Corrija los errores léxicos antes del análisis sintáctico", 
                "Tokens válidos", 
                "Errores léxicos encontrados"
            });
            return;
        }
        
        // Run syntactic analysis
        Analisis_Sintactico sintactico = new Analisis_Sintactico();
        boolean success = sintactico.analizar(tokens);
        
        if (success) {
            // Show checkmark and clear any previous errors
            syntaxErrorModel.setRowCount(0);
            syntaxErrorModel.addRow(new Object[]{
                "✓", "✓", 
                "¡Analisis sintactico sin errores!", 
                "-", 
                "-"
            });
            // Enable semantic analysis button
            BtnSemantico.setEnabled(true);
        } else {
            // Display all errors
            java.util.List<Analisis_Sintactico.SyntaxError> errors = sintactico.getErrors();
            for (Analisis_Sintactico.SyntaxError error : errors) {
                syntaxErrorModel.addRow(new Object[]{
                    error.getLine(),
                    error.getColumn(),
                    error.getMessage(),
                    error.getExpected(),
                    error.getFound()
                });
            }
        }
    }

    private void BtnSemanticoMouseClicked(java.awt.event.MouseEvent evt) {
        semanticErrorModel.setRowCount(0);
        
        Analisis_Lexico analyzer = new Analisis_Lexico();
        java.util.List<Token> tokens = analyzer.scan(TxAnalisis.getText());
        
        boolean hasLexicalErrors = false;
        for (Token token : tokens) {
            if (token.isError()) {
                hasLexicalErrors = true;
                break;
            }
        }
        
        if (hasLexicalErrors) {
            semanticErrorModel.addRow(new Object[]{
                "-", "-", 
                "Corrija los errores léxicos antes del análisis semántico", 
                "Tokens válidos requeridos"
            });
            return;
        }
        
        Analisis_Sintactico sintactico = new Analisis_Sintactico();
        boolean syntaxSuccess = sintactico.analizar(tokens);
        
        if (!syntaxSuccess) {
            semanticErrorModel.addRow(new Object[]{
                "-", "-", 
                "Corrija los errores sintácticos antes del análisis semántico", 
                "AST requerido para análisis semántico"
            });
            return;
        }
        
        ASTNode ast = sintactico.getAST();
        if (ast == null) {
            semanticErrorModel.addRow(new Object[]{
                "-", "-", 
                "No se pudo construir el AST", 
                "Verifique el código fuente"
            });
            return;
        }
        
        Analisis_Semantico semantico = new Analisis_Semantico();
        boolean semanticSuccess = semantico.analizar(ast);
        
        if (semanticSuccess) {
            semanticErrorModel.addRow(new Object[]{
                "✓", "✓", 
                "Análisis semántico exitoso: todas las variables asignadas fueron declaradas", 
                "-"
            });
            BtnIntermedio.setEnabled(true);
        } else {
            java.util.List<Analisis_Semantico.SemanticError> errors = semantico.getErrors();
            for (Analisis_Semantico.SemanticError error : errors) {
                semanticErrorModel.addRow(new Object[]{
                    error.getLine(),
                    error.getColumn(),
                    error.getMessage(),
                    "Identificador: '" + error.getIdentifier() + "'"
                });
            }
        }
    }

    private void BtnIntermedioMouseClicked(java.awt.event.MouseEvent evt) {
        Analisis_Lexico analyzer = new Analisis_Lexico();
        java.util.List<Token> tokens = analyzer.scan(TxAnalisis.getText());
        
        boolean hasLexicalErrors = false;
        for (Token token : tokens) {
            if (token.isError()) {
                hasLexicalErrors = true;
                break;
            }
        }
        
        if (hasLexicalErrors) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Error: Corrija errores léxicos primero", 
                "Error", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Analisis_Sintactico sintactico = new Analisis_Sintactico();
        boolean syntaxSuccess = sintactico.analizar(tokens);
        
        if (!syntaxSuccess) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Error: Corrija errores sintácticos primero", 
                "Error", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Analisis_Semantico semantico = new Analisis_Semantico();
        boolean semanticSuccess = semantico.analizar(sintactico.getAST());
        
        if (!semanticSuccess) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Error: Corrija errores semánticos primero", 
                "Error", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        IntermediateCodeGenerator generator = sintactico.generateIntermediateCode();
        
        if (generator == null) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Error: No se pudo generar el código intermedio", 
                "Error", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        IntermediateWindow window = new IntermediateWindow(this, true, generator);
        window.setVisible(true);
    }

    private void BtnGuardarMouseClicked(java.awt.event.MouseEvent evt) {
        Archivos.guardarArchivoActual(TxAnalisis.getText());
    }

    private void BtnGuardarComoMouseClicked(java.awt.event.MouseEvent evt) {
        Archivos.guardarArchivoComo(TxAnalisis.getText());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(PrbLexico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(PrbLexico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(PrbLexico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(PrbLexico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new PrbLexico().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnCargar;
    private javax.swing.JButton BtnGuardar;
    private javax.swing.JButton BtnGuardarComo;
    private javax.swing.JButton BtnGenera;
    private javax.swing.JButton BtnSintactico;
    private javax.swing.JButton BtnSemantico;
    private javax.swing.JButton BtnIntermedio;
    private javax.swing.JTable TbAnalisis;
    private javax.swing.JTable TbErroresSintacticos;
    private javax.swing.JTable TbErroresSemanticos;
    private javax.swing.JTextArea TxAnalisis;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    // End of variables declaration//GEN-END:variables
}
