package org.davidmetam.pci;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
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
    private JButton umbralButton;
    private JButton blurButton;
    private JButton sharpenButton;
    private JButton erosionButton;
    private JButton harrisButton;
    private final Color colorDefecto = UIManager.getColor("Button.background");

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
        panelHerramientas.setBorder(new EmptyBorder(5, 5, 5, 5));
        panelHerramientas.setBackground(Color.LIGHT_GRAY);

        panelHerramientas.setLayout(new GridLayout(0, 1, 10, 10));

        escalaDeGrisesButton = new JButton("Escala de Grises");
        escalaDeGrisesButton.addActionListener(e -> {
            setBackground(escalaDeGrisesButton);
            setEscalaDeGrises();
        });

        negativeButton = new JButton("Negativo");
        negativeButton.addActionListener(e -> {
            setBackground(negativeButton);
            setNegativo();
        });

        originalButton = new JButton("Color original");
        originalButton.addActionListener(e -> {
            setBackground(originalButton);
            imagenActual = imagenOriginal;
            actualizarPantalla();
        });

        umbralButton = new JButton("Umbral");
        umbralButton.addActionListener(e -> {
            setBackground(umbralButton);
            imagenActual = imagenOriginal;
            setUmbral();
        });

        blurButton = new JButton("Blur");
        blurButton.addActionListener(e -> {
            setBackground(blurButton);
            imagenActual = imagenOriginal;
            setBlur();
        });

        sharpenButton = new JButton("Sharpen");
        sharpenButton.addActionListener(e -> {
            setBackground(sharpenButton);
            imagenActual = imagenOriginal;
            setSharpen();
        });

        erosionButton = new JButton("Erosión");
        erosionButton.addActionListener(e -> {
            setBackground(erosionButton);
            setErosion();
        });

        harrisButton = new JButton("Harris");
        harrisButton.addActionListener(e -> {
            setBackground(harrisButton);
            setHarris();
        });

        panelHerramientas.add(originalButton);
        panelHerramientas.add(escalaDeGrisesButton);
        panelHerramientas.add(negativeButton);
        panelHerramientas.add(umbralButton);
        panelHerramientas.add(blurButton);
        panelHerramientas.add(sharpenButton);
        panelHerramientas.add(erosionButton);
        panelHerramientas.add(harrisButton);
        add(panelHerramientas, BorderLayout.EAST);

        JPanel panelInferior = new JPanel();
        JButton botonCargar = new JButton("Cargar Imagen");
        botonCargar.addActionListener(e -> cargarImagenDesdeArchivo());
        panelInferior.add(botonCargar);
        add(panelInferior, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setBackground(JButton button) {
        escalaDeGrisesButton.setBackground(colorDefecto);
        umbralButton.setBackground(colorDefecto);
        negativeButton.setBackground(colorDefecto);
        originalButton.setBackground(colorDefecto);
        blurButton.setBackground(colorDefecto);
        sharpenButton.setBackground(colorDefecto);
        erosionButton.setBackground(colorDefecto);
        harrisButton.setBackground(colorDefecto);
        button.setBackground(new Color(50, 252, 82));
    }

    private void cargarImagenDesdeArchivo() {
        JFileChooser selector = new JFileChooser("C:\\Users\\david\\OneDrive\\Imágenes");
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
        imagenActual = imagenOriginal;

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

    private void setBlur() {
        if (imagenActual == null) return;
        int ancho = imagenActual.getWidth();
        int alto = imagenActual.getHeight();
        BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int x = 1; x < ancho - 1; x++) {
            for (int y = 1; y < alto - 1; y++) {
                int sumaR = 0, sumaG = 0, sumaB = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        Color vecino = new Color(imagenActual.getRGB(x + i, y + j));

                        sumaR += vecino.getRed();
                        sumaG += vecino.getGreen();
                        sumaB += vecino.getBlue();
                    }
                }
                int r = sumaR / 9;
                int g = sumaG / 9;
                int b = sumaB / 9;
                Color nuevoColor = new Color(r, g, b);
                resultado.setRGB(x, y, nuevoColor.getRGB());
            }
        }
        imagenActual = resultado;
        actualizarPantalla();
    }

    private void setSharpen() {
        if (imagenActual == null) return;
        int ancho = imagenActual.getWidth();
        int alto = imagenActual.getHeight();
        BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int x = 1; x < ancho - 1; x++) {
            for (int y = 1; y < alto - 1; y++) {
                int sumaR = 0, sumaG = 0, sumaB = 0;

                Color c01 = new Color(imagenActual.getRGB(x, y - 1));
                Color c10 = new Color(imagenActual.getRGB(x - 1, y));
                Color c11 = new Color(imagenActual.getRGB(x, y));
                Color c12 = new Color(imagenActual.getRGB(x + 1, y));
                Color c21 = new Color(imagenActual.getRGB(x, y + 1));

                sumaR = (c11.getRed() * 5) - c01.getRed() - c10.getRed() - c12.getRed() - c21.getRed();
                sumaG = (c11.getGreen() * 5) - c01.getGreen() - c10.getGreen() - c12.getGreen() - c21.getGreen();
                sumaB = (c11.getBlue() * 5) - c01.getBlue() - c10.getBlue() - c12.getBlue() - c21.getBlue();

                int r = Math.min(255, Math.max(0, sumaR));
                int g = Math.min(255, Math.max(0, sumaG));
                int b = Math.min(255, Math.max(0, sumaB));

                Color nuevoColor = new Color(r, g, b);
                resultado.setRGB(x, y, nuevoColor.getRGB());
            }
        }
        imagenActual = resultado;
        actualizarPantalla();
    }

    private void setEscalaDeGrises() {
        if (imagenActual == null) return;
        imagenActual = imagenOriginal;

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

    private void setUmbral() {
        if (imagenActual == null) return;
        imagenActual = imagenOriginal;

        int ancho = imagenActual.getWidth();
        int alto = imagenActual.getHeight();
        BufferedImage nueva = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < ancho; x++) {
            for (int y = 0; y < alto; y++) {
                Color pixel = new Color(imagenActual.getRGB(x, y));

                int r = pixel.getRed();
                int g = pixel.getGreen();
                int b = pixel.getBlue();

                int gris = (r + g + b) < 128 ? 0 : 255;

                Color nuevoColor = new Color(gris, gris, gris);
                nueva.setRGB(x, y, nuevoColor.getRGB());
            }
        }

        imagenActual = nueva;
        actualizarPantalla();
    }

    private void setErosion() {
        if (imagenActual == null) return;
        int ancho = imagenActual.getWidth();
        int alto = imagenActual.getHeight();
        BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int x = 1; x < ancho - 1; x++) {
            for (int y = 1; y < alto - 1; y++) {
                int minGris = 255;
                Color colorMinimo = Color.WHITE;

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        Color vecino = new Color(imagenActual.getRGB(x + i, y + j));
                        int gris = (vecino.getRed() + vecino.getGreen() + vecino.getBlue()) / 3;

                        if (gris < minGris) {
                            minGris = gris;
                            colorMinimo = vecino;
                        }
                    }
                }
                resultado.setRGB(x, y, colorMinimo.getRGB());
            }
        }
        imagenActual = resultado;
        actualizarPantalla();
    }

    private void setHarris() {
        if (imagenOriginal == null) return;
        int ancho = imagenOriginal.getWidth();
        int alto = imagenOriginal.getHeight();

        int[][] gris = new int[ancho][alto];
        for (int x = 0; x < ancho; x++) {
            for (int y = 0; y < alto; y++) {
                Color p = new Color(imagenOriginal.getRGB(x, y));
                gris[x][y] = (int) (0.299 * p.getRed() + 0.587 * p.getGreen() + 0.114 * p.getBlue());
            }
        }

        double[][] ix = new double[ancho][alto];
        double[][] iy = new double[ancho][alto];

        for (int x = 1; x < ancho - 1; x++) {
            for (int y = 1; y < alto - 1; y++) {
                double hx = (gris[x + 1][y - 1] + 2 * gris[x + 1][y] + gris[x + 1][y + 1]) -
                        (gris[x - 1][y - 1] + 2 * gris[x - 1][y] + gris[x - 1][y + 1]);
                double hy = (gris[x - 1][y + 1] + 2 * gris[x][y + 1] + gris[x + 1][y + 1]) -
                        (gris[x - 1][y - 1] + 2 * gris[x][y - 1] + gris[x + 1][y - 1]);
                ix[x][y] = hx;
                iy[x][y] = hy;
            }
        }

        double[][] ix2 = new double[ancho][alto];
        double[][] iy2 = new double[ancho][alto];
        double[][] ixy = new double[ancho][alto];

        for (int x = 0; x < ancho; x++) {
            for (int y = 0; y < alto; y++) {
                ix2[x][y] = ix[x][y] * ix[x][y];
                iy2[x][y] = iy[x][y] * iy[x][y];
                ixy[x][y] = ix[x][y] * iy[x][y];
            }
        }

        double[][] sX2 = new double[ancho][alto];
        double[][] sY2 = new double[ancho][alto];
        double[][] sXY = new double[ancho][alto];

        for (int x = 2; x < ancho - 2; x++) {
            for (int y = 2; y < alto - 2; y++) {
                double sumX2 = 0, sumY2 = 0, sumXY = 0;
                for (int i = -2; i <= 2; i++) {
                    for (int j = -2; j <= 2; j++) {
                        sumX2 += ix2[x + i][y + j];
                        sumY2 += iy2[x + i][y + j];
                        sumXY += ixy[x + i][y + j];
                    }
                }
                sX2[x][y] = sumX2;
                sY2[x][y] = sumY2;
                sXY[x][y] = sumXY;
            }
        }

        double[][] harrisR = new double[ancho][alto];
        double maxR = 0;
        double k = 0.04;

        for (int x = 2; x < ancho - 2; x++) {
            for (int y = 2; y < alto - 2; y++) {
                double a = sX2[x][y];
                double b = sXY[x][y];
                double c = sY2[x][y];

                double det = (a * c) - (b * b);
                double trace = a + c;
                double r = det - k * (trace * trace);

                if (r > 0) {
                    harrisR[x][y] = r;
                    if (r > maxR) {
                        maxR = r;
                    }
                }
            }
        }

        BufferedImage copia = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = copia.createGraphics();
        g2d.drawImage(imagenOriginal, 0, 0, null);
        g2d.setColor(Color.GREEN);

        double umbral = maxR * 0.1;

        for (int x = 2; x < ancho - 2; x++) {
            for (int y = 2; y < alto - 2; y++) {
                if (harrisR[x][y] > umbral) {
                    boolean esMaxLocal = true;
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            if (harrisR[x][y] < harrisR[x + i][y + j]) {
                                esMaxLocal = false;
                                break;
                            }
                        }
                        if (!esMaxLocal) break;
                    }
                    if (esMaxLocal) {
                        g2d.drawOval(x - 3, y - 3, 6, 6);
                    }
                }
            }
        }
        g2d.dispose();

        imagenActual = copia;
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