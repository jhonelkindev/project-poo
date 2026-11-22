package controller;

import model.Agent;
import model.Zone;
import model.Zone1BoilerRoom;
import model.Zone2EngineRoom;
import view.GamePanel;

public class GameLoopThread extends Thread {
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

        Zone currentZone = gamePanel.getCurrentZone();
        if (currentZone != null) {
            agent.move(dx, dy, currentZone.getFloorArea());
            agent.update();
            currentZone.update(agent);


            if (currentZone instanceof Zone1BoilerRoom && keyboard.isNextLevel()) {
                gamePanel.setCurrentZone(new Zone2EngineRoom());
                agent.setPosition(170, 420);
            }
        }
    }

    public void stopLoop() {
        running = false;
    }
}
