package model;

import java.awt.*;

public class Decoration {

    private Image image;
    private  Rectangle bounds;

    public Decoration(Image image, Rectangle bounds) {
        this.image = image;
        this.bounds = bounds;
    }

    public void draw(Graphics g) {

        g.drawImage(image, bounds.x, bounds.y, bounds.width, bounds.height, null);

    }

}
