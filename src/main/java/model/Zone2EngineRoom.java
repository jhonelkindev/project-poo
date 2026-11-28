package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Zone2EngineRoom extends Zone {
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

    private Image backgroundImg;
    private Image ventImg;
    private Image transformerImg;
    private Image ductsImg;

    private Image generatorDamagedImg;
    private Image generatorAssemblyImg;
    private Image generatorOnImg;

    private Image cleaningTableIdleImg;
    private Image cleaningTableCleaningImg;

    private Image panelUncalibratedImg;
    private Image panelCalibratedImg;

    private Decoration generator;
    private Decoration table;
    private boolean[] calibratedPanels = new boolean[electricalPanels.length];

    private List<Part> parts = new ArrayList<>();
    private int installedNuts;
    private int installedCables;

    private long holdStart;
    private int holdDuration;
    private boolean holding;
    private boolean cleaning;

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

    private void placeParts() {
        parts.add(new Part(240, 380, Part.NUT));
        parts.add(new Part(440, 380, Part.NUT));
        parts.add(new Part(720, 340, Part.NUT));
        parts.add(new Part(960, 300, Part.NUT));
        parts.add(new Part(280, 460, Part.NUT));
        parts.add(new Part(920, 460, Part.NUT));

        parts.add(new Part(160, 420, Part.CABLE));
        parts.add(new Part(720, 420, Part.CABLE));
        parts.add(new Part(400, 500, Part.CABLE));
        parts.add(new Part(840, 500, Part.CABLE));
    }

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

    @Override
    public void drop(Agent agent) {
        Part part = agent.dropPart();

        if (part != null) {
            part.placeAt(agent.getX() + 8, agent.getY() + 35);
        }
    }

    @Override
    public void action(Agent agent) {
        Part part = agent.getCarriedPart();

        if (part != null && !part.isClean() && isNear(agent, cleaningTable)) {
            cleaning = true;

            if (holdE(1500)) {
                part.cleanPart();
            }
        }

        if (part != null && part.isClean() && isNear(agent, centralGenerator)) {
            if (holdE(1000)) {
                deliverPart(agent);
            }
        }
    }

    private boolean holdE(int millis) {
        holding = true;
        holdDuration = millis;

        if (holdStart == 0) {
            holdStart = System.currentTimeMillis();
        }

        return System.currentTimeMillis() - holdStart >= millis;
    }

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

    private void drawProgressBar(Graphics graphics, Rectangle area) {
        int barX = area.x + area.width / 2 - 50;
        int barY = area.y - 8;

        graphics.setColor(Color.DARK_GRAY);
        graphics.fillRect(barX, barY, 100, 8);

        graphics.setColor(Color.GREEN);
        graphics.fillRect(barX, barY, (int) (100 * getHoldProgress()), 8);
    }

    private void drawParts(Graphics graphics) {
        for (Part part : parts) {
            if (!part.isCollected()) {
                part.draw(graphics);
            }
        }
    }


    private void drawMachineLabels(Graphics graphics, Agent agent) {
        Part part = agent.getCarriedPart();

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

        if (holdStart != 0) {
            if (isNear(agent, cleaningTable)) {
                drawProgressBar(graphics, cleaningTable);
            } else {
                drawProgressBar(graphics, centralGenerator);
            }
        }
    }

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

    private void placeTransformersAndDucts() {
        Polygon transformerBase = new Polygon(
                new int[]{18, 55, 92, 55},
                new int[]{85, 105, 85, 65}, 4);

        Polygon ductsBase = new Polygon(
                new int[]{16, 60, 104, 60},
                new int[]{64, 84, 64, 44}, 4);

        // Grupo superior, detrás del generador.
        addDecoration(new Decoration(transformerImg, new Rectangle(640, 175, 110, 120), transformerBase));
        addDecoration(new Decoration(transformerImg, new Rectangle(720, 202, 110, 120), transformerBase));
        addDecoration(new Decoration(ductsImg, new Rectangle(805, 240, 120, 96), ductsBase));

        // Pasillo izquierdo.
        addDecoration(new Decoration(ductsImg, new Rectangle(390, 265, 120, 96), ductsBase));
        addDecoration(new Decoration(transformerImg, new Rectangle(315, 300, 110, 120), transformerBase));

        // Grupo inferior, frente al generador.
        addDecoration(new Decoration(transformerImg, new Rectangle(510, 455, 110, 120), transformerBase));
        addDecoration(new Decoration(transformerImg, new Rectangle(590, 485, 110, 120), transformerBase));
        addDecoration(new Decoration(ductsImg, new Rectangle(675, 515, 120, 96), ductsBase));

        addDecoration(new Decoration(transformerImg, new Rectangle(1005, 330, 110, 120), transformerBase));
    }

    private void placeRats() {
        enemies.add(new RadioactivedRat(640, 260));
        enemies.add(new RadioactivedRat(1040, 340));
        enemies.add(new RadioactivedRat(800, 500));
        enemies.add(new RadioactivedRat(480, 430));
        enemies.add(new RadioactivedRat(510, 300));
    }

    public List<Enemy> getRats() {
        return getEnemies();
    }

    private void drawDeadRats(Graphics graphics) {
        for (Enemy rat : enemies) {
            if (!rat.isAlive()) {
                rat.draw(graphics);
            }
        }
    }


    public void showGeneratorAssembly() {
        generator.setImage(generatorAssemblyImg);
    }

    public void showGeneratorOn() {
        generator.setImage(generatorOnImg);
    }

    public void setTableCleaning(boolean cleaning) {
        if (cleaning) {
            table.setImage(cleaningTableCleaningImg);
        } else {
            table.setImage(cleaningTableIdleImg);
        }
    }

    public void calibratePanel(int index) {
        calibratedPanels[index] = true;
    }

    @Override
    protected boolean isOnFloor(Rectangle feet) {
        return floorArea.contains(feet);
    }

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
    public synchronized void draw(Graphics graphics, Agent agent) {
        drawBackgroundAndWalls(graphics);
        drawParts(graphics);
        drawDeadRats(graphics);
        drawDecorations(graphics);

        for (Enemy rat : enemies) {
            if (rat.isAlive()) {
                rat.draw(graphics);
            }
        }
        drawMachineLabels(graphics, agent);
    }

    @Override
    public synchronized void drawScene(Graphics graphics, Agent agent, Runnable drawAgent) {
        drawBackgroundAndWalls(graphics);
        drawParts(graphics);
        drawDeadRats(graphics);
        drawDecorationsWithAgent(graphics, agent, drawAgent, enemies);
        drawMachineLabels(graphics, agent);
    }

    private void drawBackgroundAndWalls(Graphics graphics) {
        graphics.drawImage(backgroundImg, 0, 0, 1280, 720, null);

        for (int i = 0; i < wallVents.length; i++) {
            drawOnWall(graphics, ventImg, wallVents[i], i == 0);
        }

        for (int i = 0; i < electricalPanels.length; i++) {
            Image panelImg = panelUncalibratedImg;

            if (calibratedPanels[i]) {
                panelImg = panelCalibratedImg;
            }

            drawOnWall(graphics, panelImg, electricalPanels[i], i == 0);
        }
    }

    private void drawOnWall(Graphics graphics, Image image, Rectangle area, boolean mirrored) {
        if (mirrored) {
            graphics.drawImage(image, area.x + area.width, area.y,
                    -area.width, area.height, null);
        } else {
            graphics.drawImage(image, area.x, area.y, area.width, area.height, null);
        }
    }


    @Override
    public boolean isCompleted() {
        return false;
    }

    @Override
    public String getObjectiveText() {
        return "Tuercas: " + installedNuts + "/6   Cables: " + installedCables + "/4";
    }
}