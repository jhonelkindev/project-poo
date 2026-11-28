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
    private static final String[] ROD_FILES = {"roja", "azul", "amarilla"};
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
     * DrUranio
     */
    private DrUranio doctor = new DrUranio();
    private List<Enemy> enemies = new ArrayList<>();
    private Rectangle[] consoles = {
            new Rectangle(290, 400, 64, 64),
            new Rectangle(980, 390, 64, 64),
    };
    private boolean[] usedConsoles = new boolean[consoles.length];
    private Decoration[] consoleDecorations = new Decoration[consoles.length];
    private Image consoleOnImg;
    private Image consoleOffImg;
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
    private int carriedRod = -1;

    /**
     * adornos para el nivel
     */
    private Image pipesImg;
    private Image condensersImg;
    private Image barrierImg;
    private Image platformImg;
    private Image wasteImg;
    private Image beaconImg;

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
        enemies.add(doctor);
        placeDecorations();
        placeConsoles();

    }

    private void loadImages() {
        backgroundImg = loadImage("/mapas_niveles/nivel3_fondo.png");
        consoleOnImg = loadImage(FRAMES + "consola/consola_prendida.png");
        consoleOffImg = loadImage(FRAMES + "consola/consola_apagada.png");
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
        coreAprovedImg = loadImage(FRAMES + "Nucleo/nucleo_aprobado.png");

        rodImgs = new Image[]{
                loadImage(ITEMS + "item_barra_roja.png"),
                loadImage(ITEMS + "item_barra_azul.png"),
                loadImage(ITEMS + "item_barra_amarilla.png")
        };
        pipesImg = loadImage(FRAMES + "decoraciones/decoracion_tuberias_refrigeracion.png");
        condensersImg = loadImage(FRAMES + "decoraciones/decoracion_condensadores.png");
        barrierImg = loadImage(FRAMES + "decoraciones/decoracion_barrera_radiologica.png");
        platformImg = loadImage(FRAMES + "decoraciones/decoracion_plataforma_mantenimiento.png");
        wasteImg = loadImage(FRAMES + "decoraciones/decoracion_residuos_radiactivos.png");
        beaconImg = loadImage(FRAMES + "decoraciones/decoracion_baliza_emergencia.png");

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
     * Ubica las consolas
     */
    private void placeConsoles() {
        Polygon consolesBase = new Polygon(
                new int[]{6, 32, 58, 32},
                new int[]{46, 60, 46, 32}, 4
        );
        for (int i = 0; i < consoles.length; i++) {
            consoleDecorations[i] = new Decoration(consoleOnImg, consoles[i], consolesBase);
            addDecoration(consoleDecorations[i]);
        }
    }

    /**
     * Obtiene al DrUranio para que los hilos los muevan
     *
     * @return lista con el DrUranio
     */
    public List<Enemy> getEnemies() {
        return enemies;
    }
    /**
     * Ubica las decoraciones en el nucleo
     *
     */
    /**
     * Ubica las decoraciones del cuarto del reactor
     */
    private void placeDecorations() {
        Polygon boxBase = new Polygon(
                new int[]{6, 32, 58, 32},
                new int[]{46, 60, 46, 32}, 4);
        Polygon platformBase = new Polygon(
                new int[]{8, 48, 88, 48},
                new int[]{42, 62, 42, 24}, 4);
        Polygon wasteBase = new Polygon(
                new int[]{9, 24, 39, 24},
                new int[]{38, 46, 38, 30}, 4);

        // Pared izquierda (arriba): tuberia + condensador
        addDecoration(new Decoration(pipesImg, new Rectangle(450, 200, 64, 64), boxBase));
        addDecoration(new Decoration(condensersImg, new Rectangle(395, 230, 64, 64), boxBase));

        // Pared derecha (arriba): condensador + tuberia
        addDecoration(new Decoration(condensersImg, new Rectangle(740, 195, 64, 64), boxBase));
        addDecoration(new Decoration(pipesImg, new Rectangle(800, 216, 64, 64), boxBase));

        // Rincon de abajo a la izquierda: deposito de residuos
        addDecoration(new Decoration(wasteImg, new Rectangle(170, 470, 48, 48), wasteBase));
        addDecoration(new Decoration(wasteImg, new Rectangle(215, 490, 48, 48), wasteBase));
        addDecoration(new Decoration(wasteImg, new Rectangle(180, 505, 48, 48), wasteBase));
        addDecoration(new Decoration(barrierImg, new Rectangle(250, 500, 64, 64), boxBase));
        addDecoration(new Decoration(beaconImg, new Rectangle(140, 450, 32, 48)));

        // Rincon de abajo a la derecha: mantenimiento
        addDecoration(new Decoration(platformImg, new Rectangle(930, 500, 96, 64), platformBase));
        addDecoration(new Decoration(condensersImg, new Rectangle(1030, 480, 64, 64), boxBase));
        addDecoration(new Decoration(beaconImg, new Rectangle(900, 480, 32, 48)));

        // Barreras a los lados del dispensador de abajo
        addDecoration(new Decoration(barrierImg, new Rectangle(500, 580, 64, 64), boxBase));
        addDecoration(new Decoration(barrierImg, new Rectangle(720, 590, 64, 64), boxBase));
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
        if (isNear(agent, core) && carriedRod != -1) {
            holding = true;
            message = "Esa barra no es la correcta, devuelvela a su dispensador con Q";
            return;
        }

        //En un dispensador lleno: mantener E 1.5 s para quitar las barra
        for (int i = 0; i < dispensers.length; i++) {
            if (!emptyDispensers[i] && isNear(agent, dispensers[i])) {
                if (carriedRod != -1) {
                    holding = true;
                    message = "Solo puedes llevar una barra a la vez";
                    return;
                }
                if (holdE(1500, dispensers[i])) {
                    extractRod(i);
                }
                return;
            }
        }
        for (int i = 0; i < consoles.length; i++) {
            if (!usedConsoles[i] && isNear(agent, consoles[i])) {
                if (holdE(1000, consoles[i])) {
                    useConsole(i);
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


    }

    /**
     * Usa una consola, esta aturde al DrUranio 5 segundos y se apaga
     *
     * @param index posicion de la consola
     */
    private void useConsole(int index) {
        usedConsoles[index] = true;
        consoleDecorations[index].setImage(consoleOffImg);
        doctor.stun(5000);
        holdStart = 0;
    }

    /**
     * Saca la barra y la agrega a lo que lleva el jugador
     *
     * @param index Posicion del dispensador
     *
     */
    private void extractRod(int index) {
        emptyDispensers[index] = true;
        dispenserDecorations[index].setImage(dispenserEmptyImg);
        holdStart = 0;
        carriedRod = index;
        message = "";

    }

    /**
     * Termina de poner la barra despues de poner el seguro
     *
     */
    private void finishInsertion() {
        insertedRods++;
        carriedRod = -1;
        if (insertedRods == 2) {
            doctor.enrage();
        }
        coreDecoration.setImage(loadCoreImage());
        locking = false;
        message = "";
        checkCompleted();
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
        checkCompleted();
    }

    /**
     * si ya se completo el objetivo del nivel
     */
    private void checkCompleted() {
        if (isCompleted()) {
            coreDecoration.setImage(coreAprovedImg);
            message = "";
        }
    }

    /**
     * indica si el jugador lleva la barra del color que es al nucleo acerta en el turno
     *
     * @return true si lleva la barra correcta
     */
    private boolean hasRodOfTurn() {
        return insertedRods < rodOrder.size() && carriedRod == rodOrder.get(insertedRods);
    }

    /**
     * Devuelve la barra con Q para que el jugador la lleve de vuelta a su dispensador si
     * se equivocó de color
     *
     * @param agent jugador
     */
    @Override
    public void drop(Agent agent) {
        if (carriedRod != -1 && isNear(agent, dispensers[carriedRod])) {
            emptyDispensers[carriedRod] = false;
            dispenserDecorations[carriedRod].setImage(dispenserFullImgs[carriedRod]);
            carriedRod = -1;
            message = "";
        }
    }

    /**
     * Busca la imagen del nucleo en los colores que corresponden
     *
     * @return Imagen del nucleo con las barras insertadas
     */
    private Image loadCoreImage() {
        String name = "nucleo";
        for (int i = 0; i < insertedRods; i++) {
            name += "_" + ROD_FILES[rodOrder.get(i)];
        }
        String path = FRAMES + "Nucleo/Barras/" + name + ".png";
        if (getClass().getResource(path) != null) {
            return loadImage(path);
        }
        return coreImgs[insertedRods];
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
    public boolean isNear(Agent agent, Rectangle area) {
        Rectangle playerArea = new Rectangle(agent.getX() - 20, agent.getY() - 20,
                agent.getWidth() + 40, agent.getHeight() + 40);
        return playerArea.intersects(area);

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
        doctor.draw(graphics);
        drawLives(graphics, agent);
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
     * Dibuja la barra que lleva el jugador
     *
     * @param graphics pincel
     * @param agent    jugador
     */
    private void drawCarriedRods(Graphics graphics, Agent agent) {
        if (carriedRod != -1) {
            graphics.drawImage(rodImgs[carriedRod], agent.getX() + 14, agent.getY() - 28, 20, 20, null);
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
     * Muestra las vidas del usuario
     *
     * @param graphics pincel
     * @param agent    jugador
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
                + " | Cargando: " + (carriedRod == -1 ? "nada" : ROD_COLORS[carriedRod]);
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.BOLD, 16));
        graphics.drawString(text, 380, 40);
        if (!message.isEmpty()) {
            graphics.setColor(Color.RED);
            graphics.drawString(message, 380, 62);
        }
        if (isCompleted()) {
            graphics.setColor(Color.GREEN);
            graphics.setFont(new Font("Arial", Font.BOLD, 32));
            graphics.drawString("REACTOR ESTABILIZADO", 460, 120);
        }
    }


    /**
     * Indica si la zona fue completada
     *
     * @return true si el nucleo tiene 3 barras
     */
    @Override
    public boolean isCompleted() {
        return insertedRods == dispensers.length && openedValves == valves.length;
    }
}
