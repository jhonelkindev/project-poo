package view;

import model.Agent;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private Agent agent;
    private Polygon floorArea;

    public GamePanel(Agent agent) {
        this.agent = agent;
        setPreferredSize(new Dimension(1280, 720));
        setBackground(Color.BLACK);
        setFocusable(true);

        int[] xPoints = {640, 1180, 640, 100};
        int[] yPoints = {100, 400, 700, 400};
        this.floorArea = new Polygon(xPoints, yPoints, 4);
    }

    public Polygon getFloorArea() {
        return floorArea;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(50, 56, 68));
        g.fillPolygon(floorArea);
        g.setColor(new Color(220, 175, 45));
        g.drawPolygon(floorArea);

        g.setColor(new Color(25, 28, 34));
        g.fillRect(0, 0, getWidth(), 50);


        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Jugador" + agent.getPlayerName(), 20, 38);
        g.drawString("vidas" + agent.getLives(), 260, 38);
        g.drawString("Puntaje" + agent.getScore(), 420, 38);

        //Personaje
        g.setColor(new Color(0, 200, 150));
        g.fillRect(agent.getX(), agent.getY(), agent.getWidth(), agent.getHeight());
    }
}
