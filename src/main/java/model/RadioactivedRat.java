package model;

import javax.swing.*;
import java.awt.*;

/**
 * Rata radioactiva
 * patruya la sala, persigue al jugador si lo ve cerca y al recibir un gole huye o muere
 */
public class RadioactivedRat extends Enemy {
    private Image walkUp;
    private Image walkDown;
    private Image walkLeft;
    private Image walkRight;
    private Image walkUpLeft;
    private Image walkUpRight;
    private Image walkDownLeft;
    private Image walkDownRight;
    private Image deadImage;
    private Image currentImage;
    private int dirX;
    private int dirY;
    private int framesToTurn;
    private boolean fleeing;
    private long fleeEndTime;


    /**
     * Crea una rata en la posicion del mapa
     *
     * @param x
     * @param y
     */
    public RadioactivedRat(double x, double y) {
        super(x, y, 44, 40, 0.8);
        walkUp = new ImageIcon(getClass().getResource("/Sprites/Nivel-2/Rata/Rata_Arriba_caminata.gif")).getImage();
        walkDown = new ImageIcon(getClass().getResource("/Sprites/Nivel-2/Rata/Rata_Abajo_caminata.gif")).getImage();
        walkLeft = new ImageIcon(getClass().getResource("/Sprites/Nivel-2/Rata/Rata_Izquierda_caminata.gif")).getImage();
        walkRight = new ImageIcon(getClass().getResource("/Sprites/Nivel-2/Rata/Rata_Derecha_caminata.gif")).getImage();
        walkUpLeft = new ImageIcon(getClass().getResource("/Sprites/Nivel-2/Rata/Rata_ArribaIzquierda_caminata.gif")).getImage();
        walkUpRight = new ImageIcon(getClass().getResource("/Sprites/Nivel-2/Rata/Rata_ArribaDerecha_caminata.gif")).getImage();
        walkDownLeft = new ImageIcon(getClass().getResource("/Sprites/Nivel-2/Rata/Rata_AbajoIzquierda_caminata.gif")).getImage();
        walkDownRight = new ImageIcon(getClass().getResource("/Sprites/Nivel-2/Rata/Rata_AbajoDerecha_caminata.gif")).getImage();
        deadImage = new ImageIcon(getClass().getResource("/frames/Nivel-2/Rata/Rata_Derecha.png")).getImage();

        currentImage = walkDown;
        chooseRandomDirection();
    }

    /**
     * Movimiento de la rata, huye o persigue al jugador
     *
     * @param agent Jugador
     * @param zone  Zona donde se mueve el enemigo
     */
    @Override
    public void move(Agent agent, Zone zone) {
        if (alive) {
            int goX;
            int goY;
            double currentSpeed;

            if (fleeing) {
                goX = -directionTo(getX(), agent.getX());
                goY = -directionTo(getY(), agent.getY() + agent.getHeight() - height);
                currentSpeed = 1.6;
            } else if (distanceTo(agent) < 180) {
                goX = directionTo(getX(), agent.getX());
                goY = directionTo(getY(), agent.getY() + agent.getHeight() - height);
                currentSpeed = 1.2;
            } else {
                framesToTurn--;
                if (framesToTurn <= 0) {
                    chooseRandomDirection();
                }
                goX = dirX;
                goY = dirY;
                currentSpeed = speed;
            }

            double moveX = goX * 0.85;
            double moveY = goY * 0.45;

            boolean moved = false;

            double nextX = x + moveX * currentSpeed;
            if (canGo(zone, nextX, y)) {
                x = nextX;
                moved = true;
            }

            double nextY = y + moveY * currentSpeed;
            if (canGo(zone, x, nextY)) {
                y = nextY;
                moved = true;
            }

            if (!moved) {
                chooseRandomDirection();
            }
            updateImage(goX, goY);
        }
    }

    /**
     * Revisa si la rata puede moverse a una posicion del piso
     *
     * @param zone  Zona donde se mueve el enemigo
     * @param nextX posicion horizontal
     * @param nextY posicion vertical
     * @return true si se puede mover
     */
    private boolean canGo(Zone zone, double nextX, double nextY) {
        Rectangle body = new Rectangle((int) nextX, (int) nextY, width, height);
        return zone.canMoveTo(getBoundsAt(nextX, nextY)) && zone.getFloorArea().contains(body);
    }

