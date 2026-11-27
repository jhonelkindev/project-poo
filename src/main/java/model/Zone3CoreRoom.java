package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Nivel 3, en esta clase se dibujan todos los powerites
 * el mapa y las maquinas, definen las mecanicas, el temporizador,
 * las mecanicas del villano, como ganar y perder.
 *
 * @author Mariana Ocampo
 * @version 1.0
 */
public class Zone3CoreRoom extends Zone {

    /**
     * Recursos del nivel, sprites, powerites
     */

    private static final String FRAMES = "/frames/Nivel-3/";
    private static final String ITEMS = "/frames/power-rites/";
    //colores de las barras
    private static final String[] ROD_COLORS = {"Roja", "Azul", "Amarilla"};
    private Rectangle core = new Rectangle(550, 350, 180, 180);
    /**
     * Dispensadores de donde se agarran las barras
     */
    private Rectangle[] dispensers = {
            new Rectangle(255, 290, 66, 88),
            new Rectangle(965, 290, 66, 88),
            new Rectangle(600, 560, 66, 88)
    };
    /**
     * Valvulas para estabilizar el reactor
     */
    private Rectangle[] valves = {
            new Rectangle(150, 420, 64, 64),
            new Rectangle(560, 200, 64, 64),
            new Rectangle(1080, 400, 64, 64)
    };
    /** Imagenes */
    /**
     * Fondo
     */
    private Image backgroundImg;
    /**
     * Nucleo
     */
    private Image[] coreImgs;
    /**
     * Sprites de los dispensadores cuando tienen las barras en ellas y vacias
     */
    private Image[] dispenserFullImgs;
    private Image dispenserEmptyImg;
    /**
     * Valvulas de refrigeracion
     */
    private Image valveClosedImg;
    private Image valveOpenImg;
    /**
     * Valvulas que tiene el jugador en la mano
     */
    private Image[] rodImgs;

    private Image coreAprovedImg;
    /**
     * Piezas del mapa que cambian
     */
    private Decoration coreDecoration;
    private Decoration[] dispenserDecorations = new Decoration[dispensers.length];
    private Decoration[] valveDecorations = new Decoration[valves.length];
    private boolean[] emptyDispensers = new boolean[dispensers.length];
    private boolean[] openValves = new boolean[valves.length];
    /**
     * Barras que carga el jugador
     */
    private boolean[] carriedRods = new boolean[dispensers.length];
    /**
     * Orden
     */
    private List<Integer> rodOrder = new ArrayList<>();
    private int insertedRods;
    private int openedValves;
    /**
     * Mecanismo de cerrar
     */
    private boolean locking;
    private int lockPresses;
    private String message = "";
    /**
     * Acciones con E
     */
    private long holdStart;
    private int holdDuration;
    private Rectangle holdArea;
    private boolean holding;
    private boolean wasHolding;


    /**
     * Constructor que crea las maquinas donde tienen que estar
     * junto con los dispensadores y las valvulas
     */
    public Zone3CoreRoom() {
        super("Zona 3: Nucleo del Reactor");
        int[] xPoints = {570, 1225, 650, 60};
        int[] yPoints = {160, 390, 690, 450};
        floorArea = new Polygon(xPoints, yPoints, 4);
        loadImages();
        placedMachines();
        placedValves();
        sortDecorationsByDepth();
        chooseRodOrder();

    }

    private void loadImages() {
        backgroundImg = loadImage("/mapas_niveles/nivel3_fondo.png");
        coreImgs = new Image[]{
                loadImage(FRAMES + "Nucleo/nucleo_vacio.png"),
                loadImage(FRAMES + "Nucleo/nucleo_barra1.png"),
                loadImage(FRAMES + "Nucleo/nucleo_barras2.png"),
                loadImage(FRAMES + "Nucleo/nucleo_barras3.png"),
        };
        dispenserFullImgs = new Image[]{
                loadImage(FRAMES + "dispensadores/dispensador_rojo_lleno.png"),
                loadImage(FRAMES + "dispensadores/dispensador_azul_lleno.png"),
                loadImage(FRAMES + "dispensadores/dispensador_amarillo_lleno.png")

        };
        dispenserEmptyImg = loadImage(FRAMES + "dispensadores/dispensador_vacio.png");
        valveClosedImg = loadImage(FRAMES + "/valvula_refrigeracion/valvula_refrigeracion_cerrada.png");
        valveOpenImg = loadImage(FRAMES + "/valvula_refrigeracion/valvula_refrigeracion_abierta.png");

        rodImgs = new Image[]{
                loadImage(ITEMS + "item_barra_roja.png"),
                loadImage(ITEMS + "item_barra_azul.png"),
                loadImage(ITEMS + "item_barra_amarilla.png")
        };

        coreAprovedImg = loadImage(FRAMES + "Nucleo/nucleo_aprobado.png");
    }

