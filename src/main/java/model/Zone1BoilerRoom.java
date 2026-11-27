package model;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa el primer nivel del juego, la sala de calderas. Las acciones y
 * el dibujo se sincronizan para que Swing muestre cada entrega completa.
 */
public class Zone1BoilerRoom extends Zone {
    private Rectangle rack = new Rectangle(300, 400, 130, 130);

    private Rectangle[] boilers = {
            new Rectangle(566, 149, 128, 128),
            new Rectangle(951, 290, 128, 128),
            new Rectangle(566, 466, 128, 128)
    };

    private Rectangle[] exhaustFans = {
            new Rectangle(160, 290, 96, 96),
            new Rectangle(880, 420, 96, 96)
    };

    private Rectangle[] tubes = {
            new Rectangle(270, 185, 96, 96),
            new Rectangle(425, 96, 96, 96),
            new Rectangle(775, 135, 96, 96),
            new Rectangle(1060, 237, 96, 96)
    };

    private Image backgroundImg;
    private Image[] boilerSprites;
    private Image[] pipeSprites;

    // Primero se registran las tres calderas y después las cuatro fugas.
    // Un mismo índice identifica el dibujo, el área de trabajo y su contador.
    private Decoration[] machines = new Decoration[boilers.length + tubes.length];
    private Rectangle[] repairAreas = new Rectangle[machines.length];
    private int[] installedParts = new int[machines.length];
    private int partsPerMachine = 2;

    private List<Part> shelfParts = new ArrayList<>();
    private Rectangle rackFront = new Rectangle(rack.x + 16, rack.y + 98, 98, 32);

    // Solo se cronometra la pieza y la máquina que se están usando con E.
    private int activeMachine = -1;
    private Part activePart;
    private long holdStart;
    private int installMillis = 1000;
    private boolean holding;

    /** Construye la sala y coloca sus máquinas, obstáculos y objetos. */
    public Zone1BoilerRoom() {
        super("Zona 1: Sala de Calderas");
        int[] xPoints = {565, 1238, 715, 36};
        int[] yPoints = {128, 369, 675, 433};
        floorArea = new Polygon(xPoints, yPoints, 4);

        loadImages();
        placeMachines();
        placeTanks();
        placeFloorPipes();
        sortDecorationsByDepth();
        placeParts();
        placeSupplies();
    }

    /** Carga los sprites en orden de avería, reparación en curso y reparación terminada. */
    private void loadImages() {
        backgroundImg = loadImage("/mapas_niveles/nivel1_fondo.png");
        String boilerFolder = "/Sprites/Nivel-1/Maquina caldera/";
        boilerSprites = new Image[]{
                loadImage(boilerFolder + "caldera_danada.gif"),
                loadImage(boilerFolder + "caldera_apagada.gif"),
                loadImage(boilerFolder + "caldera_reparada.gif")
        };
        String pipeFolder = "/Sprites/Nivel-1/Tubos/";
        pipeSprites = new Image[]{
                loadImage(pipeFolder + "tuberia_dañada.gif"),
                loadImage(pipeFolder + "tuberia_arreglando.gif"),
                loadImage(pipeFolder + "tuberia_arreglada.gif")
        };
    }

    /** Registra cada máquina y la zona del pasillo desde la que se puede reparar. */
    private void placeMachines() {
        Image rackImg = loadImage("/frames/Nivel-1/Diseños/Estanteria/estanteria_repuestos.png");
        Image fanImg = loadImage("/Sprites/Nivel-1/Extractor/extractor_activo.gif");
        Polygon rackBase = new Polygon(
                new int[]{24, 77, 103, 50}, new int[]{93, 111, 96, 77}, 4);
        Polygon boilerBase = new Polygon(
                new int[]{22, 32, 63, 84, 103, 87, 56, 33},
                new int[]{94, 106, 110, 108, 88, 81, 79, 84}, 8);
        Polygon fanBase = new Polygon(
                new int[]{16, 60, 81, 38}, new int[]{68, 84, 71, 55}, 4);

        // Los tubos se dibujan con el estante para que se respete la profundidad.
        addDecoration(new Decoration(rackImg, rack, rackBase) {
            @Override
            public void draw(Graphics graphics) {
                super.draw(graphics);
                for (Part part : shelfParts) {
                    part.draw(graphics);
                }
            }
        });

        for (int i = 0; i < boilers.length; i++) {
            Rectangle boiler = boilers[i];
            machines[i] = new Decoration(boilerSprites[0], boiler, boilerBase);
            repairAreas[i] = new Rectangle(boiler.x + 14, boiler.y + 98, 100, 42);
            addDecoration(machines[i]);
        }
        for (int i = 0; i < tubes.length; i++) {
            int index = boilers.length + i;
            Rectangle tube = tubes[i];
            Rectangle drawing = tube;
            if (i < 2) {
                // El ancho negativo voltea el sprite sobre la pared izquierda.
                drawing = new Rectangle(tube.x + tube.width, tube.y, -tube.width, tube.height);
            }
            machines[index] = new Decoration(pipeSprites[0], drawing);
            repairAreas[index] = new Rectangle(tube.x + 12, tube.y + tube.height, 72, 44);
            addDecoration(machines[index]);
        }
        for (Rectangle fan : exhaustFans) {
            addDecoration(new Decoration(fanImg, fan, fanBase));
        }
    }

