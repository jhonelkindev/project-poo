package model;

import java.awt.*;

/**
 * Clase abstracta que representa un objeto interactivo dentro del juego
 * Define la posicion, área de colision y estado de recoleccion
 */
public abstract class GameObject {
    protected int x;
    protected int y;
    protected int widht;
    protected int height;
    protected boolean collected;

    /**
     * Construye un objeto del juego con una posicion y tamaño determinados
     *
     * @param x      Coordenada horizontal en pixeles
     * @param y      Coordenada vertical en pixeles
     * @param widht  Ancho del objeto en píxeles
     * @param height Alto del objeto en pixeles
     */
    public GameObject(int x, int y, int widht, int height, boolean collected) {
        this.x = x;
        this.y = y;
        this.widht = widht;
        this.height = height;
        this.collected = false;
    }

    /**
     * Obtiene el rectangulo de colision del objeto para detectar contacto con el jugador
     *
     * @return Rectangulo con la posicion y dimensiones actuales del proyecto
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, widht, height);
    }

    /**
     * Aplica el efecto sobre el agente cuando recoge el objeto
     *
     * @param agent jugador que interactua con el objeto
     */
    public abstract void applyEffect(Agent agent);


    /**
     * Dibuja la representacion visual del objeto en el lienzo del juego
     *
     * @param graphics Contexto gráfico de Swing utilizado para dibujar
     */
    public abstract void draw(Graphics graphics);

    /**
     * Obtiene la coordenada X del objeto
     *
     * @return Posicion horizontal en pixeles
     */
    public int getX() {
        return x;
    }

    /**
     * Obtiene la coordenada Y del objeto
     *
     * @return Posicion vertical en pixeles
     */
    public int getY() {
        return y;
    }

    /**
     * Obtiene el ancho del objeto
     *
     * @return ancho en pixeles
     */
    public int getWidht() {
        return widht;
    }

    /**
     * Obtiene el alto del objeto
     *
     * @return alto en pixeles
     */
    public int getHeight() {
        return height;
    }

    /**
     * Indica si el objeto ya fue recogido por el jugador
     *
     * @return true si ya fue recogido, false en caso contrario
     */
    public boolean isCollected() {
        return collected;
    }

    /**
     * Actualiza el estado de recoleccion del objeto;
     *
     * @param collected Nuevo estado de recoleccion
     */
    public void setCollected(boolean collected) {
        this.collected = collected;
    }
}
