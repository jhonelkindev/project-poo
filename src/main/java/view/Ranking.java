package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Vista encargada de mostrar el ranking del juego.
 * Muestra los tres mejores jugadores y el historial
 * de todas las partidas realizadas.
 */
public class Ranking {

    private JPanel mainPanel;
    private JLabel topTitleLabel;
    private JLabel historyTitleLabel;
    private JScrollPane topScrollPane;
    private JScrollPane historyScrollPane;
    private JTable topTable;
    private JTable historyTable;
    private JButton backButton;

    private DefaultTableModel topModel;
    private DefaultTableModel historyModel;

    /**
     * Constructor que configura los componentes,
     * el botón de volver y las tablas del ranking.
     */
    public Ranking() {
        configureComponents();
        configureButton();
        showResults(new ArrayList<>());
    }

    /**
     * Crea el panel principal con la imagen de fondo.
     */
    private void createUIComponents() {
        mainPanel = new PanelImage("/menus/puntajes.png");
    }

    /**
     * Configura los títulos, las tablas y las posiciones
     * de los componentes dentro del panel principal.
     */
    private void configureComponents() {
        mainPanel.setPreferredSize(new Dimension(1280, 720));
        configureTitle(topTitleLabel, "TOP 3", 18);
        configureTitle(historyTitleLabel, "TODOS LOS JUGADORES", 16);

        topModel = configureTable(topTable, topScrollPane, false);
        historyModel = configureTable(historyTable, historyScrollPane, true);

        // Ajusta las posiciones al tamaño del fondo.
        mainPanel.setLayout(new LayoutManager() {

            @Override
            public void addLayoutComponent(String name, Component component) {}

            @Override
            public void removeLayoutComponent(Component component) {}

            @Override
            public Dimension preferredLayoutSize(Container parent) {
                return new Dimension(1280, 720);
            }

            @Override
            public Dimension minimumLayoutSize(Container parent) {
                return new Dimension(1024, 687);
            }

            @Override
            public void layoutContainer(Container parent) {
                place(topTitleLabel, 91, 215, 214, 19);
                place(topScrollPane, 53, 255, 305, 118);
                place(historyTitleLabel, 700, 181, 254, 23);
                place(historyScrollPane, 700, 208, 254, 158);
                place(backButton, 440, 623, 144, 48);
            }
        });
    }

    /**
     * Ubica un componente de forma proporcional
     * al tamaño actual del panel.
     *
     * @param component componente que se va a ubicar.
     * @param x posición horizontal.
     * @param y posición vertical.
     * @param width ancho del componente.
     * @param height alto del componente.
     */
    private void place(Component component, int x, int y,
                       int width, int height) {

        // Tamaño original de la imagen: 1024 x 687.
        double sx = mainPanel.getWidth() / 1024.0;
        double sy = mainPanel.getHeight() / 687.0;

        component.setBounds((int) (x * sx), (int) (y * sy),
                (int) (width * sx), (int) (height * sy));
    }

    /**
     * Configura el texto, color, tamaño y alineación
     * de los títulos del ranking.
     *
     * @param label etiqueta que se va a configurar.
     * @param text texto que se va a mostrar.
     * @param size tamaño de la letra.
     */
    private void configureTitle(JLabel label, String text, int size) {
        label.setText(text);
        label.setFont(new Font("Monospaced", Font.BOLD, size));
        label.setForeground(new Color(182, 242, 91));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setOpaque(false);
    }

