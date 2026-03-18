/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Gerardo
 */
public class Archivos
{
/**
 *
 * @author David de la Luz
 */

    private static File currentFile = null;
    
    public static void setCurrentFile(File file) {
        currentFile = file;
    }
    
    public static File getCurrentFile() {
        return currentFile;
    }
    
    public static void guardarArchivo(String texto) {
        FileWriter fichero = null;
        PrintWriter pw = null;

        try {
            // Ruta relativa para la carpeta Datos
            File folder = new File("Datos");
            if (!folder.exists()) {
                folder.mkdir(); // Crear la carpeta si no existe
            }

            fichero = new FileWriter(new File(folder, "archivo.txt"));
            pw = new PrintWriter(fichero);
            pw.println(texto);

        } catch (IOException ex) {
            Logger.getLogger(Archivos.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            if (fichero != null) {
                try {
                    fichero.close();
                } catch (IOException ex) {
                    Logger.getLogger(Archivos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public static void guardarArchivoActual(String texto) {
        if (currentFile != null) {
            FileWriter fichero = null;
            PrintWriter pw = null;
            
            try {
                fichero = new FileWriter(currentFile);
                pw = new PrintWriter(fichero);
                pw.print(texto);
            } catch (IOException ex) {
                Logger.getLogger(Archivos.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (fichero != null) {
                    try {
                        fichero.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Archivos.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } else {
            // If no current file, use save as
            guardarArchivoComo(texto);
        }
    }
    
    public static void guardarArchivoComo(String texto) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar archivo");
        
        // Set default directory to Datos folder
        File datosFolder = new File("Datos");
        if (datosFolder.exists()) {
            fileChooser.setCurrentDirectory(datosFolder);
        }
        
        // Add filter for .txt files
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de texto (*.txt)", "txt");
        fileChooser.setFileFilter(filter);
        
        // Show save dialog
        int result = fileChooser.showSaveDialog(null);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            // Add .txt extension if not present
            if (!file.getName().endsWith(".txt")) {
                file = new File(file.getAbsolutePath() + ".txt");
            }
            
            FileWriter fichero = null;
            PrintWriter pw = null;
            
            try {
                fichero = new FileWriter(file);
                pw = new PrintWriter(fichero);
                pw.print(texto);
                currentFile = file; // Update current file reference
                
            } catch (IOException ex) {
                Logger.getLogger(Archivos.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (fichero != null) {
                    try {
                        fichero.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Archivos.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public static String cargarArchivo() {
        String cadena = "";
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        // Create file chooser dialog
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo de texto");
        
        // Set default directory to Datos folder
        File datosFolder = new File("Datos");
        if (datosFolder.exists()) {
            fileChooser.setCurrentDirectory(datosFolder);
        }
        
        // Add filter for .txt files
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de texto (*.txt)", "txt");
        fileChooser.setFileFilter(filter);
        
        // Show dialog
        int result = fileChooser.showOpenDialog(null);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            archivo = fileChooser.getSelectedFile();
            currentFile = archivo; // Set current file for save functionality
            try {
                fr = new FileReader(archivo);
                br = new BufferedReader(fr);
                String linea;

                while ((linea = br.readLine()) != null) {
                    cadena = cadena + linea + "\n";
                }

            } catch (FileNotFoundException ex) {
                Logger.getLogger(Archivos.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Archivos.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (fr != null) {
                    try {
                        fr.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Archivos.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        return cadena;
    }
}

