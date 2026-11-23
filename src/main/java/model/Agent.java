package model;

import javax.swing.*;
import java.awt.*;

/**
 * Representa al jugador.
 * Gestiona su posicion, movimiento, vidas y puntaje
 * y el cambio de sprite segun la direccion en la que se mueve.
 */
public class Agent extends Entity {
    private String playerName;
    private int lives;
    private int score;

    private Image idleSprite;
    private Image walkDown;
    private Image walkUp;
    private Image walkLeft;
    private Image walkRight;
    private Image walkUpLeft;
    private Image walkUpRight;
    private Image currentSprite;

    /**
     * Crea al agente en la posicion inicial con 3 vidas y carga sus sprites.
     *
     * @param x Coordenada horizontal inicial en pixeles.
     * @param y Coordenada vertical inicial en pixeles.
     */
    public Agent(double x, double y) {
        super(x, y, 48, 64, 1.4);

        this.playerName = "Agente";
        this.lives = 3;
        this.score = 0;

        this.idleSprite = new ImageIcon(getClass().getResource(
                "/frames/Personaje Principal/Personaje_Principal.png")).getImage();

        this.walkDown = new ImageIcon(getClass().getResource(
                "/Sprites/Personaje/preview_abajo.gif")).getImage();

        this.walkUp = new ImageIcon(getClass().getResource(
                "/Sprites/Personaje/preview_arriba.gif")).getImage();

        this.walkLeft = new ImageIcon(getClass().getResource(
                "/Sprites/Personaje/preview_izquierda.gif")).getImage();

        this.walkRight = new ImageIcon(getClass().getResource(
                "/Sprites/Personaje/preview_derecha.gif")).getImage();

        this.walkUpLeft = new ImageIcon(getClass().getResource(
                "/Sprites/Personaje/preview_arriba_izquierda.gif")).getImage();

        this.walkUpRight = new ImageIcon(getClass().getResource(
                "/Sprites/Personaje/preview_arriba_derecha_nuevo.gif")).getImage();

        this.currentSprite = this.idleSprite;
    }

    /**
     * Desplaza al agente dentro de los limites del piso y actualiza su imagen.
     * Si no se presiona ninguna tecla, muestra el sprite quieto.
     *
     * @param dx Direccion horizontal.
     * @param dy Direccion vertical.
     * @param zone Zona actual donde se mueve el jugador.
     */
    public void move(int dx, int dy, Zone zone) {
        if (dx == 0 && dy == 0) {
            currentSprite = idleSprite;
            return;
        }

        if (dy < 0 && dx < 0) {
            currentSprite = walkUpLeft;
        } else if (dy < 0 && dx > 0) {
            currentSprite = walkUpRight;
        } else if (dy < 0) {
            currentSprite = walkUp;
        } else if (dy > 0) {
            currentSprite = walkDown;
        } else if (dx < 0) {
            currentSprite = walkLeft;
        } else if (dx > 0) {
            currentSprite = walkRight;
        }

        double nextX = x + dx * speed;

        if (zone.canMoveTo(getBoundsAt(nextX, y))) {
            this.x = nextX;
        }

        // Usa la X que el jugador pudo alcanzar al comprobar el paso horizontal.
        double nextY = y + dy * speed;

        if (zone.canMoveTo(getBoundsAt(x, nextY))) {
            this.y = nextY;
        }
    }

    @Override
    public void update() {
        // Reservado para futuras actualizaciones del jugador.
    }

    /**
     * Ubica al personaje directamente en una nueva coordenada del escenario.
     *
     * @param x Nueva posicion horizontal en pixeles.
     * @param y Nueva posicion vertical en pixeles.
     */
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Image getCurrentSprite() {
        return currentSprite;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }
}