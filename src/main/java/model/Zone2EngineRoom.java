package model;

import java.awt.*;

/**
 * Representa el segundo nivel del juego, la sala de maquinas.
 */
public class Zone2EngineRoom extends Zone {
    // Carpetas de los recursos del nivel 2
    private static final String SPRITES = "/Sprites/Nivel-2/";
    private static final String FRAMES = "/frames/Nivel-2/";

    private Rectangle centralGenerator = new Rectangle(525, 255, 160, 160);
    private Rectangle cleaningTable = new Rectangle(780, 335, 130, 110);

    private Rectangle[] electricalPanels = {
            new Rectangle(355, 170, 96, 96),
            new Rectangle(680, 130, 96, 96),
            new Rectangle(1035, 235, 96, 96)
    };

    private Rectangle[] wallVents = {
            new Rectangle(225, 225, 96, 96),
            new Rectangle(865, 185, 96, 96)
    };

    // Fondo y decoraciones fijas (PNG)
    private Image backgroundImg;
    private Image ventImg;
    private Image transformerImg;
    private Image ductsImg;

    // Generador central (GIF animados)
    private Image generatorDamagedImg;
    private Image generatorAssemblyImg;
    private Image generatorOnImg;

    // Mesa de limpieza (GIF animados)
    private Image cleaningTableIdleImg;
    private Image cleaningTableCleaningImg;

    // Tableros electricos (GIF animados)
    private Image panelUncalibratedImg;
    private Image panelCalibratedImg;

    // Piezas del mapa que cambian de imagen segun su estado
    private Decoration generator;
    private Decoration table;
    private boolean[] calibratedPanels = new boolean[electricalPanels.length];

    /**
     * Construye la sala de maquinas y ubica sus maquinas y decoraciones.
     */
    public Zone2EngineRoom() {
        super("Zona 2: Sala de Maquinas");

        int[] xPoints = {581, 1210, 719, 67};
        int[] yPoints = {158, 343, 686, 441};
        floorArea = new Polygon(xPoints, yPoints, 4);

        loadImages();
        placeMachines();
        placeTransformersAndDucts();
        sortDecorationsByDepth();
    }

    /**
     * Carga todas las imagenes del nivel 2 en un solo lugar.
     */
    private void loadImages() {
        backgroundImg = loadImage("/mapas_niveles/nivel2_fondo.png");

        ventImg = loadImage(FRAMES + "decoracion/decoracion_ventilacion.png");
        transformerImg = loadImage(FRAMES + "decoracion/decoracion_transformador.png");
        ductsImg = loadImage(FRAMES + "decoracion/decoracion_conductos.png");

        generatorDamagedImg = loadImage(SPRITES + "generador/generador_danado.gif");
        generatorAssemblyImg = loadImage(SPRITES + "generador/generador_ensamblaje.gif");
        generatorOnImg = loadImage(SPRITES + "generador/generador_encendido.gif");

        cleaningTableIdleImg = loadImage(SPRITES + "mesa_limpieza/mesa_limpieza_inactiva.gif");
        cleaningTableCleaningImg = loadImage(SPRITES + "mesa_limpieza/mesa_limpieza_limpiando.gif");

        panelUncalibratedImg = loadImage(SPRITES + "tablero_electrico/tablero_descalibrado.gif");
        panelCalibratedImg = loadImage(SPRITES + "tablero_electrico/tablero_calibrado.gif");
    }

    /**
     * Ubica el generador central y la mesa de limpieza con sus bases sobre el piso.
     */
    private void placeMachines() {
        Polygon generatorBase = new Polygon(
                new int[]{25, 80, 135, 80},
                new int[]{115, 142, 115, 88}, 4);

        Polygon tableBase = new Polygon(
                new int[]{18, 65, 112, 65},
                new int[]{75, 98, 75, 52}, 4);

        generator = new Decoration(generatorDamagedImg, centralGenerator, generatorBase);
        table = new Decoration(cleaningTableIdleImg, cleaningTable, tableBase);

        addDecoration(generator);
        addDecoration(table);
    }