    /**
     * ubica las bases de los dispensadores y el nucleo para que existan las colisiones
     */
    private void placedMachines() {
        Polygon coreBase = new Polygon(
                new int[]{30, 85, 152, 97},
                new int[]{135, 170, 138, 103}, 4);


        Polygon dispenserBase = new Polygon(
                new int[]{4, 38, 61, 27},
                new int[]{70, 86, 72, 56}, 4);
        coreDecoration = new Decoration(coreImgs[0], core, coreBase);
        addDecoration(coreDecoration);

        for (int i = 0; i < dispensers.length; i++) {
            dispenserDecorations[i] = new Decoration(dispenserFullImgs[i], dispensers[i], dispenserBase);
            addDecoration(dispenserDecorations[i]);
        }

    }

    /**
     * Ubica las tres valvulas de refrigeracion
     */
    private void placedValves() {
        Polygon valveBase = new Polygon(
                new int[]{12, 32, 52, 32},
                new int[]{52, 62, 52, 42}, 4);


        for (int i = 0; i < valves.length; i++) {
            valveDecorations[i] = new Decoration(valveClosedImg, valves[i], valveBase);
            addDecoration(valveDecorations[i]);
        }
    }

    /**
     * Determina en que orden se ponen las barras de colores
     */
    private void chooseRodOrder() {
        for (int i = 0; i < ROD_COLORS.length; i++) {
            rodOrder.add(i);
        }
        Collections.shuffle(rodOrder);
    }

    /**
     * Accion de la tecla E: quitar las barras, las inserta en el nucleo y abre las valvulas
     *
     * @param agent Jugador activo
     */
    @Override
    public void action(Agent agent) {
        boolean newPress = !wasHolding;
        //meter seguro en el nucleo, e tres veces despues de meter la barra
        if (locking) {
            if (newPress && isNear(agent, core)) {
                lockPresses++;
                if (lockPresses == 3) {
                    finishInsertion();
                }

            }
            holding = true;
            return;
        }
        if (isNear(agent, core) && hasRodOfTurn()) {
            if (holdE(1550, core)) {
                locking = true;
                lockPresses = 0;
                holdStart = 0;
            }
            return;
        }
        if (isNear(agent, core) && countCarriedRods() > 0) {
            holding = true;
            message = "Esa barra no es la correcta";
            return;
        }

        //En un dispensador lleno: mantener E 1.5 s para quitar las barra
        for (int i = 0; i < dispensers.length; i++) {
            if (!emptyDispensers[i] && isNear(agent, dispensers[i])) {
                if (holdE(1500, dispensers[i])) {
                    extractRod(i);
                }
                return;
            }
        }
        //en un valvula cerrada e 2s para abrirla
        for (int i = 0; i < valves.length; i++) {
            if (!openValves[i] && isNear(agent, valves[i])) {
                if (holdE(2000, valves[i])) {
                    openValve(i);
                }
                return;
            }

        }
        /**
         * Saca la barra y la agrega a lo que lleva el jugador
         * @param index Posicion del dispensador
         *
         */


    }

    private void extractRod(int index) {
        emptyDispensers[index] = true;
        carriedRods[index] = true;
        dispenserDecorations[index].setImage(dispenserEmptyImg);
        holdStart = 0;
        message = "";

    }

    /**
     * Termina de poner la barra despues de poner el seguro
     *
     */
    private void finishInsertion() {
        carriedRods[rodOrder.get(insertedRods)] = false;
        insertedRods++;
        coreDecoration.setImage(coreImgs[insertedRods]);
        locking = false;
        message = "";
    }

    /**
     * Abre una valvula de refrigeracion de forma permanente
     *
     * @param index posicion de la valvula
     */
    private void openValve(int index) {
        openValves[index] = true;
        openedValves++;
        valveDecorations[index].setImage(valveOpenImg);
        holdStart = 0;
        isCompleted();
    }

    /**
     * indica si el jugador lleva la barra del color que es al nucleo acerta en el turno
     *
     * @return true si lleva la barra correcta
     */
    private boolean hasRodOfTurn() {
        return insertedRods < rodOrder.size() && carriedRods[rodOrder.get(insertedRods)];
    }

    /**
     * cuenta cuantas barras tienen
     */
    private int countCarriedRods() {
        int count = 0;
        for (boolean rod : carriedRods) {
            if (rod) {
                count++;
            }
        }
        return count;
    }

