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
    private JButton escalaDeGrisesButton;
    private JButton negativeButton;
    private BufferedImage imagenOriginal;
    private JButton originalButton;

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

        escalaDeGrisesButton = new JButton("Escala de Grises");
        escalaDeGrisesButton.addActionListener(e -> setEscalaDeGrises());

        negativeButton = new JButton("Negativo");
        negativeButton.addActionListener(e -> setNegativo());

        originalButton = new JButton("Color original");
        originalButton.addActionListener( e -> {
            imagenActual = imagenOriginal;
            actualizarPantalla();
        });

        panelHerramientas.add(originalButton);
        panelHerramientas.add(escalaDeGrisesButton);
        panelHerramientas.add(negativeButton);
        add(panelHerramientas, BorderLayout.EAST);

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
                imagenOriginal = imagenActual;
                actualizarPantalla();
                pack();
                setLocationRelativeTo(null);
            } catch (IOException e) {
                System.err.println("Error al cargar la imagen");
            }
        }
    }

    private void setNegativo() {
        if (imagenActual == null) return;

        int ancho = imagenActual.getWidth();
        int alto = imagenActual.getHeight();
        BufferedImage nueva = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < ancho; x++) {
            for (int y = 0; y < alto; y++) {
                Color pixel = new Color(imagenActual.getRGB(x, y));

                int r = 255 - pixel.getRed();
                int g = 255 - pixel.getGreen();
                int b = 255 - pixel.getBlue();

                Color nuevoColor = new Color(r, g, b);
                nueva.setRGB(x, y, nuevoColor.getRGB());
            }
        }

        imagenActual = nueva;
        actualizarPantalla();
    }

    private void setEscalaDeGrises() {
        if (imagenActual == null) return;

        int ancho = imagenActual.getWidth();
        int alto = imagenActual.getHeight();
        BufferedImage nueva = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < ancho; x++) {
            for (int y = 0; y < alto; y++) {
                Color pixel = new Color(imagenActual.getRGB(x, y));

                int r = pixel.getRed();
                int g = pixel.getGreen();
                int b = pixel.getBlue();

                int gris = (int) (0.299 * r + 0.587 * g + 0.114 * b);

                Color nuevoColor = new Color(gris, gris, gris);
                nueva.setRGB(x, y, nuevoColor.getRGB());
            }
        }

        imagenActual = nueva;
        actualizarPantalla();
    }

    private void actualizarPantalla() {
        if (imagenActual != null) {
            etiquetaImagen.setIcon(new ImageIcon(imagenActual));
            etiquetaImagen.setPreferredSize(new Dimension(imagenActual.getWidth(), imagenActual.getHeight()));
            etiquetaImagen.revalidate();
            etiquetaImagen.repaint();
        }
    }

    public static void main(String[] args) {
        new Visualizador();
    }
}