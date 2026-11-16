package controller;

import model.Agent;
import view.GamePanel;

public class GameLoopThread extends Thread {
    private static final int FPS = 60;
    private boolean running;
    private Agent agent;
    private KeyboardController keyboard;
    private GamePanel gamePanel;

    public GameLoopThread(Agent agent, KeyboardController keyboard, GamePanel gamePanel) {
        this.agent = agent;
        this.keyboard = keyboard;
        this.gamePanel = gamePanel;
        this.running = false;
    }

    @Override
    public void run() {
        running = true;

        while (running) {
            update();
            gamePanel.repaint();

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        int dx = 0;
        int dy = 0;

        if (keyboard.isUp()) dy -= 1;
        if (keyboard.isDown()) dy += 1;
        if (keyboard.isLeft()) dx -= 1;
        if (keyboard.isRight()) dx += 1;

        agent.move(dx, dy, gamePanel.getWidth(), gamePanel.getHeight());
        agent.update();
    }

    public void stopLoop() {
        running = false;
    }
}
