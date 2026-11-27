package view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * Vista de instrucciones del juego PROJECT REACTOR.
 * Muestra las reglas de cada seccion utilizando un formulario Swing.
 */
public class InstructionsPanel {

    private JPanel mainPanel;
    private JPanel navigationPanel;
    private JPanel contentPanel;

    private JButton mechanicsButton;
    private JButton level1Button;
    private JButton level2Button;
    private JButton level3Button;
    private JButton survivalButton;
    private JButton rankingButton;
    private JButton backButton;

    private JLabel sectionTitleLabel;
    private JScrollPane contentScrollPane;
    private JTextArea contentTextArea;

    private static final String BUTTON_PATH = "/menus/instrucciones_botones/";

    /**
     * Configura los componentes y muestra las mecanicas al abrir la vista.
     */
    public InstructionsPanel() {
        configureComponents();
        configureButtons();
        showMechanics();
    }

    /**
     * Crea el panel de fondo desde el formulario de IntelliJ.
     */
    private void createUIComponents() {
        mainPanel = new PanelImage("/menus/instruciones.png");
    }

    /**
     * Configura el titulo y el cuadro donde aparecen las instrucciones.
     */
    private void configureComponents() {
        navigationPanel.setOpaque(false);
        contentPanel.setOpaque(false);

        sectionTitleLabel.setText("TRAINING: MECANICAS");
        sectionTitleLabel.setOpaque(false);
        sectionTitleLabel.setFont(new Font("Monospaced", Font.BOLD, 22));
        sectionTitleLabel.setForeground(new Color(182, 242, 91));
        sectionTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        contentTextArea.setEditable(false);
        contentTextArea.setFocusable(false);
        contentTextArea.setLineWrap(true);
        contentTextArea.setWrapStyleWord(true);
        contentTextArea.setOpaque(false);
        contentTextArea.setFont(new Font("Monospaced", Font.BOLD, 13));
        contentTextArea.setForeground(new Color(226, 239, 229));
        contentTextArea.setMargin(new Insets(12, 10, 8, 10));
        contentTextArea.setBorder(null);

        contentScrollPane.setOpaque(false);
        contentScrollPane.getViewport().setOpaque(false);
        contentScrollPane.setBorder(null);
        contentScrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
        contentScrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );
        contentScrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }

    /**
     * Carga los PNG y asigna las acciones a los botones.
     */
    private void configureButtons() {
        configureImageButton(mechanicsButton, "btn_mecanicas_selected.png", 196, 60);
        configureImageButton(level1Button, "btn_nivel1_selected.png", 196, 60);
        configureImageButton(level2Button, "btn_nivel2_selected.png", 196, 60);
        configureImageButton(level3Button, "btn_nivel3_selected.png", 196, 60);
        configureImageButton(survivalButton, "btn_supervivencia_selected.png", 196, 60);
        configureImageButton(rankingButton, "btn_ranking_selected.png", 196, 60);
        configureImageButton(backButton, "btn_volver_hover.png", 125, 42);

        mechanicsButton.addActionListener(e -> showMechanics());
        level1Button.addActionListener(e -> showLevel1());
        level2Button.addActionListener(e -> showLevel2());
        level3Button.addActionListener(e -> showLevel3());
        survivalButton.addActionListener(e -> showSurvival());
        rankingButton.addActionListener(e -> showRanking());
        // El boton Volver se conectara desde MainFrame.
    }

    /**
     * Coloca la imagen PNG en un JButton sin perder el estilo pixel art.
     *
     * @param button boton al que se le asigna la imagen
     * @param fileName nombre de la imagen dentro de resources
     * @param width ancho de la imagen en pantalla
     * @param height alto de la imagen en pantalla
     */
    private void configureImageButton(JButton button, String fileName,
                                      int width, int height) {
        String path = BUTTON_PATH + fileName;
        URL imageUrl = getClass().getResource(path);

        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        Dimension size = new Dimension(width, height);
        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setMaximumSize(size);

        if (imageUrl == null) {
            System.err.println("No se encontro: " + path);
            // Se conserva el texto del .form para detectar un PNG faltante.
            button.setForeground(Color.WHITE);
            return;
        }

        ImageIcon original = new ImageIcon(imageUrl);
        BufferedImage scaled = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D graphics = scaled.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        graphics.drawImage(original.getImage(), 0, 0, width, height, null);
        graphics.dispose();

        button.setIcon(new ImageIcon(scaled));
        button.setText("");
    }

    /**
     * Cambia el titulo y el texto del panel derecho.
     *
     * @param title titulo de la seccion seleccionada
     * @param instructions instrucciones de esa seccion
     */
    private void showSection(String title, String instructions) {
        sectionTitleLabel.setText(title);
        contentTextArea.setText(instructions);
        contentTextArea.setCaretPosition(0);
        contentScrollPane.getViewport().setViewPosition(new Point(0, 0));
    }

    /** Muestra los controles y las reglas generales. */
    private void showMechanics() {
        showSection("TRAINING: MECANICAS",
                "CONTROLES GENERALES\n\n" +
                        "F - Recoger objetos y piezas.\n" +
                        "E - Ejecutar acciones.\n" +
                        "Q - Soltar el objeto que llevas.\n\n" +
                        "TRANSPORTE\n" +
                        "Solo puedes llevar 1 pieza a la vez.\n" +
                        "Con una pieza, caminas mas lento.\n\n" +
                        "TIEMPO Y VIDAS\n" +
                        "Cada nivel tiene una cuenta regresiva.\n" +
                        "Sin tiempo o sin vidas: GAME OVER.");
    }

    /** Muestra las instrucciones de la zona de calderas. */
    private void showLevel1() {
        showSection("TRAINING: NIVEL 1",
                "ZONA 1 - CALDERAS\n\n" +
                        "ENEMIGO: VAPOR\n" +
                        "Si te toca, pierdes 1 vida.\n\n" +
                        "OBJETIVO\n" +
                        "Cada caldera necesita 2 tuercas.\n" +
                        "Cada tubo danado necesita 2 tubos.\n" +
                        "Los repuestos aparecen en el piso.\n\n" +
                        "Recoge con F y repara con E.\n" +
                        "Debes terminar antes de que el\n" +
                        "tiempo llegue a cero.");
    }

    /** Muestra las instrucciones de la zona electrica. */
    private void showLevel2() {
        showSection("TRAINING: NIVEL 2",
                "ZONA 2 - GENERADOR\n\n" +
                        "ENEMIGOS: RATAS\n" +
                        "Si te tocan, pierdes 1 vida.\n\n" +
                        "OBJETIVO\n" +
                        "Debes instalar 6 tuercas.\n" +
                        "Las tuercas tienen radiacion.\n\n" +
                        "Llevalas primero a la mesa de\n" +
                        "limpieza para descontaminarlas.\n" +
                        "Despues instalalas en el generador.\n\n" +
                        "No dejes que el tiempo se acabe.");
    }

    /** Muestra las instrucciones del reactor y el jefe final. */
    private void showLevel3() {
        showSection("TRAINING: NIVEL 3",
                "ZONA 3 - REACTOR\n\n" +
                        "JEFE FINAL: RATA MUTANTE\n" +
                        "Lanza proyectiles radiactivos.\n" +
                        "Cada impacto te quita 1 vida.\n\n" +
                        "OBJETIVO\n" +
                        "Recoge las 3 barras radiactivas\n" +
                        "y ponlas dentro del reactor.\n" +
                        "Solo llevas una barra a la vez.\n\n" +
                        "Estabiliza el reactor antes de\n" +
                        "que termine la cuenta regresiva.");
    }

    /** Muestra las vidas y los poderes del jugador. */
    private void showSurvival() {
        showSection("TRAINING: SUPERVIVENCIA",
                "SUPERVIVENCIA\n\n" +
                        "Comienzas con 3 vidas.\n" +
                        "Los enemigos quitan 1 vida.\n" +
                        "Con 0 vidas: GAME OVER.\n" +
                        "Sin tiempo: pierdes todas las vidas.\n\n" +
                        "PASTILLAS DE YODO\n" +
                        "Recoge y consume una para ganar\n" +
                        "1 vida adicional.\n\n" +
                        "TRAJE DE PLOMO\n" +
                        "Te da inmunidad por unos segundos.\n" +
                        "Ambos funcionan en los 3 niveles.");
    }

    /** Muestra como se ordenan los puntajes del ranking. */
    private void showRanking() {
        showSection("TRAINING: RANKING",
                "CLASIFICACION POR TIEMPO\n\n" +
                        "El ranking ordena a quienes\n" +
                        "completan el juego mas rapido.\n\n" +
                        "1. Menor tiempo: primer lugar.\n" +
                        "2. Segundo menor: segundo lugar.\n" +
                        "3. Tercer menor: tercer lugar.\n\n" +
                        "Entre menos tardes en terminar,\n" +
                        "mejor sera tu posicion.");
    }

    /**
     * Devuelve el panel principal de instrucciones.
     *
     * @return panel que se muestra en MainFrame
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Devuelve el boton para regresar al menu.
     *
     * @return boton Volver
     */
    public JButton getBackButton() {
        return backButton;
    }

    /**
     * Permite ejecutar esta vista por separado para probar el formulario.
     *
     * @param args argumentos de ejecucion
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame("PROJECT REACTOR - INSTRUCCIONES");
            InstructionsPanel instructions = new InstructionsPanel();
            instructions.getMainPanel().setPreferredSize(new Dimension(1280, 720));

            window.setContentPane(instructions.getMainPanel());
            instructions.getBackButton().addActionListener(e -> window.dispose());
            window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            window.pack();
            window.setLocationRelativeTo(null);
            window.setResizable(false);
            window.setVisible(true);
        });
    }
}
