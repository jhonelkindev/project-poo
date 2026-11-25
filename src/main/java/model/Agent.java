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
     * Desplaza al agente en perspectiva isomeytrica dentro de los limites del piso y actualiza su imagen.
     * Si no se presiona ninguna tecla, muestra el sprite quieto.
     *
     * @param dx   Direccion horizontal.
     * @param dy   Direccion vertical.
     * @param zone Zona actual donde se mueve el jugador.
     */
    public void move(int dx, int dy, Zone zone) {
        if (dx == 0 && dy == 0) {
            currentSprite = idleSprite;
            return;
        }

        double moveX = (dx - dy) * 0.85;
        double moveY = (dx + dy) * 0.45;

        if (moveX == 0) {
            moveX = dx * 1.0;
        }

        updateDirectionSprite(moveX, moveY);

        double nextX = x + moveX * speed;
        if (zone.canMoveTo(getBoundsAt(nextX, y))) {
            this.x = nextX;
        }

        double nextY = y + moveY * speed;
        if (zone.canMoveTo(getBoundsAt(x, nextY))) {
            this.y = nextY;
        }
    }

    /**
     * Selecciona el sprite correspondiente segun el movimiento del jugador
     *
     * @param isoX desplazamiento horizontal del jugador
     * @param isoY desplazamiento vertical del jugador
     */
    private void updateDirectionSprite(double isoX, double isoY) {
        if (isoY < -0.1 && isoX < -0.1) {
            currentSprite = walkUpLeft;
        } else if (isoY < -0.1 && isoX > 0.1) {
            currentSprite = walkUpRight;
        } else if (isoY < -0.1) {
            currentSprite = walkUp;
        } else if (isoY > 0.1 && Math.abs(isoX) <= 0.1) {
            currentSprite = walkDown;
        } else if (isoX < -0.1) {
            currentSprite = walkLeft;
        } else if (isoX > 0.1) {
            currentSprite = walkRight;
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