package view;

import controller.GameLoopThread;
import controller.KeyboardController;
import model.Agent;
import model.Zone;
import model.Zone1BoilerRoom;

import javax.swing.*;

public class MainFrame extends JFrame {
    private JPanel panel1;


    public MainFrame() {
        setTitle("jueguito poo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        Agent agent = new Agent(620, 460);
        Zone initialZone = new Zone1BoilerRoom();

        KeyboardController keyboard = new KeyboardController();
        GamePanel gamePanel = new GamePanel(agent, initialZone);

        gamePanel.addKeyListener(keyboard);
        setContentPane(gamePanel);

        pack();
        setLocationRelativeTo(null);

        GameLoopThread gameLoop = new GameLoopThread(agent, keyboard, gamePanel);
        gameLoop.start();
    }

}