    /** Coloca seis tuercas y cinco tubos en el piso, y los otros tres tubos en el estante. */
    private void placeParts() {
        int[][] nuts = {{701, 475}, {560, 367}, {660, 273}, {993, 428}, {498, 400}, {699, 575}};
        for (int[] point : nuts) {
            addPart(point[0], point[1], Part.NUT);
        }
        int[][] pipes = {{677, 516}, {434, 396}, {697, 301}, {867, 433}, {608, 302}};
        for (int[] point : pipes) {
            addPart(point[0], point[1], Part.PIPE);
        }
        int[][] slots = {{30, 30}, {55, 40}, {42, 66}};
        for (int[] slot : slots) {
            Part pipe = addPart(rack.x + slot[0], rack.y + slot[1], Part.PIPE);
            shelfParts.add(pipe);
        }
    }

    /**
     * Crea un repuesto con la misma clase Part que usa el generador.
     * Las piezas de esta sala se colocan listas para instalar.
     *
     * @param x Posición horizontal del repuesto.
     * @param y Posición vertical del repuesto.
     * @param type Tuerca o tubo.
     * @return Pieza registrada en el nivel.
     */
    private Part addPart(int x, int y, String type) {
        Part part = new Part(x, y, type);
        part.cleanPart();
        gameObjects.add(part);
        return part;
    }

    /** Coloca el yodo y el traje en dos puntos distintos de los pasillos. */
    private void placeSupplies() {
        List<Point> spots = new ArrayList<>(List.of(new Point(523, 510), new Point(808, 505),
                new Point(529, 352), new Point(399, 437)));
        Collections.shuffle(spots);
        Point iodine = spots.get(0);
        Point suit = spots.get(1);
        gameObjects.add(new BoilerRoomSupply(iodine.x, iodine.y, BoilerRoomSupply.Type.IODINE));
        gameObjects.add(new BoilerRoomSupply(suit.x, suit.y, BoilerRoomSupply.Type.LEAD_SUIT));
    }

    /**
     * Ubica los tres grupos de tanques que forman los pasillos del nivel.
     */
    private void placeTanks() {
        Image horizontal = new ImageIcon(getClass().getResource("/frames/Nivel-1/Diseños/Decoracion/tanque_horizontal.png")).getImage();

        Image vertical = new ImageIcon(getClass().getResource("/frames/Nivel-1/Diseños/Decoracion/tanque_vertical.png")).getImage();

        Image auxiliary = new ImageIcon(getClass().getResource("/frames/Nivel-1/Diseños/Decoracion/tanque_auxiliar.png")).getImage();

        // Puntos sobre el piso, relativos a la esquina de cada imagen dibujada.
        Polygon horizontalBase = new Polygon(
                new int[]{12, 82, 99, 29},
                new int[]{59, 84, 72, 47}, 4);

        Polygon verticalBase = new Polygon(
                new int[]{14, 27, 47, 58, 55, 42, 25, 14},
                new int[]{80, 75, 75, 81, 89, 93, 92, 87}, 8);

        Polygon auxiliaryBase = new Polygon(
                new int[]{9, 17, 31, 38, 37, 25, 11},
                new int[]{59, 55, 55, 59, 65, 69, 66}, 7);

        // Grupo izquierdo, junto a la estanteria.
        addDecoration(new Decoration(horizontal, new Rectangle(374, 194, 128, 96), horizontalBase));
        addDecoration(new Decoration(horizontal, new Rectangle(458, 224, 128, 96), horizontalBase));
        addDecoration(new Decoration(vertical, new Rectangle(484, 269, 72, 96), verticalBase));
        addDecoration(new Decoration(auxiliary, new Rectangle(447, 318, 48, 72), auxiliaryBase));

        // Grupo del fondo, entre las dos calderas.
        addDecoration(new Decoration(vertical, new Rectangle(860, 192, 72, 96), verticalBase));
        addDecoration(new Decoration(vertical, new Rectangle(813, 219, 72, 96), verticalBase));
        addDecoration(new Decoration(vertical, new Rectangle(766, 246, 72, 96), verticalBase));
        addDecoration(new Decoration(horizontal, new Rectangle(797, 265, 128, 96), horizontalBase));

        // Grupo inferior, cerca de la caldera del frente.
        addDecoration(new Decoration(horizontal, new Rectangle(595, 340, 128, 96), horizontalBase));
        addDecoration(new Decoration(horizontal, new Rectangle(679, 370, 128, 96), horizontalBase));
        addDecoration(new Decoration(vertical, new Rectangle(777, 387, 72, 96), verticalBase));
        addDecoration(new Decoration(auxiliary, new Rectangle(747, 438, 48, 72), auxiliaryBase));
    }

