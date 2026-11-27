package view;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class WelcomePanel {

    private JPanel panel1;
    private JPanel panelBotones;
    private JPanel headerPanel;
    private JPanel buttonColumn;

    private JButton iniciarButton;
    private JButton rankingButton;
    private JButton instruccionesButton;
    private JButton salirButton;

    private JLabel JLnombreTitulo;
    private JLabel logo;
    private JLabel subjectLabel;
    private JLabel membersLabel;

    public WelcomePanel() {

        // Los paneles auxiliares no deben tapar las imagenes de fondo.
        headerPanel.setOpaque(false);
        buttonColumn.setOpaque(false);
        panelBotones.setOpaque(false);

        // Textos del menu
        configureLabels();

        // Logo de la universidad
        configureLogo();

        // Imagenes de los botones
        configureButton(iniciarButton, "btn_iniciar_hover.png");
        configureButton(rankingButton, "btn_ranking_hover.png");
        configureButton(instruccionesButton, "btn_instrucciones_hover.png");
        configureButton(salirButton, "btn_salir_hover.png");

        // Eventos de los botones
        iniciarButton.addActionListener(e -> {
            System.out.println("Iniciar juego");
        });

        rankingButton.addActionListener(e -> {
            System.out.println("Abrir ranking");
        });

        instruccionesButton.addActionListener(e -> {
            System.out.println("Abrir instrucciones");
        });

        salirButton.addActionListener(e -> {
            System.exit(0);
        });
    }

    // Crea los paneles con sus imagenes
    private void createUIComponents() {

        panel1 = new PanelImage(
                "/menus/Menu_Principal.png"
        );

        panelBotones = new PanelImage(
                "/menus/botones_menu/panel_botones.png"
        );
    }

    // Configura los textos de la ventana

    private void configureLabels() {

        // Titulo principal
        JLnombreTitulo.setText("PROJECT REACTOR");

        JLnombreTitulo.setFont(
                new Font("Monospaced", Font.BOLD, 40)
        );

        JLnombreTitulo.setForeground(
                new Color(182, 242, 91)
        );

        JLnombreTitulo.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        // Materia
        subjectLabel.setText(
                "PROGRAMACION ORIENTADA A OBJETOS"
        );

        subjectLabel.setFont(
                new Font("Monospaced", Font.BOLD, 16)
        );

        subjectLabel.setForeground(Color.WHITE);

        // Integrantes
        membersLabel.setText(
                "Mariana Ocampo  |  Jhon Elkin Candamil  |  Dylan Osorio"
        );

        membersLabel.setFont(
                new Font("Monospaced", Font.BOLD, 20)
        );

        membersLabel.setForeground(Color.WHITE);

        membersLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        membersLabel.setOpaque(false);
    }


    // Carga el logo de la universidad

    private void configureLogo() {

        String path = "/menus/logoUAM/logoUAM.png";

        URL imageUrl = getClass().getResource(path);

        if (imageUrl != null) {

            ImageIcon icon = new ImageIcon(imageUrl);

            int maxWidth = 210;
            int maxHeight = 120;

            double scale = Math.min(
                    (double) maxWidth / icon.getIconWidth(),
                    (double) maxHeight / icon.getIconHeight()
            );

            int width = (int) (icon.getIconWidth() * scale);
            int height = (int) (icon.getIconHeight() * scale);

            Image image = icon.getImage().getScaledInstance(
                    width,
                    height,
                    Image.SCALE_SMOOTH
            );

            logo.setIcon(new ImageIcon(image));
            logo.setText("");

            logo.setPreferredSize(new Dimension(210, 120));
            logo.setHorizontalAlignment(SwingConstants.CENTER);
            logo.setVerticalAlignment(SwingConstants.CENTER);
            logo.setOpaque(false);

        } else {
            System.out.println("No se encontro el logo: " + path);
        }
    }


    // Configura la imagen de cada boton
    private void configureButton(JButton button, String fileName) {

        String path = "/menus/botones_menu/" + fileName;

        URL imageUrl = getClass().getResource(path);

        if (imageUrl != null) {

            ImageIcon icon = new ImageIcon(imageUrl);

            button.setIcon(icon);

            Dimension size = new Dimension(
                    icon.getIconWidth(),
                    icon.getIconHeight()
            );

            button.setPreferredSize(size);
            button.setMinimumSize(size);

        } else {
            System.out.println("No se encontro: " + path);
        }

        button.setText("");
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);

        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
    }

    public JPanel getPanel1() {
        return panel1;
    }

    public JButton getStartButton() {
        return iniciarButton;
    }

    public JButton getRankingButton() {
        return rankingButton;
    }

    public JButton getInstructionsButton() {
        return instruccionesButton;
    }

    public JButton getExitButton() {
        return salirButton;
    }

    // Main temporal para probar el menu
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame window = new JFrame("PROJECT REACTOR");

            WelcomePanel welcome = new WelcomePanel();

            welcome.getPanel1().setPreferredSize(
                    new Dimension(1280, 720)
            );

            window.setContentPane(welcome.getPanel1());

            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            window.pack();
            window.setLocationRelativeTo(null);
            window.setResizable(false);
            window.setVisible(true);
        });
    }
}
