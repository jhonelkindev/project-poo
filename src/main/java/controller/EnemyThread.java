package controller;

import model.Agent;
import model.Enemy;
import model.Zone;

import java.util.List;

/**
 * Hilo que mueve a los enemigos y revisan si toca al jugador
 */
public class EnemyThread extends Thread {
    private List<Enemy> enemies;
    private Agent agent;
    private Zone zone;
    private boolean running;

    /**
     * Crea el hilo de los enemigos de una zona
     *
     * @param enemies Enemigos que se van a mover
     * @param agent   Jugador
     * @param zone    Zona donde estan los enemigos
     */
    public EnemyThread(List<Enemy> enemies, Agent agent, Zone zone) {
        this.enemies = enemies;
        this.agent = agent;
        this.zone = zone;
        this.running = false;
    }

    /**
     * mueve a los enemigos 60 veces por segundo
     */
    @Override
    public void run() {
        running = true;

        while (running) {
            for (Enemy enemy : enemies) {
                enemy.move(agent, zone);
                enemy.update();

                if (enemy.isDangerous() && enemy.touches(agent)) {
                    enemy.bite(agent);
                }
            }

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Detiene el hilo de lo enemigos
     */
    public void stopEnemies() {
        running = false;
    }

}