    /**
     * Ubica los grupos de transformadores y conductos que pueblan la sala y forman pasillos.
     */
    private void placeTransformersAndDucts() {
        Polygon transformerBase = new Polygon(
                new int[]{18, 55, 92, 55},
                new int[]{85, 105, 85, 65}, 4);

        Polygon ductsBase = new Polygon(
                new int[]{16, 60, 104, 60},
                new int[]{64, 84, 64, 44}, 4);

        // Grupo superior, detras del generador central
        addDecoration(new Decoration(transformerImg, new Rectangle(640, 175, 110, 120), transformerBase));
        addDecoration(new Decoration(transformerImg, new Rectangle(720, 202, 110, 120), transformerBase));
        addDecoration(new Decoration(ductsImg, new Rectangle(805, 240, 120, 96), ductsBase));

        // Grupo izquierdo, formando pasillo desde la entrada
        addDecoration(new Decoration(ductsImg, new Rectangle(390, 265, 120, 96), ductsBase));
        addDecoration(new Decoration(transformerImg, new Rectangle(315, 300, 110, 120), transformerBase));

        // Isla inferior, frente al generador
        addDecoration(new Decoration(transformerImg, new Rectangle(510, 455, 110, 120), transformerBase));
        addDecoration(new Decoration(transformerImg, new Rectangle(590, 485, 110, 120), transformerBase));
        addDecoration(new Decoration(ductsImg, new Rectangle(675, 515, 120, 96), ductsBase));

        // Equipo en el extremo derecho
        addDecoration(new Decoration(transformerImg, new Rectangle(1005, 330, 110, 120), transformerBase));
    }

    /**
     * Muestra el generador parcialmente armado mientras recibe piezas.
     */
    public void showGeneratorAssembly() {
        generator.setImage(generatorAssemblyImg);
    }

    /**
     * Muestra el generador encendido al completar su reparacion.
     */
    public void showGeneratorOn() {
        generator.setImage(generatorOnImg);
    }

    /**
     * Cambia la mesa de limpieza entre inactiva y limpiando.
     *
     * @param cleaning true mientras el jugador limpia las piezas.
     */
    public void setTableCleaning(boolean cleaning) {
        if (cleaning) {
            table.setImage(cleaningTableCleaningImg);
        } else {
            table.setImage(cleaningTableIdleImg);
        }
    }

    /**
     * Marca un tablero electrico como calibrado.
     *
     * @param index Posicion del tablero (0, 1 o 2).
     */
    public void calibratePanel(int index) {
        calibratedPanels[index] = true;
    }

    /**
     * Comprueba que ambos pies queden dentro de las paredes de la sala.
     *
     * @param feet Rectangulo de los pies en la posicion propuesta.
     * @return true si toda la base del jugador cabe en el piso.
     */
    @Override
    protected boolean isOnFloor(Rectangle feet) {
        return floorArea.contains(feet);
    }

    @Override
    public void update(Agent agent) {
    }

    @Override
    public void draw(Graphics graphics, Agent agent) {
        drawBackgroundAndWalls(graphics);
        drawDecorations(graphics);
        drawZoneName(graphics);
    }

    /**
     * Dibuja al jugador a la profundidad que le corresponde entre las maquinas.
     *
     * @param graphics  Contexto grafico de Swing.
     * @param agent     Jugador activo.
     * @param drawAgent Accion que dibuja al jugador.
     */
    @Override
    public void drawScene(Graphics graphics, Agent agent, Runnable drawAgent) {
        drawBackgroundAndWalls(graphics);
        drawDecorationsWithAgent(graphics, agent, drawAgent);
        drawZoneName(graphics);
    }

    /**
     * Dibuja el fondo, los ductos de ventilacion y los tableros electricos en las paredes.
     *
     * @param graphics Contexto grafico de Swing.
     */
    private void drawBackgroundAndWalls(Graphics graphics) {
        graphics.drawImage(backgroundImg, 0, 0, 1280, 720, null);

        // La primera ventilacion va en espejo sobre la pared izquierda
        for (int i = 0; i < wallVents.length; i++) {
            drawOnWall(graphics, ventImg, wallVents[i], i == 0);
        }

        // El primer tablero va en espejo sobre la pared izquierda
        for (int i = 0; i < electricalPanels.length; i++) {
            Image panelImg = panelUncalibratedImg;
            if (calibratedPanels[i]) {
                panelImg = panelCalibratedImg;
            }
            drawOnWall(graphics, panelImg, electricalPanels[i], i == 0);
        }
    }

    /**
     * Dibuja una imagen sobre un muro, volteandola si esta en la pared izquierda.
     *
     * @param graphics Contexto grafico de Swing.
     * @param image    Imagen que se dibuja.
     * @param area     Posicion y tamano de la imagen.
     * @param mirrored true si la imagen se voltea horizontalmente.
     */
    private void drawOnWall(Graphics graphics, Image image, Rectangle area, boolean mirrored) {
        if (mirrored) {
            graphics.drawImage(image, area.x + area.width, area.y, -area.width, area.height, null);
        } else {
            graphics.drawImage(image, area.x, area.y, area.width, area.height, null);
        }
    }

    /**
     * Dibuja el nombre de la zona sobre la escena.
     *
     * @param graphics Contexto grafico de Swing.
     */
    private void drawZoneName(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.BOLD, 16));
        graphics.drawString("ZONA 2 - Sala de Maquinas", 20, 40);
    }

    /**
     * Indica si la zona 2 fue completada.
     *
     * @return true si el nivel fue completado, al contrario false.
     */
    @Override
    public boolean isCompleted() {
        return false;
    }
}