    /**
     * Cuenta el tiempo que el juegador presiona la E
     *
     * @param millis tiempo que se debe mantener la E
     * @param area   maquina que se esta usando
     * @return true cuando ya se tuvo la E el tiempo necesario
     */
    private boolean holdE(int millis, Rectangle area) {
        holding = true;
        holdDuration = millis;
        holdArea = area;
        if (holdStart == 0) {
            holdStart = System.currentTimeMillis();

        }
        return System.currentTimeMillis() - holdStart >= millis;
    }

    /**
     * Comprueba si el jugador esta cerca de un area del nucleo (mapa)
     *
     * @param agent jugador
     * @param area  Area de la maquina
     * @return true si el jugador esta cerca
     */
    private boolean isNear(Agent agent, Rectangle area) {
        Rectangle playerArea = new Rectangle(agent.getX() - 20, agent.getY() - 20,
                agent.getWidth() + 40, agent.getHeight() + 40);
        return playerArea.intersects(area);

    }

    private void levelCompleted() {
        if (isCompleted()) {
            coreDecoration.setImage(coreAprovedImg);
            message = "";
        }
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
        if (!holding) {
            holdStart = 0;
        }
        wasHolding = holding;
        holding = false;
    }

    /**
     * Dibuja todos los items del nivel 3
     *
     * @param graphics Contexto grafico de Swing.
     * @param agent    Jugador activo.
     */
    @Override
    public void draw(Graphics graphics, Agent agent) {
        drawBackground(graphics);
        drawDecorations(graphics);
        drawObjectives(graphics);
        drawZoneName(graphics);
    }

    @Override
    public void drawScene(Graphics graphics, Agent agent, Runnable drawAgent) {
        drawBackground(graphics);
        drawDecorationsWithAgent(graphics, agent, drawAgent);
        drawCarriedRods(graphics, agent);
        drawProgress(graphics);
        drawZoneName(graphics);
        drawObjectives(graphics);
    }

    /**
     * Dibuja el fondo del nivel
     *
     * @param graphics pincel
     */
    private void drawBackground(Graphics graphics) {
        graphics.drawImage(backgroundImg, 0, 0, 1280, 720, null);
    }

    /**
     * Dibuja las barras que lleva el jugador
     *
     * @param graphics pincel
     * @param agent    jugador
     */
    private void drawCarriedRods(Graphics graphics, Agent agent) {
        int x = agent.getX();
        for (int i = 0; i < carriedRods.length; i++) {
            if (carriedRods[i]) {
                graphics.drawImage(rodImgs[i], x, agent.getY() - 28, 20, 20, null);
                x += 16;
            }
        }
    }

    /**
     * Dibuja la barra de proceso
     *
     * @param graphics pincel
     */
    private void drawProgress(Graphics graphics) {
        if (locking) {
            graphics.setColor(Color.YELLOW);
            graphics.setFont(new Font("Arial", Font.BOLD, 14));
            graphics.drawString("Presiona E para cerrar el seguro" + lockPresses + "/3",
                    core.x, core.y - 10);


        } else if (holdStart != 0 && holdArea != null) {
            double progress = (double) (System.currentTimeMillis() - holdStart) / holdDuration;
            if (progress > 1) {
                progress = 1;
            }
            int barX = holdArea.x + holdArea.width / 2 - 50;
            int barY = holdArea.y - 8;

            graphics.setColor(Color.DARK_GRAY);
            graphics.fillRect(barX, barY, 100, 8);
            graphics.setColor(Color.GREEN);
            graphics.fillRect(barX, barY, (int) (100 * progress), 8);

        }
    }

    /**
     *
     * Dibuja el nombre de la zona
     *
     * @param graphics pincel
     */
    private void drawZoneName(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.BOLD, 16));
        graphics.drawString("ZONA 3- Núcleo del Reactor", 20, 40);
    }

    /**
     * Muestra las barras instaladas
     *
     * @param graphics pincel
     */
    private void drawObjectives(Graphics graphics) {
        String order = "";
        for (int i = 0; i < rodOrder.size(); i++) {
            order += ROD_COLORS[rodOrder.get(i)];
            if (i < rodOrder.size() - 1) {
                order += " -> ";
            }
        }
        String text = "Barras: " + insertedRods + "/3"
                + " | Orden: " + order
                + " | Valvulas: " + openedValves + "/3"
                + " | Cargando: " + countCarriedRods();
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.BOLD, 16));
        graphics.drawString(text, 380, 40);
        if (!message.isEmpty()) {
            graphics.setColor(Color.RED);
            graphics.drawString(message, 380, 62);
        }
    }


    /**
     * Indica si la zona fue completada
     *
     * @return true si el nucleo tiene 3 barras
     */
    @Override
    public boolean isCompleted() {
        return insertedRods == dispensers.length && openedValves == dispensers.length;
    }
}
