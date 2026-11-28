package view;

import controller.GameLoopThread;
import controller.KeyboardController;
import controller.TimeController;
import model.Agent;
import model.Part;
import model.Zone;
import model.Zone3CoreRoom;

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
    private JLabel timeLabel;
    private TimeController timer;


    public MainFrame() {
        setTitle("jueguito poo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        Agent agent = new Agent(420, 400);
        Zone initialZone = new Zone3CoreRoom();

        KeyboardController keyboard = new KeyboardController();
        GamePanel gamePanel = new GamePanel(agent, initialZone);
        gamePanel.addKeyListener(keyboard);

        gameContainer.add(gamePanel, BorderLayout.CENTER);

        setContentPane(panel1);
        timer = new TimeController(60);
        timer.start();
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
        timeLabel.setText("Cronometro: " + timer.getCronTime() + "");
        timeLabel.setForeground(timeColor());
    }

    /**
     * Va poniendo las letras de distinto color dependiendo
     * de cuanto tiempo le falte
     *
     * @return verde, naranja o rojo
     */
    private Color timeColor() {
        double fraction = (double) timer.getSecondsLeft() / timer.getInitialSeconds();
        if (fraction > 0.5) {
            return new Color(20, 74, 20);
        } else if (fraction > 0.2) {
            return new Color(220, 127, 60);
        }
        return new Color(220, 60, 60);
    }
}
