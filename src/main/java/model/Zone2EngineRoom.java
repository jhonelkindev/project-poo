package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


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

    private List<Part> parts = new ArrayList<>();
    private int installedNuts;
    private int installedCables;

    // Acciones que se hacen manteniendo E: limpiar (1.5 s) e instalar (1 s)
    private long holdStart;
    private int holdDuration;
    private boolean holding;
    private boolean cleaning;
    private List<Enemy> rats = new ArrayList<>();

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
        placeParts();
        placeRats();
    }

    /**
     * Ubica las 6 tuercas y los 4 cables en el piso de la sala.
     */
    private void placeParts() {
        // Tuercas
        parts.add(new Part(240, 380, Part.NUT));
        parts.add(new Part(440, 380, Part.NUT));
        parts.add(new Part(720, 340, Part.NUT));
        parts.add(new Part(960, 300, Part.NUT));
        parts.add(new Part(280, 460, Part.NUT));
        parts.add(new Part(920, 460, Part.NUT));

        // Cables
        parts.add(new Part(160, 420, Part.CABLE));
        parts.add(new Part(720, 420, Part.CABLE));
        parts.add(new Part(400, 500, Part.CABLE));
        parts.add(new Part(840, 500, Part.CABLE));

    }


    /**
     * Recoge con F una pieza cercana, solo si el jugador tiene las manos libres.
     *
     * @param agent Jugador activo.
     */
    @Override
    public void pickUp(Agent agent) {
        if (!agent.hasPart()) {
            for (Part part : parts) {
                if (!part.isCollected() && isNear(agent, part.getBounds())) {
                    part.setCollected(true);
                    part.applyEffect(agent);
                    break;
                }
            }
        }
    }

    /**
     * Suelta con Q la pieza recogida
     *
     * @param agent Jugador activo.
     */
    @Override
    public void drop(Agent agent) {
        Part part = agent.dropPart();

        if (part != null) {
            part.placeAt(agent.getX() + 8, agent.getY() + 35);
        }
    }

    /**
     * Accion de la tecla E: limpia la pieza en la mesa o la entrega en el generador.
     *
     * @param agent Jugador activo.
     */
    @Override
    public void action(Agent agent) {
        Part part = agent.getCarriedPart();

        // En la mesa de limpieza: hay que mantener E durante 1.5 segundos
        if (part != null && !part.isClean() && isNear(agent, cleaningTable)) {
            cleaning = true;

            if (holdE(1500)) {
                part.cleanPart();
            }
        }

        // En el generador: solo se aceptan piezas limpias, manteniendo E durante 1 segundo
        if (part != null && part.isClean() && isNear(agent, centralGenerator)) {
            if (holdE(1000)) {
                deliverPart(agent);
            }
        }
    }

    /**
     * Cuenta el tiempo que el jugador lleva manteniendo la E.
     *
     * @param millis Tiempo que se debe mantener la E, en milisegundos.
     * @return true cuando ya se mantuvo la E el tiempo necesario.
     */
    private boolean holdE(int millis) {
        holding = true;
        holdDuration = millis;

        if (holdStart == 0) {
            holdStart = System.currentTimeMillis();
        }

        return System.currentTimeMillis() - holdStart >= millis;
    }

    /**
     * Comprueba si el jugador esta cerca de un area del mapa.
     *
     * @param agent Jugador activo.
     * @param area  Area de la pieza o de la maquina.
     * @return true si el jugador esta cerca.
     */
    private boolean isNear(Agent agent, Rectangle area) {
        Rectangle playerArea = new Rectangle(agent.getX() - 20, agent.getY() - 20,
                agent.getWidth() + 40, agent.getHeight() + 40);
        return playerArea.intersects(area);
    }

    /**
     * Instala en el generador la pieza limpia que lleva el jugador.
     *
     * @param agent Jugador activo.
     */
    private void deliverPart(Agent agent) {
        Part part = agent.dropPart();

        if (part.getType().equals(Part.NUT)) {
            installedNuts++;
        } else {
            installedCables++;
        }

        if (installedNuts == 6 && installedCables == 4) {
            showGeneratorOn();
        } else {
            showGeneratorAssembly();
        }
    }

    /**
     * Calcula cuanto lleva la accion de mantener E, de 0.0 (nada) a 1.0 (completa).
     *
     * @return Progreso de la accion.
     */
    private double getHoldProgress() {
        if (holdStart == 0) {
            return 0;
        }

        double progress = (double) (System.currentTimeMillis() - holdStart) / holdDuration;
        if (progress > 1) {
            progress = 1;
        }
        return progress;
    }

    /**
     * Dibuja una barra de progreso encima de una maquina.
     *
     * @param graphics Contexto grafico de Swing.
     * @param area     Area de la maquina.
     */
    private void drawProgressBar(Graphics graphics, Rectangle area) {
        int barX = area.x + area.width / 2 - 50;
        int barY = area.y - 8;

        graphics.setColor(Color.DARK_GRAY);
        graphics.fillRect(barX, barY, 100, 8);
        graphics.setColor(Color.GREEN);
        graphics.fillRect(barX, barY, (int) (100 * getHoldProgress()), 8);
    }

    /**
     * Dibuja las piezas que siguen en el piso.
     *
     * @param graphics Contexto grafico de Swing.
     */
    private void drawParts(Graphics graphics) {
        for (Part part : parts) {
            if (!part.isCollected()) {
                part.draw(graphics);
            }
        }
    }

    /**
     * Muestra cuantas piezas tiene el generador y cuantas carga el jugador.
     *
     * @param graphics Contexto grafico de Swing.
     * @param agent    Jugador activo.
     */
    private void drawObjectives(Graphics graphics, Agent agent) {
        String carrying = "nada";
        Part part = agent.getCarriedPart();

        if (part != null) {
            if (part.isClean()) {
                carrying = part.getType() + " (pieza limpia)";
            } else {
                carrying = part.getType() + " (pieza sucia)";
            }
        }

        String text = "Tuercas: " + installedNuts + "/6"
                + " | Cables: " + installedCables + "/4"
                + " | Llevas: " + carrying;

        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.BOLD, 16));
        graphics.drawString(text, 640, 40);
    }

    /**
     * Escribe encima de la mesa y del generador su nombre y lo que se puede hacer.
     *
     * @param graphics Contexto grafico de Swing.
     * @param agent    Jugador activo.
     */
    private void drawMachineLabels(Graphics graphics, Agent agent) {
        Part part = agent.getCarriedPart();

        // Mesa de limpieza
        String tableHint = null;
        if (isNear(agent, cleaningTable)) {
            if (part == null) {
                tableHint = "Trae una pieza sucia";
            } else if (part.isClean()) {
                tableHint = "Esta pieza ya esta limpia";
            } else {
                tableHint = "Manten E para limpiar";
            }
        }
        drawLabel(graphics, "MESA DE LIMPIEZA", tableHint, cleaningTable);

        // Generador central
        String generatorHint = null;
        if (isNear(agent, centralGenerator)) {
            if (part == null) {
                generatorHint = "Trae una pieza limpia";
            } else if (part.isClean()) {
                generatorHint = "Manten E para instalar";
            } else {
                generatorHint = "Primero limpia la pieza";
            }
        }
        drawLabel(graphics, "GENERADOR", generatorHint, centralGenerator);

        // Barra de progreso encima de la maquina que se esta usando
        if (holdStart != 0) {
            if (isNear(agent, cleaningTable)) {
                drawProgressBar(graphics, cleaningTable);
            } else {
                drawProgressBar(graphics, centralGenerator);
            }
        }
    }

    /**
     * Dibuja el nombre de una maquina centrado encima de ella y, si hay, una indicacion.
     *
     * @param graphics Contexto grafico de Swing.
     * @param title    Nombre de la maquina.
     * @param hint     Indicacion para el jugador, o null si no hay.
     * @param area     Area de la maquina.
     */
    private void drawLabel(Graphics graphics, String title, String hint, Rectangle area) {
        graphics.setFont(new Font("Arial", Font.BOLD, 13));
        FontMetrics metrics = graphics.getFontMetrics();
        int centerX = area.x + area.width / 2;

        graphics.setColor(Color.WHITE);
        graphics.drawString(title, centerX - metrics.stringWidth(title) / 2, area.y - 30);

        if (hint != null) {
            graphics.setColor(Color.YELLOW);
            graphics.drawString(hint, centerX - metrics.stringWidth(hint) / 2, area.y - 14);
        }
    }

    /**
     * Carga todas las imagenes del nivel 2.
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
     * Ubicamos las 4 ratas alrededor del mapa
     */
    private void placeRats() {
        rats.add(new RadioactivedRat(640, 260));
        rats.add(new RadioactivedRat(1040, 340));
        rats.add(new RadioactivedRat(800, 500));
        rats.add(new RadioactivedRat(480, 430));
        rats.add(new RadioactivedRat(510, 300));
    }


    /**
     * Obtenemos las ratas de la zona para que el hilo de enemigos las mueva
     *
     * @return lista de ratas
     */
    public List<Enemy> getRats() {
        return rats;
    }


    /**
     * Golpea con el espacio la primer rata que el jugador tenga cerca
     *
     * @param agent jugador
     */
    @Override
    public void attack(Agent agent) {
        for (Enemy rat : rats) {
            if (rat.isAlive() && isNear(agent, rat.getBounds())) {
                rat.hit();
                break;
            }
        }
    }

    /**
     * Dibuja los cadaveres de las ratas
     *
     * @param graphics
     */
    private void drawDeadRats(Graphics graphics) {
        for (Enemy rat : rats) {
            if (!rat.isAlive()) {
                rat.draw(graphics);
            }
        }
    }


    /**
     * Muestra las vidas del jugador. se ponen rojas al recibir un golpe
     *
     * @param graphics
     * @param agent
     */
    private void drawLives(Graphics graphics, Agent agent) {
        if (agent.isHurt()) {
            graphics.setColor(Color.RED);
        } else {
            graphics.setColor(Color.WHITE);
        }
        graphics.setFont(new Font("Arial", Font.BOLD, 16));
        graphics.drawString("Vidas: " + agent.getLives(), 20, 65);

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

    /**
     * Si el jugador solto la E o se alejo de la maquina, el progreso vuelve a cero.
     *
     * @param agent Jugador activo.
     */
    @Override
    public void update(Agent agent) {
        if (!holding) {
            holdStart = 0;
        }
        setTableCleaning(cleaning);

        holding = false;
        cleaning = false;
    }

    @Override
    public void draw(Graphics graphics, Agent agent) {
        drawBackgroundAndWalls(graphics);
        drawParts(graphics);
        drawDeadRats(graphics);
        drawDecorations(graphics);
        for (Enemy rat : rats) {
            if (rat.isAlive()) {
                rat.draw(graphics);
            }
        }
        drawZoneName(graphics);
        drawLives(graphics, agent);
        drawObjectives(graphics, agent);
        drawMachineLabels(graphics, agent);
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
        drawParts(graphics);
        drawDeadRats(graphics);
        drawDecorationsWithAgent(graphics, agent, drawAgent, rats);
        drawZoneName(graphics);
        drawLives(graphics, agent);
        drawObjectives(graphics, agent);
        drawMachineLabels(graphics, agent);
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
