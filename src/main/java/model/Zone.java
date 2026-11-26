package model;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Clase abstracta que representa un nivel del juego
 */
public abstract class Zone {
    protected String zoneName;
    protected Polygon floorArea;
    protected List<GameObject> gameObjects;

    private List<Decoration> decorations = new ArrayList<>();
    private CollisionMap collisionMap = new CollisionMap();

    /**
     * Construye una zona jugable con el nombre y el area
     *
     * @param zoneName nombre identificador de la zona
     */
    public Zone(String zoneName) {
        this.zoneName = zoneName;
        this.gameObjects = new ArrayList<>();

        int[] xPoints = {640, 1180, 640, 100};
        int[] yPoints = {100, 400, 700, 400};
        this.floorArea = new Polygon(xPoints, yPoints, 4);
    }

    /**
     * Actualiza la logica y la interaccion de la zona en cada fotograma.
     *
     * @param agent Jugador activo dentro de la zona.
     */
    public abstract void update(Agent agent);

    /**
     * Dibuja el piso, las maquinas y los objetos de la zona.
     *
     * @param graphics Contexto grafico de Swing.
     * @param agent    Jugador activo.
     */
    public abstract void draw(Graphics graphics, Agent agent);

    /**
     * Indica si se cumplieron todos los objetivos del nivel.
     *
     * @return true si la zona esta completa, false en caso contrario.
     */
    public abstract boolean isCompleted();

    /**
     * Obtiene el poligono que delimita la zona transitable del mapa.
     *
     * @return Poligono con los bordes del escenario.
     */
    public Polygon getFloorArea() {
        return floorArea;
    }

    /**
     * Obtiene la lista de objetos recolectables presentes en la zona.
     *
     * @return Lista de objetos del nivel.
     */
    public List<GameObject> gameObjects() {
        return gameObjects;
    }

    /**
     * Obtiene el nombre de la zona actual.
     *
     * @return Nombre de la zona.
     */
    public String getZoneName() {
        return zoneName;
    }

    /**
     * Carga una imagen de la carpeta de recursos.
     *
     * @param path Ruta de la imagen dentro de resources.
     * @return Imagen cargada.
     */
    protected Image loadImage(String path) {
        return new ImageIcon(getClass().getResource(path)).getImage();
    }

    /**
     * Agrega la decoracion y registra su base cuando ocupa espacio en el piso.
     *
     * @param decoration Decoracion que se ubica en el mapa.
     */
    protected void addDecoration(Decoration decoration) {
        decorations.add(decoration);

        if (decoration.getBase() != null) {
            addObstacle(decoration.getBase());
        }
    }

    /**
     * Ordena las piezas fijas del fondo hacia el frente despues de ubicarlas.
     */
    protected void sortDecorationsByDepth() {
        // Las piezas con una base mas arriba en la pantalla se dibujan primero.
        decorations.sort(Comparator.comparingInt(Decoration::getDepth));
    }

    /**
     * Dibuja las decoraciones de la zona en su orden actual.
     *
     * @param graphics Contexto grafico utilizado para dibujar.
     */
    protected void drawDecorations(Graphics graphics) {
        for (Decoration decoration : decorations) {
            decoration.draw(graphics);
        }
    }

    /**
     * Dibuja al jugador entre las piezas ordenadas del fondo hacia el frente.
     *
     * @param graphics  Contexto grafico de Swing.
     * @param agent     Jugador activo.
     * @param drawAgent Accion que dibuja al jugador.
     */
    protected void drawDecorationsWithAgent(Graphics graphics, Agent agent, Runnable drawAgent) {
        Rectangle sprite = new Rectangle(
                agent.getX(), agent.getY(), agent.getWidth(), agent.getHeight());

        Rectangle feet = agent.getBounds();
        boolean agentDrawn = false;

        for (Decoration decoration : decorations) {
            if (!agentDrawn && decoration.isInFrontOf(sprite, feet)) {
                drawAgent.run();
                agentDrawn = true;
            }

            decoration.draw(graphics);
        }

        if (!agentDrawn) {
            drawAgent.run();
        }
    }

    /**
     * Agrega una figura que bloquea el paso dentro de la zona.
     *
     * @param obstacle Base que ocupa el obstaculo sobre el piso.
     */
    protected void addObstacle(Shape obstacle) {
        collisionMap.addObstacle(obstacle);
    }

    /**
     * Comprueba si los pies pueden ocupar la posicion solicitada.
     *
     * @param feet Rectangulo de los pies en la posicion propuesta.
     * @return true si el apoyo esta en el piso y los pies quedan libres.
     */
    public boolean canMoveTo(Rectangle feet) {
        return isOnFloor(feet) && !collisionMap.intersectsObstacle(feet);
    }

    /**
     * Comprueba el apoyo del jugador con el centro inferior de los pies.
     *
     * @param feet Rectangulo de los pies en la posicion propuesta.
     * @return true si el punto de apoyo esta dentro del piso de la zona.
     */
    protected boolean isOnFloor(Rectangle feet) {
        return floorArea.contains(feet.getCenterX(), feet.getMaxY());
    }

    /**
     * Dibuja la zona y el jugador en el orden del nivel.
     *
     * @param graphics  Contexto grafico de Swing.
     * @param agent     Jugador activo.
     * @param drawAgent Accion que dibuja al jugador.
     */
    public void drawScene(Graphics graphics, Agent agent, Runnable drawAgent) {
        draw(graphics, agent);
        drawAgent.run();
    }


    /**
     * Se ejecuta cuando el jugador presiona F para recoger un objeto.
     * Cada zona decide que se puede recoger.
     *
     * @param agent Jugador activo.
     */
    public void pickUp(Agent agent) {
    }

    /**
     * Se ejecuta cuando el jugador presiona Q para soltar un objeto.
     *
     * @param agent Jugador activo.
     */
    public void drop(Agent agent) {
    }

    /**
     * Se ejecuta en cada fotograma mientras el jugador mantiene presionada la E.
     *
     * @param agent Jugador activo.
     */
    public void action(Agent agent) {
    }
}