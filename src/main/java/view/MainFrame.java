package view;

import controller.GameLoopThread;
import controller.KeyboardController;
import model.Agent;
import model.Part;
import model.Zone;
import model.Zone1BoilerRoom;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel panel1;
    private JPanel hudPanel;
    private JPanel gameContainer;
    private JLabel zoneLabel;
    private JLabel livesLabel;
    private JLabel objectivesLabel;
    private JLabel carryingLabel;


    public MainFrame() {
        setTitle("jueguito poo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        Agent agent = new Agent(620, 460);
        Zone initialZone = new Zone1BoilerRoom();

        KeyboardController keyboard = new KeyboardController();
        GamePanel gamePanel = new GamePanel(agent, initialZone);
        gamePanel.addKeyListener(keyboard);

        gameContainer.add(gamePanel, BorderLayout.CENTER);

        setContentPane(panel1);

        pack();
        setLocationRelativeTo(null);

        GameLoopThread gameLoop = new GameLoopThread(agent, keyboard, gamePanel, this);
        gameLoop.start();
    }

    /**
     * Actualiza los textos de la barra superior con los datos del juego
     *
     * @param agent jugador
     * @param zone  zona actual
     */
    public void updateHud(Agent agent, Zone zone) {
        zoneLabel.setText(zone.getZoneName());
        livesLabel.setText("Vidas: " + agent.getLives());
        objectivesLabel.setText(zone.getObjectiveText());

        if (agent.isHurt()) {
            livesLabel.setForeground(Color.RED);
        } else {
            livesLabel.setForeground(Color.BLACK);
        }

        Part part = agent.getCarriedPart();
        if (part == null) {
            carryingLabel.setText("Llevas: nada");
        } else if (part.isClean()) {
            carryingLabel.setText("Llevas: " + part.getType() + " (limpia)");
        } else {
            carryingLabel.setText("Llevas: " + part.getType() + " (sucia)");
        }
    }
}
