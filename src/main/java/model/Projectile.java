package model;

import javax.swing.*;
import java.awt.*;

/**
 * Proyectil radiactivo que dispara el Dr Uranio al jugador con el objetivo de
 * danarlo para que no pueda completar el nivel
 *
 * @author Mariana Ocampo Cardona
 * @version 1.0
 */
public class Projectile extends Entity {
    private double dirX;
    private double dirY;
    private boolean active;
    private Image image;

    /**
     * Crea el proyectil que avanza directamente hacia el jugador
     *
     * @param x      posicion horizontal
     * @param y      posicion vertical
     * @param target jugador
     */

    public Projectile(double x, double y, Agent target) {
        super(x, y, 24, 24, 4);
        double dx = target.getX() + target.getWidth() / 2 - x;
        double dy = target.getY() + target.getHeight() / 2 - y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        dirX = dx / distance;
        dirY = dy / distance;
        active = true;
        image = new ImageIcon(getClass().getResource("/Sprites/Nivel-3/jefe_final/proyectil_radiactivo.gif")).getImage();

    }

    /**
     * Envia el proyectil para que se mueva hacia el frente y si
     */
    @Override
    public void update() {
        x += dirX * speed;
        y += dirY * speed;
        if (x < 0 || x > 1280 || y < 0 || y > 720) {
            active = false;
        }
    }

    /**
     * Revisa si el proyectil toco al jugador
     *
     * @param agent jugador
     * @return true si lo toc
     */
    public boolean hits(Agent agent) {
        Rectangle body = new Rectangle(agent.getX(), agent.getY(), agent.getWidth(), agent.getHeight());
        return body.intersects(new Rectangle(getX(), getY(), width, height));
    }

    /**
     * Indica si el proyectil sigue en la pantalla
     *
     * @return true si sigue en la pantalla
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Dibujar el proyectil
     *
     * @param g pincel
     */
    public void draw(Graphics g) {
        g.drawImage(image, getX(), getY(), width, height, null);
    }

    /**
     * Apaga el proyectil cuando golpea al jugador
     */
    public void deactivate() {
        active = false;
    }
}
