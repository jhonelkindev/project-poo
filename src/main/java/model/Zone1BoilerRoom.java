package model;

import java.awt.*;

public class Zone1BoilerRoom extends Zone {


    public Zone1BoilerRoom() {
        super("Zona 1: Sala de Calderas");
    }


    @Override
    public void update(Agent agent) {
        // Espacio reservado para el Nivel 1
    }


    @Override
    public void draw(Graphics graphics, Agent agent) {
        graphics.setColor(new Color(50, 56, 68));
        graphics.fillPolygon(floorArea);
        graphics.setColor(new Color(220, 175, 45));
        graphics.drawPolygon(floorArea);

        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.BOLD, 16));
        graphics.drawString("ZONA 1 - N para pasar a Zona 2", 410, 390);
    }


    @Override
    public boolean isCompleted() {
        return false;
    }
}