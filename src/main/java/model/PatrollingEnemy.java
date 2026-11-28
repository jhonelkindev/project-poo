package model;

import java.awt.Rectangle;

public abstract class PatrollingEnemy extends Enemy {
    private int dirX;
    private int dirY;
    private int framesToTurn;
    private boolean fleeing;
    private long fleeEndTime;

    protected PatrollingEnemy(double x, double y, int width, int height) {
        super(x, y, width, height, 0.8);
        chooseRandomDirection();
    }

    @Override
    public void move(Agent agent, Zone zone) {
        if (!alive) {
            return;
        }

        int goX;
        int goY;
        double currentSpeed;

        if (fleeing || distanceTo(agent) < 180) {
            goX = directionTo(getX(), agent.getX());
            goY = directionTo(getY(), agent.getY() + agent.getHeight() - height);
            currentSpeed = 1.2;

            if (fleeing) {
                goX = -goX;
                goY = -goY;
                currentSpeed = 1.6;
            }
        } else {
            framesToTurn--;

            if (framesToTurn <= 0) {
                chooseRandomDirection();
            }

            goX = dirX;
            goY = dirY;
            currentSpeed = speed;
        }

        // Se revisa cada eje por separado para poder pasar junto a las máquinas.
        double nextX = x + goX * 0.85 * currentSpeed;
        double nextY = y + goY * 0.45 * currentSpeed;

        boolean canMoveX = canGo(zone, nextX, y);
        if (canMoveX) {
            x = nextX;
        }

        boolean canMoveY = canGo(zone, x, nextY);
        if (canMoveY) {
            y = nextY;
        }

        if (!canMoveX && !canMoveY) {
            chooseRandomDirection();
        }

        boolean moved = (canMoveX && goX != 0) || (canMoveY && goY != 0);
        updateImage(goX, goY, moved);
    }

    private boolean canGo(Zone zone, double nextX, double nextY) {
        Rectangle body = new Rectangle((int) nextX, (int) nextY, width, height);

        return zone.canMoveTo(getBoundsAt(nextX, nextY))
                && zone.getFloorArea().contains(body);
    }

    private void chooseRandomDirection() {
        dirX = (int) (Math.random() * 3) - 1;
        dirY = (int) (Math.random() * 3) - 1;
        framesToTurn = 60 + (int) (Math.random() * 120);
    }

    private int directionTo(int from, int to) {
        if (to > from + 2) {
            return 1;
        }

        if (to < from - 2) {
            return -1;
        }

        return 0;
    }

    private double distanceTo(Agent agent) {
        Rectangle player = agent.getBounds();
        Rectangle enemy = getBounds();

        return Math.hypot(
                player.getCenterX() - enemy.getCenterX(),
                player.getCenterY() - enemy.getCenterY()
        );
    }

    protected abstract void updateImage(int goX, int goY, boolean moved);

    @Override
    public void update() {
        if (fleeing && System.currentTimeMillis() >= fleeEndTime) {
            fleeing = false;
        }
    }

    @Override
    public void bite(Agent agent) {
        agent.loseLife();
        flee(1500);
    }

    @Override
    public void hit() {
        if (Math.random() < 0.5) {
            alive = false;
        } else {
            flee(4000);
        }
    }

    public void flee(int millis) {
        fleeing = true;
        fleeEndTime = System.currentTimeMillis() + millis;
    }

    @Override
    public boolean isDangerous() {
        return alive && !fleeing;
    }
}