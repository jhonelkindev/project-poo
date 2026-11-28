package model;

import javax.swing.*;
import java.awt.*;

public class RadioactivedRat extends PatrollingEnemy {
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

    public RadioactivedRat(double x, double y) {
        super(x, y, 44, 40);

        walkUp = new ImageIcon(getClass().getResource(
                "/Sprites/Nivel-2/Rata/Rata_Arriba_caminata.gif")).getImage();
        walkDown = new ImageIcon(getClass().getResource(
                "/Sprites/Nivel-2/Rata/Rata_Abajo_caminata.gif")).getImage();
        walkLeft = new ImageIcon(getClass().getResource(
                "/Sprites/Nivel-2/Rata/Rata_Izquierda_caminata.gif")).getImage();
        walkRight = new ImageIcon(getClass().getResource(
                "/Sprites/Nivel-2/Rata/Rata_Derecha_caminata.gif")).getImage();
        walkUpLeft = new ImageIcon(getClass().getResource(
                "/Sprites/Nivel-2/Rata/Rata_ArribaIzquierda_caminata.gif")).getImage();
        walkUpRight = new ImageIcon(getClass().getResource(
                "/Sprites/Nivel-2/Rata/Rata_ArribaDerecha_caminata.gif")).getImage();
        walkDownLeft = new ImageIcon(getClass().getResource(
                "/Sprites/Nivel-2/Rata/Rata_AbajoIzquierda_caminata.gif")).getImage();
        walkDownRight = new ImageIcon(getClass().getResource(
                "/Sprites/Nivel-2/Rata/Rata_AbajoDerecha_caminata.gif")).getImage();

        deadImage = new ImageIcon(getClass().getResource(
                "/frames/Nivel-2/Rata/Rata_Derecha.png")).getImage();

        currentImage = walkDown;
    }

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
            graphics.drawImage(image, drawX, drawY + imageHeight,
                    imageWidth, -imageHeight, null);
        }
    }

    @Override
    protected void updateImage(int goX, int goY, boolean moved) {
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