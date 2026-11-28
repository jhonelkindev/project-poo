package controller;

import model.Agent;
import model.Enemy;
import model.Zone;

import java.util.List;

public class EnemyThread extends Thread {
    private final List<Enemy> enemies;
    private final Agent agent;
    private final Zone zone;
    private boolean running = true;

    public EnemyThread(List<Enemy> enemies, Agent agent, Zone zone) {
        this.enemies = enemies;
        this.agent = agent;
        this.zone = zone;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                synchronized (zone) {
                    if (!running) {
                        return;
                    }

                    for (Enemy enemy : enemies) {
                        enemy.move(agent, zone);
                        enemy.update();

                        if (enemy.isDangerous() && enemy.touches(agent)) {
                            enemy.bite(agent);
                        }
                    }
                }

                Thread.sleep(16);
            }
        } catch (InterruptedException e) {
            interrupt();
        }
    }

    public void stopEnemies() {
        synchronized (zone) {
            running = false;
        }

        interrupt();
    }
}