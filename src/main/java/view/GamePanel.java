package view;

import model.Agent;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private Agent agent;

    public GamePanel(Agent agent) {
        this.agent = agent;
        setPreferredSize(new Dimension(1280, 720));
        setBackground(new Color(40, 44, 52));
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(25, 28, 34));
        g.fillRect(0, 0, getWidth(), 60);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Jugador" + agent.getPlayerName(), 20, 38);
        g.drawString("vidas" + agent.getLives(), 260,38);
        g.drawString("Puntaje"+ agent.getScore(), 420,38);

        g.setColor(new Color(0,200,150));
        g.fillRect(agent.getX(), agent.getY(), agent.getWidth(), agent.getHeight());
    }
}