    /**
     * Configura el diseño y las columnas de una tabla.
     * También controla los colores y el desplazamiento.
     *
     * @param table tabla que se va a configurar.
     * @param scrollPane panel de desplazamiento de la tabla.
     * @param history indica si la tabla es la del historial.
     * @return modelo utilizado para guardar los datos de la tabla.
     */
    private DefaultTableModel configureTable(JTable table,
                                             JScrollPane scrollPane,
                                             boolean history) {

        // Define las columnas dependiendo de la tabla.
        String[] columns = history
                ? new String[]{"Jugador", "Tiempo"}
                : new String[]{"Pos.", "Jugador", "Tiempo"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(model);
        table.setFont(new Font("Monospaced", Font.BOLD, 15));
        table.setForeground(new Color(226, 239, 229));
        table.setBackground(new Color(17, 27, 35));
        table.setRowHeight(26);
        table.setShowGrid(false);
        table.setFocusable(false);
        table.setRowSelectionAllowed(false);

        // Configura los encabezados de las columnas.
        table.getTableHeader().setFont(new Font("Monospaced", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(32, 47, 56));
        table.getTableHeader().setForeground(new Color(182, 242, 91));
        table.getTableHeader().setReorderingAllowed(false);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        // Personaliza la apariencia de las celdas.
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object value, boolean selected,
                    boolean focus, int row, int column) {

                super.getTableCellRendererComponent(
                        t, value, false, false, row, column);

                setBackground(new Color(17, 27, 35));
                setForeground(new Color(226, 239, 229));

                setHorizontalAlignment(column == (history ? 0 : 1)
                        ? SwingConstants.LEFT : SwingConstants.CENTER);

                // Colores para los tres primeros puestos.
                if (!history) {
                    if (row == 0) setForeground(new Color(255, 215, 80));
                    if (row == 1) setForeground(new Color(205, 214, 223));
                    if (row == 2) setForeground(new Color(205, 145, 95));
                }

                setToolTipText(value == null ? null : value.toString());

                return this;
            }
        };

        table.setDefaultRenderer(Object.class, renderer);

        // Ajusta el ancho de las columnas de cada tabla.
        if (!history) {
            table.getColumnModel().getColumn(0).setPreferredWidth(45);
            table.getColumnModel().getColumn(1).setPreferredWidth(185);
            table.getColumnModel().getColumn(2).setPreferredWidth(100);
        } else {
            table.getColumnModel().getColumn(0).setPreferredWidth(180);
            table.getColumnModel().getColumn(1).setPreferredWidth(95);
        }

        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        scrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Solo el historial necesita desplazamiento vertical.
        scrollPane.setVerticalScrollBarPolicy(history
                ? ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
                : ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        scrollPane.getVerticalScrollBar().setUnitIncrement(26);

        return model;
    }

    /**
     * Configura la imagen y la apariencia del botón de volver.
     * Si no encuentra la imagen, muestra un texto.
     */
    private void configureButton() {

        URL url = getClass().getResource(
                "/menus/instrucciones_botones/btn_volver_hover.png");

        if (url != null) {
            backButton.setIcon(new ImageIcon(url));
            backButton.setText("");
        } else {
            backButton.setText("VOLVER");
            backButton.setForeground(new Color(182, 242, 91));
        }

        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        backButton.setMargin(new Insets(0, 0, 0, 0));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * Actualiza el historial y los tres mejores jugadores.
     * Las partidas completadas se ordenan de menor a mayor tiempo.
     *
     * @param results lista de resultados de las partidas.
     */
    public void showResults(List<PlayerResult> results) {

        // Limpia los datos anteriores de las tablas.
        topModel.setRowCount(0);
        historyModel.setRowCount(0);

        List<PlayerResult> completed = new ArrayList<>();

        for (PlayerResult result : results) {

            historyModel.addRow(new Object[]{result.name,
                    formatTime(result.seconds)
                            + (result.completed ? "" : " *")});

            if (result.completed) {
                completed.add(result);
            }
        }

        // Ordena las partidas completadas por tiempo.
        completed.sort(Comparator.comparingLong(result -> result.seconds));

        // Agrega como máximo los tres mejores resultados.
        for (int i = 0; i < Math.min(3, completed.size()); i++) {

            PlayerResult result = completed.get(i);

            topModel.addRow(new Object[]{
                    i + 1,
                    result.name,
                    formatTime(result.seconds)
            });
        }

        historyTitleLabel.setToolTipText("* Partida no completada");
    }

    /**
     * Convierte el tiempo en segundos al formato minutos:segundos.
     *
     * @param seconds tiempo total en segundos.
     * @return tiempo con formato MM:SS.
     */
    private String formatTime(long seconds) {
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }

    /**
     * Representa el resultado de una partida.
     * Clase temporal hasta conectar el ScoreManager.
     */
    public static class PlayerResult {

        private final String name;
        private final long seconds;
        private final boolean completed;

        /**
         * Constructor que guarda los datos de una partida.
         *
         * @param name nombre del jugador.
         * @param seconds tiempo de la partida en segundos.
         * @param completed indica si el jugador completó el juego.
         * @throws IllegalArgumentException si el tiempo es negativo.
         */
        public PlayerResult(String name, long seconds, boolean completed) {

            if (seconds < 0)
                throw new IllegalArgumentException("Tiempo negativo");

            this.name = name;
            this.seconds = seconds;
            this.completed = completed;
        }
    }

    /**
     * Obtiene el panel principal del ranking.
     *
     * @return panel principal.
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Obtiene el botón de volver.
     *
     * @return botón de volver.
     */
    public JButton getBackButton() {
        return backButton;
    }

    /**
     * Método temporal para probar la vista del ranking
     * sin ejecutar el juego completo.
     *
     * @param args argumentos de ejecución.
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame window = new JFrame("PROJECT REACTOR - RANKING");

            Ranking results = new Ranking();

            // Datos de prueba para las tablas.
            results.showResults(List.of(
                    new PlayerResult("Jugador A", 312, true),
                    new PlayerResult("Jugador B", 265, true),
                    new PlayerResult("Jugador C", 398, true),
                    new PlayerResult("Jugador D", 120, false),
                    new PlayerResult("Jugador E", 510, true)
            ));

            window.setContentPane(results.getMainPanel());

            results.getBackButton().addActionListener(e -> {

                WelcomePanel welcome = new WelcomePanel();

                window.setContentPane(welcome.getPanel1());

                welcome.getRankingButton().addActionListener(event -> {

                    window.setContentPane(results.getMainPanel());
                    window.revalidate();
                    window.repaint();
                });

                window.revalidate();
                window.repaint();
            });

            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.pack();
            window.setLocationRelativeTo(null);
            window.setResizable(false);
            window.setVisible(true);
        });
    }
}