    /**
     * la rata termina su huida pasado el tiempo
     */
    @Override
    public void update() {
        if (fleeing && System.currentTimeMillis() >= fleeEndTime) {
            fleeing = false;
        }
    }

    /**
     * La rata muerde al jugador y despues huye
     *
     * @param agent Jugador
     */
    @Override
    public void bite(Agent agent) {
        agent.loseLife();
        flee(1500);
    }

    /**
     * Si la rata recibe un golpe al azar huye o se muere
     */
    @Override
    public void hit() {
        if (Math.random() < 0.5) {
            alive = false;
        } else {
            flee(4000);
        }
    }

    /**
     * Hace que la rata huya del jugador durante un tiempo
     *
     * @param millis
     */
    public void flee(int millis) {
        fleeing = true;
        fleeEndTime = System.currentTimeMillis() + millis;
    }

    /**
     * La rata solo hace daño si esta viva y no esta huyendo
     *
     * @return
     */
    @Override
    public boolean isDangerous() {
        return alive && !fleeing;
    }

    /**
     * Dibuja la rata caminando o su cadaver boca arriba si se murio
     * La imagen esta vista desde arriba, por eso se dibuja un poco mas baja
     * y asi queda acostada sobre el piso de la vista isometrica
     *
     * @param graphics Contexto grafco de Swing
     */
    @Override
    public void draw(Graphics graphics) {
        Image image = currentImage;
        if (!alive) {
            image = deadImage;
        }

        int imageWidth = image.getWidth(null) * 3 / 2;
        int imageHeight = image.getHeight(null) * 9 / 8;

        int drawX = getX() + width / 2 - imageWidth / 2;
        int drawY = getY() + height - imageHeight;

        graphics.setColor(new Color(0, 0, 0, 90));
        graphics.fillOval(getX() + 4, getY() + height - 10, width - 8, 12);

        if (alive) {
            graphics.drawImage(image, drawX, drawY, imageWidth, imageHeight, null);
        } else {
            graphics.drawImage(image, drawX, drawY + imageHeight, imageWidth, -imageHeight, null);
        }
    }

    /**
     * Elige al azar hacia donde moverse y por cuanto tiempo hacerlo
     */
    private void chooseRandomDirection() {
        dirX = (int) (Math.random() * 3) - 1;
        dirY = (int) (Math.random() * 3) - 1;
        framesToTurn = 60 + (int) (Math.random() * 120);
    }

    /**
     * Indica a la rata como moverse para llegar a un punto (jugador)
     *
     * @param from posicion actual
     * @param to   posuion de destino
     * @return 1 para avanzar, -1 para retroceder o 0 si ya esta en la posicion
     */
    private int directionTo(int from, int to) {
        if (to > from + 2) {
            return 1;
        } else if (to < from - 2) {
            return -1;
        } else {
            return 0;
        }
    }


    /**
     * Calcula la distancia entre la rata y el jugador
     *
     * @param agent
     * @return
     */
    private double distanceTo(Agent agent) {
        double diffX = agent.getBounds().getCenterX() - getBounds().getCenterX();
        double diffY = agent.getBounds().getCenterY() - getBounds().getCenterY();
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    /**
     * Cambia la imagen segun la direccion de movimiento
     *
     * @param goX direccion horizontal
     * @param goY direccion vertical
     */
    private void updateImage(int goX, int goY) {
        if (goY < 0 && goX < 0) {
            currentImage = walkUpLeft;
        } else if (goY < 0 && goX > 0) {
            currentImage = walkUpRight;
        } else if (goY > 0 && goX < 0) {
            currentImage = walkDownLeft;
        } else if (goY > 0 && goX > 0) {
            currentImage = walkDownRight;
        } else if (goY < 0) {
            currentImage = walkUp;
        } else if (goY > 0) {
            currentImage = walkDown;
        } else if (goX < 0) {
            currentImage = walkLeft;
        } else if (goX > 0) {
            currentImage = walkRight;
        }
    }
}
