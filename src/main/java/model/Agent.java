package model;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

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

    // Sprites de ataque
    private Image attackDown;
    private Image attackUp;
    private Image attackLeft;
    private Image attackRight;
    private Image attackUpLeft;
    private Image attackUpRight;
    private Image lastDirection;
    private long attackEndTime;

    // Pieza que el jugador lleva en la mano
    private Part carriedPart;
    private Map<Image, Image> leadSprites = new HashMap<>();
    private volatile long leadSuitUntil;

    //Tiempo de gracia tras recibir daño
    private long invulnerableUntil;

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

        this.idleSprite = new ImageIcon(getClass().getResource("/frames/Personaje Principal/Personaje_Principal.png")).getImage();
        this.walkDown = new ImageIcon(getClass().getResource("/Sprites/Personaje/preview_abajo.gif")).getImage();
        this.walkUp = new ImageIcon(getClass().getResource("/Sprites/Personaje/preview_arriba.gif")).getImage();
        this.walkLeft = new ImageIcon(getClass().getResource("/Sprites/Personaje/preview_izquierda.gif")).getImage();
        this.walkRight = new ImageIcon(getClass().getResource("/Sprites/Personaje/preview_derecha.gif")).getImage();
        this.walkUpLeft = new ImageIcon(getClass().getResource("/Sprites/Personaje/preview_arriba_izquierda.gif")).getImage();
        this.walkUpRight = new ImageIcon(getClass().getResource("/Sprites/Personaje/preview_arriba_derecha_nuevo.gif")).getImage();

        this.attackDown = new ImageIcon(getClass().getResource("/Sprites/Personaje/ataque/ataque_abajo.gif")).getImage();
        this.attackUp = new ImageIcon(getClass().getResource("/Sprites/Personaje/ataque/ataque_arriba.gif")).getImage();
        this.attackLeft = new ImageIcon(getClass().getResource("/Sprites/Personaje/ataque/ataque_izquierda.gif")).getImage();
        this.attackRight = new ImageIcon(getClass().getResource("/Sprites/Personaje/ataque/ataque_derecha.gif")).getImage();
        this.attackUpLeft = new ImageIcon(getClass().getResource("/Sprites/Personaje/ataque/ataque_arriba_izquierda.gif")).getImage();
        this.attackUpRight = new ImageIcon(getClass().getResource("/Sprites/Personaje/ataque/ataque_arriba_derecha.gif")).getImage();

        this.currentSprite = this.idleSprite;
        this.lastDirection = this.walkDown;
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
        if (isAttacking()) {
            return;
        }

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

        double currentSpeed = getCurrentSpeed();
        double nextX = x + moveX * currentSpeed;
        if (zone.canMoveTo(getBoundsAt(nextX, y))) {
            this.x = nextX;
        }

        double nextY = y + moveY * currentSpeed;
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

        lastDirection = currentSprite;
    }

    @Override
    public void update() {
        // Reservado para futuras actualizaciones del jugador.
    }

    /**
     * Calcula la velocidad: si lleva una pieza camina mas lento.
     *
     * @return Velocidad actual del jugador.
     */
    private double getCurrentSpeed() {
        if (carriedPart != null) {
            return speed * 0.75;
        }
        return speed;
    }

    /**
     * Toma una pieza en la mano.
     *
     * @param part Pieza recogida.
     */
    public void carryPart(Part part) {
        if (!hasPart()) {
            carriedPart = part;
        }
    }

    /**
     * Suelta la pieza que lleva en la mano.
     *
     * @return La pieza soltada, o null si no lleva ninguna.
     */
    public Part dropPart() {
        Part part = carriedPart;
        carriedPart = null;
        return part;
    }

    /**
     * Indica si el jugador lleva una pieza.
     *
     * @return true si tiene una pieza en la mano.
     */
    public boolean hasPart() {
        return carriedPart != null;
    }

    /**
     * Obtiene la pieza que lleva el jugador.
     *
     * @return La pieza en la mano, o null si no lleva ninguna.
     */
    public Part getCarriedPart() {
        return carriedPart;
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

    /**
     * Indica si una pastilla de yodo puede devolverle una vida al agente.
     *
     * @return true si tiene menos de las tres vidas iniciales.
     */
    public boolean needsHealing() {
        return lives < 3;
    }

    /** Recupera una vida con yodo hasta llegar al máximo de tres. */
    public void recoverLife() {
        if (needsHealing()) {
            lives++;
        }
    }

    /** Resta una vida al recibir daño si el traje de plomo no está activo. */
    public void takeDamage() {
        if (lives > 0 && !isWearingLeadSuit()) {
            lives--;
        }
    }

    /** Activa el traje durante cinco segundos conservando la dirección del agente. */
    public void wearLeadSuit() {
        if (leadSprites.isEmpty()) {
            loadLeadSprite(walkDown, "abajo");
            loadLeadSprite(walkUp, "arriba");
            loadLeadSprite(walkLeft, "izquierda");
            loadLeadSprite(walkRight, "derecha");
            loadLeadSprite(walkUpLeft, "arriba_izquierda");
            loadLeadSprite(walkUpRight, "arriba_derecha");
            leadSprites.put(idleSprite, leadSprites.get(walkDown));
        }
        leadSuitUntil = System.nanoTime() + 5_000_000_000L;
    }

    /**
     * Asocia un sprite normal con la misma dirección usando el traje.
     *
     * @param normal Sprite que usa el agente al moverse en esa dirección.
     * @param direction Nombre de la dirección en los archivos del traje.
     */
    private void loadLeadSprite(Image normal, String direction) {
        String path = "/Sprites/Personaje/traje-plomo/personaje_plomo_" + direction + ".gif";
        leadSprites.put(normal, new ImageIcon(getClass().getResource(path)).getImage());
    }

    /** @return true durante los cinco segundos que dura la protección. */
    public boolean isWearingLeadSuit() {
        return leadSuitUntil != 0 && System.nanoTime() - leadSuitUntil < 0;
    }

    /** @return Sprite correspondiente a la dirección y al traje actual del agente. */
    public Image getCurrentSprite() {
        if (isAttacking()) {
            return getAttackSprite();
        }
        if (isWearingLeadSuit()) {
            return leadSprites.get(currentSprite);
        }
        return currentSprite;
    }

    /**
     * Empieza la animacion de ataque hacia la ultima direccion en la que camino.
     */
    public void startAttack() {
        attackEndTime = System.currentTimeMillis() + 1000;
        getAttackSprite().flush();
    }

    /**
     * Indica si el jugador esta atacando.
     *
     * @return true mientras dura la animacion de ataque.
     */
    public boolean isAttacking() {
        return System.currentTimeMillis() < attackEndTime;
    }

    /**
     * Busca el sprite de ataque segun la ultima direccion en la que camino.
     *
     * @return Sprite de ataque.
     */
    private Image getAttackSprite() {
        if (lastDirection == walkUp) {
            return attackUp;
        } else if (lastDirection == walkLeft) {
            return attackLeft;
        } else if (lastDirection == walkRight) {
            return attackRight;
        } else if (lastDirection == walkUpLeft) {
            return attackUpLeft;
        } else if (lastDirection == walkUpRight) {
            return attackUpRight;
        } else {
            return attackDown;
        }
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

    /**
     * Quita una vida al jugador, a menos que este en su tiempo de gracia
     * despues de perder una vida no puede volver a perder una vida durante 1,5 s
     */
    public void loseLife() {
        if (lives > 0 && !isHurt() && !isWearingLeadSuit()) {
            lives--;
            invulnerableUntil = System.currentTimeMillis() + 1500;
        }
    }

    /**
     * Indica si el jugador esta en su tiempo de gracia despues del golpe
     *
     * @return
     */
    public boolean isHurt() {
        return System.currentTimeMillis() < invulnerableUntil;
    }
}