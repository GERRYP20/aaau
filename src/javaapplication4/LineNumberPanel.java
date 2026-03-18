/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication4;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;

/**
 * A panel that displays line numbers for a JTextArea.
 * Designed to be used as a row header view in a JScrollPane.
 * Automatically synchronizes scrolling with the text area.
 * Highlights the current line.
 * @author Gerardo
 */
public class LineNumberPanel extends JPanel implements DocumentListener, CaretListener {

    private final JTextArea textArea;
    private int currentLine = 1;

    public LineNumberPanel(JTextArea textArea) {
        this.textArea = textArea;

        setBackground(new Color(240, 240, 240));
        setFont(textArea.getFont());

        // Add listeners
        textArea.getDocument().addDocumentListener(this);
        textArea.addCaretListener(this);

        // Add component listener to track text area resize events
        textArea.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updatePreferredSize();
                repaint();
            }
        });

        // Set preferred size based on initial content
        updatePreferredSize();
    }
    
    private void updatePreferredSize() {
        int lineCount = getLineCount();
        int digits = String.valueOf(lineCount).length();
        // Minimum 2 digits width
        digits = Math.max(digits, 2);
        
        // Use text area's font metrics for consistency
        FontMetrics fm = textArea.getFontMetrics(textArea.getFont());
        int charWidth = fm.charWidth('0');
        int width = charWidth * digits + 20; // 20px padding
        
        // Match height to text area's preferred size (not current height)
        // This ensures all lines are accounted for
        int height = textArea.getPreferredSize().height;
        
        setPreferredSize(new Dimension(width, height));
        revalidate();
        repaint();
    }
    
    private int getLineCount() {
        String text = textArea.getText();
        if (text.isEmpty()) return 1;
        
        int lines = 1;
        for (char c : text.toCharArray()) {
            if (c == '\n') lines++;
        }
        return lines;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        // Use text area's font metrics for exact consistency
        FontMetrics fm = textArea.getFontMetrics(textArea.getFont());
        int lineHeight = fm.getHeight();
        int ascent = fm.getAscent();

        // Get the visible rectangle of the text area (accounts for scrolling)
        Rectangle visibleRect = textArea.getVisibleRect();
        
        try {
            // Determine visible range using viewToModel on visible rectangle bounds
            int startOffset = textArea.viewToModel(new Point(0, visibleRect.y));
            int endOffset = textArea.viewToModel(new Point(0, visibleRect.y + visibleRect.height));
            
            // Convert offsets to line numbers
            int startLine = textArea.getLineOfOffset(startOffset) + 1;
            int endLine = textArea.getLineOfOffset(endOffset) + 1;
            int totalLines = getLineCount();
            
            // Ensure bounds are valid
            if (startLine < 1) startLine = 1;
            if (endLine > totalLines) endLine = totalLines;

            // Iterate over visible lines
            for (int line = startLine; line <= endLine; line++) {
                try {
                    // Get the document offset at the start of this line
                    int lineStartOffset = textArea.getLineStartOffset(line - 1);
                    
                    // Use modelToView2D to get the exact rectangle where this line is rendered
                    Rectangle2D viewRect = textArea.modelToView2D(lineStartOffset);
                    
                    // Get the Y position - since this panel is in the row header view,
                    // it scrolls with the text area, so we use the Y directly
                    int y = (int) viewRect.getY();
                    
                    // Highlight current line
                    if (line == currentLine) {
                        g.setColor(new Color(220, 220, 220));
                        g.fillRect(0, y, getWidth(), lineHeight);
                    }

                    // Draw line number at the exact Y position of the text line
                    g.setColor(Color.DARK_GRAY);
                    String lineNum = String.valueOf(line);
                    int x = getWidth() - fm.stringWidth(lineNum) - 8;
                    // Add ascent for baseline alignment
                    g.drawString(lineNum, x, y + ascent);
                    
                } catch (BadLocationException ex) {
                    // Skip this line if we can't determine its position
                    continue;
                }
            }
        } catch (BadLocationException ex) {
            // If we can't determine the visible range, fall back to drawing nothing
        }
    }
    
    // DocumentListener methods
    @Override
    public void insertUpdate(DocumentEvent e) {
        updatePreferredSize();
        repaint();
    }
    
    @Override
    public void removeUpdate(DocumentEvent e) {
        updatePreferredSize();
        repaint();
    }
    
    @Override
    public void changedUpdate(DocumentEvent e) {
        updatePreferredSize();
        repaint();
    }
    
    // CaretListener method
    @Override
    public void caretUpdate(CaretEvent e) {
        try {
            int caretPosition = textArea.getCaretPosition();
            currentLine = textArea.getLineOfOffset(caretPosition) + 1;
            repaint();
        } catch (BadLocationException ex) {
            // Ignore
        }
    }
}
