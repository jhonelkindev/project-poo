package model;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;

/**
 * Representa el primer nivel del juego, la sala de calderas.
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
    private Image tubeImg;

    /**
     * Construye la sala de calderas y ubica sus maquinas y decoraciones.
     */
    public Zone1BoilerRoom() {
        super("Zona 1: Sala de Calderas");

        int[] xPoints = {565, 1238, 715, 36};
        int[] yPoints = {128, 369, 675, 433};
        floorArea = new Polygon(xPoints, yPoints, 4);

        backgroundImg = new ImageIcon(getClass().getResource(
                "/mapas_niveles/nivel1_fondo.png")).getImage();

        tubeImg = new ImageIcon(getClass().getResource(
                "/Sprites/Nivel-1/Tubos/tuberia_dañada.gif")).getImage();

        placeMachines();
        placeTanks();
        placeFloorPipes();
        sortDecorationsByDepth();
    }

    /**
     * Ubica la estanteria, las calderas y los extractores con sus bases.
     */
    private void placeMachines() {
        Image rackImg = new ImageIcon(getClass().getResource(
                "/frames/Nivel-1/Diseños/Estanteria/estanteria_repuestos.png")).getImage();

        Image boilerImg = new ImageIcon(getClass().getResource(
                "/Sprites/Nivel-1/Maquina caldera/caldera_apagada.gif")).getImage();

        Image exhaustFanImg = new ImageIcon(getClass().getResource(
                "/Sprites/Nivel-1/Extractor/extractor_activo.gif")).getImage();

        Polygon rackBase = new Polygon(
                new int[]{24, 77, 103, 50},
                new int[]{93, 111, 96, 77}, 4);

        Polygon boilerBase = new Polygon(
                new int[]{22, 32, 63, 84, 103, 87, 56, 33},
                new int[]{94, 106, 110, 108, 88, 81, 79, 84}, 8);

        Polygon fanBase = new Polygon(
                new int[]{16, 60, 81, 38},
                new int[]{68, 84, 71, 55}, 4);

        addDecoration(new Decoration(rackImg, rack, rackBase));

        for (Rectangle boiler : boilers) {
            addDecoration(new Decoration(boilerImg, boiler, boilerBase));
        }

        for (Rectangle fan : exhaustFans) {
            addDecoration(new Decoration(exhaustFanImg, fan, fanBase));
        }
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
     * Dibuja al jugador a la profundidad que le corresponde en el pasillo.
     *
     * @param graphics Contexto grafico de Swing.
     * @param agent Jugador activo.
     * @param drawAgent Accion que dibuja al jugador.
     */
    @Override
    public void drawScene(Graphics graphics, Agent agent, Runnable drawAgent) {
        drawBackgroundAndWalls(graphics);
        drawDecorationsWithAgent(graphics, agent, drawAgent);
        drawZoneName(graphics);
    }

    /**
     * Dibuja los cuatro tubos elevados sobre las paredes del fondo.
     *
     * @param graphics Contexto grafico de Swing.
     */
    private void drawBackgroundAndWalls(Graphics graphics) {
        graphics.drawImage(backgroundImg, 0, 0, 1280, 720, null);

        for (int i = 0; i < tubes.length; i++) {
            Rectangle tube = tubes[i];

            if (i < 2) {
                // Los tubos izquierdos se dibujan en espejo sobre la pared.
                graphics.drawImage(tubeImg, tube.x + tube.width, tube.y,
                        -tube.width, tube.height, null);
            } else {
                graphics.drawImage(tubeImg, tube.x, tube.y,
                        tube.width, tube.height, null);
            }
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
        graphics.drawString("ZONA 1 - N para pasar a Zona 2", 20, 40);
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}