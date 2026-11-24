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
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Zone zone = currentZone;

        if (zone == null) {
            drawAgent(graphics);
        } else {
            zone.drawScene(graphics, agent, () -> drawAgent(graphics));
        }
    }

    /**
     * Dibuja el sprite actual del jugador.
     *
     * @param graphics Contexto grafico de Swing.
     */
    private void drawAgent(Graphics graphics) {
        if (agent.getCurrentSprite() != null) {
            graphics.drawImage(
                    agent.getCurrentSprite(),
                    agent.getX(),
                    agent.getY(),
                    agent.getWidth(),
                    agent.getHeight(),
                    this
            );
        } else {
            graphics.setColor(new Color(0, 200, 150));
            graphics.fillRect(agent.getX(), agent.getY(),
                    agent.getWidth(), agent.getHeight());
        }
    }
}
