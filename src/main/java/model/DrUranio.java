package model;


import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Jefe final del juego, hace aparicion en el nivel 3, su mecanica consiste en
 * estar fuera del mapa y disparar proyectiles con el fin de dificultarle al
 * jugador meter las barras en el nucleo
 *
 * @author Mariana Ocampo Cardona
 * @version 1.0
 */
public class DrUranio extends Enemy {
    private static final String SPRITES = "/Sprites/Nivel-3/jefe_final/";
    private static final int MIN_X = 380;
    private static final int MAX_X = 780;

    private Image moveImg;
    private Image stunnedImg;
    private Image currentImage;

    private List<Projectile> projectiles = new CopyOnWriteArrayList<>();
    private int dirX = 1;
    private int shootDelay = 2200;
    private long lastShot;
    private long stunnedUntil;

    /**
     * Crea al DrUranio en la parte de arriba del cuarto
     */
    public DrUranio() {
        super(560, 40, 128, 128, 1.5);
        moveImg = new ImageIcon(getClass().getResource(SPRITES + "valerio_movimiento.gif")).getImage();
        stunnedImg = new ImageIcon(getClass().getResource(SPRITES + "valerio_aturdido.gif")).getImage();
        currentImage = moveImg;
        lastShot = System.currentTimeMillis();
    }

    /**
     * Dispara un proyectil desde DrUranio para que impacte con al jugador
     *
     * @param agent Jugador
     */
    private void shoot(Agent agent) {
        projectiles.add(new Projectile(x + width / 2 - 12, y + height / 2, agent));
        lastShot = System.currentTimeMillis();
    }

    /**
     * Mueve los proyectiles y quita vida si un proyectil
     * impacta el hitbox del jugador
     */
    private void moveProjectiles(Agent agent) {
        for (Projectile projectile : projectiles) {
            projectile.update();
            if (projectile.hits(agent)) {
                agent.loseLife();
                projectile.deactivate();
            }
            if (!projectile.isActive()) {
                projectiles.remove(projectile);
            }
        }
    }

    /**
     * Aturdir al DrUranio
     *
     * @param millis duracion del aturdimiento
     */
    public void stun(int millis) {
        stunnedUntil = System.currentTimeMillis() + millis;
    }

    /**
     * Indica si el enemigo esta aturdido
     */
    public boolean isStunned() {
        return stunnedUntil > System.currentTimeMillis();
    }

    /**
     * Dispara mas rapido cuando el nucleo ya tiene dos barras
     *
     */
    public void enrage() {
        shootDelay = 1500;
    }

    /**
     * El doctor no recibe golpes, solo se aturde
     */
    @Override
    public void hit() {

    }
    @Override
    public void update() {

    }
    @Override
    public boolean isDangerous(){
        return false;
    }

    /**
     * El doctor no ataca por contacto
     *
     * @param agent Jugador
     */
    @Override
    public void bite(Agent agent) {
    }

    /**
     * Dibuja al cientifico afuera del mapa
     *
     * @param g Contexto grafico de Swing
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(currentImage, getX(), getY(), width, height, null);
        for (Projectile projectile : projectiles) {
            projectile.draw(g);
        }
    }

    /**
     * Pasa de lado a lado, dispara cada cierto tiempo y es aturdido
     */
    @Override
    public void move(Agent agent, Zone zone) {
        if (isStunned()) {
            currentImage = stunnedImg;
        } else {
            currentImage = moveImg;
            x += dirX * speed;
            if (x < MIN_X || x > MAX_X) {
                dirX = -dirX;
            }
            if (System.currentTimeMillis() - lastShot >= shootDelay) {
                shoot(agent);
            }
        }
        moveProjectiles(agent);
    }


}
