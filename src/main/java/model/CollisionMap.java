package model;

import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

/**
 * Guarda los obstaculos de un nivel y comprueba el contacto con ellos.
 */
public class CollisionMap {
    private List<Shape> obstacles = new ArrayList<>();

    /**
     * Agrega la base de un obstaculo al mapa.
     *
     * @param obstacle Figura que ocupa el obstaculo sobre el piso.
     */
    public void addObstacle(Shape obstacle) {
        obstacles.add(obstacle);
    }

    /**
     * Comprueba si un area toca alguno de los obstaculos del nivel.
     *
     * @param area Rectangulo que se quiere comprobar.
     * @return true si el area toca un obstaculo.
     */
    public boolean intersectsObstacle(Rectangle area) {
        for (Shape obstacle : obstacles) {
            if (obstacle.intersects(area)) {
                return true;
            }
        }

        return false;
    }
}