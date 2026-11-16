package view;

import controller.GameLoopThread;
import controller.KeyboardController;
import model.Agent;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel panel1;


    public MainFrame() {
        setTitle("jueguito poo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        Agent agent = new Agent(600, 350);
        KeyboardController Keyboard = new KeyboardController();
        GamePanel gamePanel = new GamePanel(agent);

        gamePanel.addKeyListener(Keyboard);
        setContentPane(gamePanel);

        pack();
        setLocationRelativeTo(null);

        GameLoopThread gameLoop = new GameLoopThread(agent, Keyboard, gamePanel);
        gameLoop.start();
    }

}
