package view;

import model.Agent;
import model.Zone;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private Agent agent;
    private Zone currentZone;

    public GamePanel(Agent agent, Zone currentZone) {
        this.agent = agent;
        this.currentZone = currentZone;
        setPreferredSize(new Dimension(1280, 720));
        setBackground(Color.BLACK);
        setFocusable(true);

    }

    public Zone getCurrentZone() {
        return currentZone;
    }

    public void setCurrentZone(Zone currentZone) {
        this.currentZone = currentZone;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (currentZone != null) {
            currentZone.draw(g, agent);

        }

        if (agent.getCurrentSprite() != null) {
            g.drawImage(
                    agent.getCurrentSprite(),
                    agent.getX(),
                    agent.getY(),
                    agent.getWidth(),
                    agent.getHeight(),
                    this
            );
        } else {
            g.setColor(new Color(0, 200, 150));
            g.fillRect(agent.getX(), agent.getY(), agent.getWidth(), agent.getHeight());
        }
    }
}
