package model;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class Zone {
    protected String zoneName;
    protected Polygon floorArea;
    protected List<GameObject> gameObjects;
    protected final List<Enemy> enemies = new ArrayList<>();

    private List<Decoration> decorations = new ArrayList<>();
    private CollisionMap collisionMap = new CollisionMap();

    public Zone(String zoneName) {
        this.zoneName = zoneName;
        this.gameObjects = new ArrayList<>();

        int[] xPoints = {640, 1180, 640, 100};
        int[] yPoints = {100, 400, 700, 400};
        this.floorArea = new Polygon(xPoints, yPoints, 4);
    }

    public abstract void update(Agent agent);

    public abstract void draw(Graphics graphics, Agent agent);

    public abstract boolean isCompleted();

    public Polygon getFloorArea() {
        return floorArea;
    }

    public List<GameObject> gameObjects() {
        return gameObjects;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public String getZoneName() {
        return zoneName;
    }

    protected Image loadImage(String path) {
        return new ImageIcon(getClass().getResource(path)).getImage();
    }

    protected void addDecoration(Decoration decoration) {
        decorations.add(decoration);

        if (decoration.getBase() != null) {
            addObstacle(decoration.getBase());
        }
    }

    protected void sortDecorationsByDepth() {
        decorations.sort(Comparator.comparingInt(Decoration::getDepth));
    }

    protected void drawDecorations(Graphics graphics) {
        for (Decoration decoration : decorations) {
            decoration.draw(graphics);
        }
    }

    protected void drawDecorationsWithAgent(Graphics graphics, Agent agent,
                                            Runnable drawAgent) {
        drawDecorationsWithAgent(graphics, agent, drawAgent, new ArrayList<>());
    }

    protected void drawDecorationsWithAgent(Graphics graphics, Agent agent,
                                            Runnable drawAgent, List<Enemy> enemies) {
        Rectangle sprite = new Rectangle(
                agent.getX(), agent.getY(), agent.getWidth(), agent.getHeight());

        Rectangle feet = agent.getBounds();
        boolean agentDrawn = false;

        boolean[] enemyDrawn = new boolean[enemies.size()];
        for (int i = 0; i < enemies.size(); i++) {
            enemyDrawn[i] = !enemies.get(i).isAlive();
        }

        for (Decoration decoration : decorations) {
            for (int i = 0; i < enemies.size(); i++) {
                Enemy enemy = enemies.get(i);

                if (!enemyDrawn[i]
                        && decoration.isInFrontOf(enemy.getSpriteBounds(), enemy.getBounds())) {
                    enemy.draw(graphics);
                    enemyDrawn[i] = true;
                }
            }

            if (!agentDrawn && decoration.isInFrontOf(sprite, feet)) {
                drawAgent.run();
                agentDrawn = true;
            }

            decoration.draw(graphics);
        }

        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);

            if (!enemyDrawn[i] && enemy.getBounds().getMaxY() < feet.getMaxY()) {
                enemy.draw(graphics);
                enemyDrawn[i] = true;
            }
        }

        if (!agentDrawn) {
            drawAgent.run();
        }

        for (int i = 0; i < enemies.size(); i++) {
            if (!enemyDrawn[i]) {
                enemies.get(i).draw(graphics);
            }
        }
    }

    protected void addObstacle(Shape obstacle) {
        collisionMap.addObstacle(obstacle);
    }

    public boolean canMoveTo(Rectangle feet) {
        return isOnFloor(feet) && !collisionMap.intersectsObstacle(feet);
    }

    protected boolean isOnFloor(Rectangle feet) {
        return floorArea.contains(feet.getCenterX(), feet.getMaxY());
    }

    public void drawScene(Graphics graphics, Agent agent, Runnable drawAgent) {
        draw(graphics, agent);
        drawAgent.run();
    }

    public void pickUp(Agent agent) {
    }

    public void drop(Agent agent) {
    }

    public void action(Agent agent) {
    }

    protected boolean isNear(Agent agent, Rectangle area) {
        Rectangle playerArea = new Rectangle(
                agent.getX() - 20,
                agent.getY() - 20,
                agent.getWidth() + 40,
                agent.getHeight() + 40
        );

        return playerArea.intersects(area);
    }

    public void attack(Agent agent) {
        for (Enemy enemy : enemies) {
            if (enemy.isAlive() && isNear(agent, enemy.getBounds())) {
                enemy.hit();
                break;
            }
        }
    }

    /**
     * Obtiene el texto con el avance de los objetivos de la zona
     *
     * @return texto de los objetivos
     */
    public String getObjectiveText() {
        return "";
    }
}