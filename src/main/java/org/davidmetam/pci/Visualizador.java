package org.davidmetam.pci;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.border.TitledBorder;
import java.awt.image.BufferedImage;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

@SuppressWarnings("all")
public class Visualizador extends JFrame {
    private JLabel etiquetaImagen;
    private BufferedImage imagenActual;
    private JPanel panelHerramientas;

    public Visualizador() {
        setTitle("Visualizador de Imagenes PCI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setMinimumSize(new Dimension(400, 300));
        setLayout(new BorderLayout());

        etiquetaImagen = new JLabel();
        etiquetaImagen.setHorizontalAlignment(JLabel.CENTER);
        JScrollPane scrollImagen = new JScrollPane(etiquetaImagen);
        add(scrollImagen, BorderLayout.CENTER);

        panelHerramientas = new JPanel();
        panelHerramientas.setPreferredSize(new Dimension(200, 0));
        panelHerramientas.setBorder(new TitledBorder("Procesamiento"));
        panelHerramientas.setBackground(Color.LIGHT_GRAY);
        //add(panelHerramientas, BorderLayout.EAST);

        JPanel panelInferior = new JPanel();
        JButton botonCargar = new JButton("Cargar Imagen");
        botonCargar.addActionListener(e -> cargarImagenDesdeArchivo());
        panelInferior.add(botonCargar);
        add(panelInferior, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void cargarImagenDesdeArchivo() {
        JFileChooser selector = new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Imagenes", "jpg", "png", "jpeg", "bmp");
        selector.setFileFilter(filtro);
        int resultado = selector.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = selector.getSelectedFile();
            try {
                imagenActual = ImageIO.read(archivo);
                etiquetaImagen.setIcon(new ImageIcon(imagenActual));
                etiquetaImagen.setPreferredSize(new Dimension(imagenActual.getWidth(), imagenActual.getHeight()));
                etiquetaImagen.revalidate();
                pack();
                setLocationRelativeTo(null);
            } catch (IOException e) {
                System.err.println("Error al cargar la imagen");
            }
        }
    }

    public static void main(String[] args) {
        new Visualizador();
    }}