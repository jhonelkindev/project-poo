package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

/**
 * Clase que dibuja el panel con la imagen y su texto pero no decide
 * temas de la logica y tampoco controla el hilo que anima las letras
 *
 * @author Mariana Ocampo
 * @version 1.0
 */
public class StoryPanel extends JPanel {
    /**
     * imagen de fondo
     */
    private Image imagenFondo;
    /**
     * Texto que va en la barrita de informacion
     */
    private JTextArea textArea;
    /**
     * Panel donde aparece la pista de saltar
     */
    private JPanel bottomBar;
    /**
     * El label para la pista
     */
    private JLabel hintLabel;

    /**
     * Crea el panel y le mete la imagen y la barra de informacion
     */
    public StoryPanel() {
        setPreferredSize(new Dimension(1280, 720));
        setLayout(new BorderLayout());
        setFocusable(true); // nuevo set, sirve para que el panel reciba teclas, por ejemplo de adelantar
        textArea = new JTextArea(); // crear el objeto textarea
        hintLabel = new JLabel("Enter o Espacio para continuar");
        bottomBar = new JPanel(new BorderLayout());
        hintLabel.setForeground(new Color(186, 188, 34));
        bottomBar.setBackground(new Color(27, 24, 24));
        textArea.setEditable(false); //no se pueden editar las palabras escritas en el panel
        textArea.setFocusable(false);
        textArea.setLineWrap(true); //si el texto es largo, cambia de linea en lugar de salirse del cuadrito
        textArea.setWrapStyleWord(true); // en vez de escribir reac-tor cambia la linea
        textArea.setFont(new Font("Monospaced", Font.BOLD, 22));
        textArea.setForeground(new Color(180, 255, 120));
        textArea.setBackground(new Color(15, 18, 22));
        textArea.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // crea el borde respecto a la cajita
        textArea.setPreferredSize(new Dimension(1280, 170));
        hintLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
        hintLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        bottomBar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        bottomBar.add(textArea, BorderLayout.CENTER);
        bottomBar.add(hintLabel, BorderLayout.SOUTH);
        add(bottomBar, BorderLayout.SOUTH);


    }

    /**
     * Pone la imagen de fondo, si no encuentra el path o esta dañado, captura la excepcion
     * y la muestra al usuario
     *
     * @param imagePath ruta de la imagen
     */
    public void setBackgroundImage(String imagePath) {
        try {
            URL url = getClass().getResource(imagePath);
            if (url == null) {
                throw new FileNotFoundException("No se encontró la imagen: " + imagePath);
            }
            imagenFondo = ImageIO.read(url);
        } catch (FileNotFoundException e) {
            imagenFondo = null;
            System.err.println(e.getMessage());
        } catch (IOException e) {
            imagenFondo = null;
            System.err.println("La imagen esta dañada: " + imagePath);
        } finally {
            repaint();
        }

    }

    /**
     * Mete dentro de la barra de informacion el texto que le llega
     * como parametro
     *
     * @param text texto que va en la barra de informacion
     */
    public void showText(String text) {
        textArea.setText(text);
    }

    /**
     * Dibujar el panel
     *
     * @param g pincel para pintar la imagen de fondo sobre el panel
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor((new Color(25, 19, 19)));
        g.fillRect(0, 0, getWidth(), getHeight());
        if (imagenFondo != null) {
            int espacio = getHeight() - bottomBar.getHeight();
            int sobrante = espacio - imagenFondo.getHeight(this);
            int y = sobrante / 2;
            g.drawImage(imagenFondo, 0, y, this);

        }

    }
}
