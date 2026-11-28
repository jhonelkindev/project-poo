package model;

import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

public class SteamCloud extends PatrollingEnemy {
    private static final String SPRITES = "/Sprites/Nivel-1/enemigo/";

    private static final Image IDLE = new ImageIcon(SteamCloud.class.getResource(
            SPRITES + "vapor_radiactivo.gif")).getImage();

    private static final Image MOVING = new ImageIcon(SteamCloud.class.getResource(
            SPRITES + "vapor_radiactivo_movimiento.gif")).getImage();

    private static final URL ATTACK_GIF = SteamCloud.class.getResource(
            SPRITES + "vapor_radiactivo_golpe.gif");

    // Los cuatro fotogramas del golpe duran 410 milisegundos en total.
    private static final long ATTACK_NANOS = 410_000_000L;

    private Image currentImage = IDLE;
    private Image attackImage;
    private long attackStart;

    public SteamCloud(double x, double y) {
        super(x, y, 48, 48);
    }

    @Override
    protected void updateImage(int goX, int goY, boolean moved) {
        currentImage = moved ? MOVING : IDLE;
    }

    @Override
    public void bite(Agent agent) {
        int previousLives = agent.getLives();

        super.bite(agent);

        if (agent.getLives() < previousLives) {
            // Cada golpe empieza desde el primer fotograma del GIF.
            attackImage = new ImageIcon(
                    Toolkit.getDefaultToolkit().createImage(ATTACK_GIF)
            ).getImage();

            attackStart = System.nanoTime();
        }
    }

    @Override
    public void draw(Graphics graphics) {
        if (alive) {
            Image image = currentImage;

            if (attackImage != null && System.nanoTime() - attackStart < ATTACK_NANOS) {
                image = attackImage;
            }

            graphics.drawImage(image, getX(), getY(), width, height, null);
        }
    }
}