    /**
     * Ubica los tubos del piso y conserva los huecos del codo y de la union en T.
     */
    private void placeFloorPipes() {
        Image straight = new ImageIcon(getClass().getResource("/frames/Nivel-1/Diseños/Decoracion/tuberia_recta.png")).getImage();

        Image valve = new ImageIcon(getClass().getResource("/frames/Nivel-1/Diseños/Decoracion/tuberia_valvula.png")).getImage();

        Image elbow = new ImageIcon(getClass().getResource("/frames/Nivel-1/Diseños/Decoracion/tuberia_codo.png")).getImage();

        Image tee = new ImageIcon(getClass().getResource("/frames/Nivel-1/Diseños/Decoracion/tuberia_union_t.png")).getImage();

        Polygon straightBase = new Polygon(
                new int[]{21, 65, 74, 29},
                new int[]{45, 63, 56, 38}, 4);

        Polygon valveBase = new Polygon(
                new int[]{21, 65, 74, 29},
                new int[]{48, 66, 59, 41}, 4);

        Polygon elbowBase = new Polygon(
                new int[]{16, 40, 48, 79, 74, 46, 26, 19},
                new int[]{52, 38, 38, 48, 57, 47, 61, 60}, 8);

        Polygon teeBase = new Polygon(
                new int[]{21, 29, 73, 68, 47, 32, 25, 34, 21},
                new int[]{39, 37, 54, 63, 55, 65, 60, 50, 45}, 9);

        addDecoration(new Decoration(straight, new Rectangle(255, 324, 96, 96), straightBase));
        addDecoration(new Decoration(valve, new Rectangle(307, 343, 96, 96), valveBase));
        addDecoration(new Decoration(straight, new Rectangle(252, 320, 96, 96), straightBase));
        addDecoration(new Decoration(elbow, new Rectangle(480, 430, 96, 96), elbowBase));
        addDecoration(new Decoration(valve, new Rectangle(1110, 325, 96, 96), valveBase));
        addDecoration(new Decoration(tee, new Rectangle(725, 532, 96, 96), teeBase));
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
     * Recoge con F el objeto disponible más cercano. Los repuestos van a la
     * mano y las ayudas se aplican en el momento de recogerlas.
     *
     * @param agent Jugador que presionó F.
     */
    @Override
    public synchronized void pickUp(Agent agent) {
        Rectangle reach = new Rectangle(agent.getX() - 20, agent.getY() - 20,
                agent.getWidth() + 40, agent.getHeight() + 40);
        Rectangle feet = agent.getBounds();
        GameObject nearest = null;
        double bestDistance = Double.POSITIVE_INFINITY;

        for (GameObject item : gameObjects) {
            if (item.isCollected() || (item instanceof Part && agent.hasPart())) {
                continue;
            }
            if (item instanceof BoilerRoomSupply supply && !supply.canUse(agent)) {
                continue;
            }
            Rectangle area = shelfParts.contains(item) ? rackFront : item.getBounds();
            if (reach.intersects(area)) {
                double distance = Point.distanceSq(feet.getCenterX(), feet.getCenterY(),
                        area.getCenterX(), area.getCenterY());
                if (distance < bestDistance) {
                    nearest = item;
                    bestDistance = distance;
                }
            }
        }

        if (nearest != null) {
            nearest.setCollected(true);
            nearest.applyEffect(agent);
            shelfParts.remove(nearest);
        }
    }

    /**
     * Deja con Q el mismo objeto a los pies del agente, como en la zona 2.
     *
     * @param agent Jugador que presionó Q.
     */
    @Override
    public synchronized void drop(Agent agent) {
        Part part = agent.dropPart();
        if (part != null) {
            part.placeAt(agent.getX() + 8, agent.getY() + 35);
            cancelHold();
        }
    }

    /**
     * Intenta instalar un repuesto mientras E esté presionada. La pieza
     * permanece en la mano hasta completar un segundo frente a la máquina.
     *
     * @param agent Jugador que mantiene E.
     */
    @Override
    public synchronized void action(Agent agent) {
        int index = getNearbyMachine(agent);
        Part part = agent.getCarriedPart();
        if (index < 0 || part == null || installedParts[index] >= partsPerMachine) {
            return;
        }
        if (requiredType(index).equals(part.getType()) && holdE(index, part)) {
            deliverPart(agent, index);
        }
    }

    /**
     * Comprueba el centro de los pies para distinguir las dos máquinas que
     * quedan juntas en la esquina derecha.
     *
     * @param agent Jugador que busca una máquina.
     * @return Índice de la máquina alcanzable, o -1 si está lejos.
     */
    private int getNearbyMachine(Agent agent) {
        Rectangle feet = agent.getBounds();
        for (int i = 0; i < repairAreas.length; i++) {
            if (repairAreas[i].contains(feet.getCenterX(), feet.getCenterY())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @param index Índice de la máquina dentro del mapa.
     * @return Tuerca para una caldera o tubo para una fuga.
     */
    private String requiredType(int index) {
        return index < boilers.length ? Part.NUT : Part.PIPE;
    }

    /**
     * Empieza de cero si se cambia de máquina o de pieza durante la entrega.
     *
     * @param index Máquina que se está reparando.
     * @param part Repuesto que sigue en la mano.
     * @return true cuando se completa el tiempo de instalación.
     */
    private boolean holdE(int index, Part part) {
        holding = true;
        if (activeMachine != index || activePart != part) {
            activeMachine = index;
            activePart = part;
            holdStart = System.nanoTime();
        }
        return getHoldProgress() >= 1.0;
    }

    /** @return Fracción de la barra que se ha llenado, entre cero y uno. */
    private double getHoldProgress() {
        if (activeMachine < 0) {
            return 0;
        }
        double elapsedMillis = (System.nanoTime() - holdStart) / 1_000_000.0;
        return Math.min(1.0, elapsedMillis / installMillis);
    }

    /**
     * Cuenta una pieza en esta máquina, cambia su imagen y libera la mano.
     *
     * @param agent Jugador que terminó de instalar la pieza.
     * @param index Máquina que aceptó el repuesto.
     */
    private void deliverPart(Agent agent, int index) {
        installedParts[index]++;
        Image[] sprites = index < boilers.length ? boilerSprites : pipeSprites;
        machines[index].setImage(sprites[installedParts[index]]);
        agent.dropPart();
        cancelHold();
    }

    /** Descarta el tiempo de una entrega terminada o interrumpida. */
    private void cancelHold() {
        activeMachine = -1;
        activePart = null;
        holding = false;
    }

    /**
     * El controlador llama a action antes de update en cada fotograma.
     * Si ya no se mantiene E sobre una máquina válida, se vacía la barra.
     *
     * @param agent Jugador activo.
     */
    @Override
    public synchronized void update(Agent agent) {
        if (!holding) {
            cancelHold();
        }
        holding = false;
    }

    @Override
    public synchronized void draw(Graphics graphics, Agent agent) {
        drawFloor(graphics);
        drawDecorations(graphics);
        drawObjectives(graphics, agent);
        drawMachineLabels(graphics, agent);
    }

    /**
     * Dibuja al agente entre las máquinas según su posición en el pasillo.
     *
     * @param graphics Contexto gráfico de Swing.
     * @param agent Jugador activo.
     * @param drawAgent Acción que dibuja al agente.
     */
    @Override
    public synchronized void drawScene(Graphics graphics, Agent agent, Runnable drawAgent) {
        drawFloor(graphics);
        drawDecorationsWithAgent(graphics, agent, drawAgent);
        drawObjectives(graphics, agent);
        drawMachineLabels(graphics, agent);
    }

    /**
     * Dibuja el fondo y los objetos disponibles en el suelo.
     *
     * @param graphics Contexto gráfico de Swing.
     */
    private void drawFloor(Graphics graphics) {
        graphics.drawImage(backgroundImg, 0, 0, 1280, 720, null);
        for (GameObject item : gameObjects) {
            if (!item.isCollected() && !shelfParts.contains(item)) {
                item.draw(graphics);
            }
        }
    }

    /**
     * Muestra arriba el nombre del nivel, las piezas instaladas y la pieza que
     * lleva el agente. Cada entrega suma una unidad, igual que en la zona 2.
     *
     * @param graphics Contexto gráfico de Swing.
     * @param agent Jugador activo.
     */
    private void drawObjectives(Graphics graphics, Agent agent) {
        int installedNuts = 0;
        int installedPipes = 0;
        for (int i = 0; i < boilers.length; i++) {
            installedNuts += installedParts[i];
        }
        for (int i = boilers.length; i < installedParts.length; i++) {
            installedPipes += installedParts[i];
        }
        Part part = agent.getCarriedPart();
        String carrying = part == null ? "nada" : part.getType();
        String text = "Tuercas: " + installedNuts + "/" + (boilers.length * partsPerMachine)
                + " | Tubos: " + installedPipes + "/" + (tubes.length * partsPerMachine)
                + " | Llevas: " + carrying;
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.BOLD, 16));
        graphics.drawString("ZONA 1 - Sala de Calderas", 20, 40);
        graphics.drawString(text, 640, 40);
        graphics.setFont(new Font("Arial", Font.BOLD, 13));
        if (isCompleted()) {
            graphics.setColor(Color.YELLOW);
            graphics.drawString("Sala reparada. Pulsa N para pasar al nivel 2.", 20, 61);
        } else {
            graphics.drawString("F recoger · Q soltar · Mantén E para instalar", 20, 61);
        }
    }

    /**
     * Dibuja el avance de cada máquina sobre su sprite. Al acercarse se
     * indica qué repuesto llevar o cuándo mantener E para instalarlo.
     *
     * @param graphics Contexto gráfico de Swing.
     * @param agent Jugador activo.
     */
    private void drawMachineLabels(Graphics graphics, Agent agent) {
        int nearby = getNearbyMachine(agent);
        Part part = agent.getCarriedPart();
        graphics.setFont(new Font("Arial", Font.BOLD, 13));
        FontMetrics metrics = graphics.getFontMetrics();

        for (int i = 0; i < machines.length; i++) {
            boolean boiler = i < boilers.length;
            Rectangle area = boiler ? boilers[i] : tubes[i - boilers.length];
            String name = boiler ? "CALDERA " + (i + 1) : "FUGA " + (i - boilers.length + 1);
            String title = name + "  " + installedParts[i] + "/" + partsPerMachine;
            int centerX = area.x + area.width / 2;
            graphics.setColor(Color.WHITE);
            graphics.drawString(title, centerX - metrics.stringWidth(title) / 2, area.y - 30);

            if (i == nearby) {
                String hint;
                if (installedParts[i] == partsPerMachine) {
                    hint = "Reparada";
                } else if (part != null && requiredType(i).equals(part.getType())) {
                    hint = "Mantén E para instalar";
                } else {
                    hint = boiler ? "Trae una tuerca" : "Trae un tubo";
                }
                graphics.setColor(Color.YELLOW);
                graphics.drawString(hint, centerX - metrics.stringWidth(hint) / 2, area.y - 14);
            }
            if (i == activeMachine) {
                drawProgressBar(graphics, area);
            }
        }
    }

    /**
     * Dibuja sobre la máquina la misma barra de instalación que usa el generador.
     *
     * @param graphics Contexto gráfico de Swing.
     * @param area Posición y tamaño de la máquina.
     */
    private void drawProgressBar(Graphics graphics, Rectangle area) {
        int barX = area.x + area.width / 2 - 50;
        int barY = area.y - 8;
        graphics.setColor(Color.DARK_GRAY);
        graphics.fillRect(barX, barY, 100, 8);
        graphics.setColor(Color.GREEN);
        graphics.fillRect(barX, barY, (int) (100 * getHoldProgress()), 8);
    }

    /** @return true cuando las tres calderas y las cuatro fugas tienen dos piezas. */
    @Override
    public synchronized boolean isCompleted() {
        for (int count : installedParts) {
            if (count < partsPerMachine) {
                return false;
            }
        }
        return true;
    }
}