package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase abstracta que representa un nivel del juego
 */
public abstract class Zone {
    protected String zoneName;
    protected Polygon floorArea;
    protected List<GameObject> gameObjects;

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
     * Actualiza la logiva y interaccion de la zona en cada fotograma
     *
     * @param agent jugador activo dentro de la zona
     */
    public abstract void update(Agent agent);

    /**
     * Dibuja el piso, las maquinas y los objetos de la zona
     *
     * @param graphics Contexto grapico de de Swing utilizado para dibujar
     * @param agent    Jugador
     */
    public abstract void draw(Graphics graphics, Agent agent);

    /**
     * Indica si se cumplieron todos los objetivos del nivel
     *
     * @return true si la zona esta completa, false en caso contrario
     */
    public abstract boolean isCompleted();

    /**
     * Obtiene el poligono que delimita la zona transitable del mapa
     *
     * @return Poligono con los bordes del escenario
     */
    public Polygon getFloorArea() {
        return floorArea;
    }

    /**
     * Obtiene la lista de objetos recolectables presentes en la zona
     *
     * @return Lista de objetos del nivel
     */
    public List<GameObject> gameObjects() {
        return gameObjects;
    }

    /**
     * Obtiene el nombre de la zona actual
     *
     * @return Nombre de la zona
     */
    public String getZoneName() {
        return zoneName;
    }

}
