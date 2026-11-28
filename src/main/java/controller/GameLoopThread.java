package controller;

import model.Agent;
import model.Zone;
import model.Zone1BoilerRoom;
import model.Zone2EngineRoom;
import view.GamePanel;
import view.MainFrame;

import javax.swing.*;

public class GameLoopThread extends Thread {
    private final MainFrame mainFrame;
    private EnemyThread enemyThread;
    private Agent agent;
    private KeyboardController keyboard;
    private GamePanel gamePanel;

    public GameLoopThread(Agent agent, KeyboardController keyboard, GamePanel gamePanel, MainFrame mainFrame) {
        this.agent = agent;
        this.keyboard = keyboard;
        this.gamePanel = gamePanel;
        this.mainFrame = mainFrame;
    }


    @Override
    public void run() {
        try {
            startEnemies(gamePanel.getCurrentZone());

            while (!isInterrupted()) {
                update();

                if (agent.getLives() <= 0) {
                    SwingUtilities.invokeLater(() -> mainFrame.showGameOver());
                    break;
                }

                gamePanel.repaint();
                SwingUtilities.invokeLater(() -> mainFrame.updateHud(agent, gamePanel.getCurrentZone()));
                Thread.sleep(16);
            }
        } catch (InterruptedException e) {
            interrupt();
        } finally {
            stopEnemies();
        }
    }

    private void startEnemies(Zone zone) {
        if (zone != null && !zone.getEnemies().isEmpty()) {
            enemyThread = new EnemyThread(zone.getEnemies(), agent, zone);
            enemyThread.start();
        }
    }

    private void stopEnemies() {
        if (enemyThread != null) {
            enemyThread.stopEnemies();
            enemyThread = null;
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

        if (currentZone == null) {
            return;
        }

        synchronized (currentZone) {
            agent.move(dx, dy, currentZone);

            if (keyboard.consumePickUp()) {
                currentZone.pickUp(agent);
            }

            if (keyboard.consumeDrop()) {
                currentZone.drop(agent);
            }

            if (keyboard.isAction()) {
                currentZone.action(agent);
            }

            if (keyboard.consumeAttack() && !agent.isAttacking()) {
                agent.startAttack();
                currentZone.attack(agent);
            }

            agent.update();
            currentZone.update(agent);

            if (currentZone instanceof Zone1BoilerRoom && keyboard.isNextLevel()) {
                stopEnemies();

                Zone2EngineRoom zone2 = new Zone2EngineRoom();
                agent.setPosition(200, 370);
                gamePanel.setCurrentZone(zone2);

                startEnemies(zone2);
            }
        }
    }

    public void stopLoop() {
        interrupt();
    }
}