package model;

import java.awt.*;

/**
 * Clase abstracta para los enemigos del juego
 */
public abstract class Enemy extends Entity {
    protected boolean alive;

    /**
     * Crea un enemigo en una posicion del mapa
     *
     * @param x      coodenada horzontal en pixeles
     * @param y      coordenada vertical en pixeles
     * @param width  ancho en pixeles
     * @param height alto en pixeles
     * @param speed  velocidad
     */
    public Enemy(double x, double y, int width, int height, double speed) {
        super(x, y, width, height, speed);
        this.alive = true;
    }

    /**
     * Mueve el enemigo segun la posicion del jugador
     *
     * @param agent Jugador
     * @param zone  Zona donde se mueve el enemigo
     */
    public abstract void move(Agent agent, Zone zone);


    /**
     * Dibuja al enemigo
     *
     * @param graphics Contexto grafco de Swing
     */
    public abstract void draw(Graphics graphics);

    /**
     * indica si el enemigo puede hacer daño en ese momento
     *
     * @return true si puede quitar vidas
     */
    public abstract boolean isDangerous();


    /**
     * Reacciona cuando el jugador golpea al enemigo
     */
    public abstract void hit();

    /**
     * Comprueba si el enemigo esta tocando al jugador
     *
     * @param agent Jugador
     * @return true si se tocan
     */
    public boolean touches(Agent agent) {
        return getBounds().intersects(agent.getBounds());
    }

    /**
     * Indica si el enemigo sigue vivo
     *
     * @return true si esta vivo
     */
    public boolean isAlive() {
        return alive;
    }
}