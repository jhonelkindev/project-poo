package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyboardController extends KeyAdapter {
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
    private boolean nextLevel;
    private boolean action;
    private boolean pickUp;
    private boolean drop;
    private boolean pickUpHeld;
    private boolean dropHeld;
    private boolean attack;
    private boolean attackHeld;

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) up = true;
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) left = true;
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) down = true;
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) right = true;

        if (code == KeyEvent.VK_N) {
            nextLevel = true;
        }

        if (code == KeyEvent.VK_E) {
            action = true;
        }

        if (code == KeyEvent.VK_F && !pickUpHeld) {
            pickUp = true;
            pickUpHeld = true;
        }

        if (code == KeyEvent.VK_Q && !dropHeld) {
            drop = true;
            dropHeld = true;
        }

        if (code == KeyEvent.VK_SPACE && !attackHeld) {
            attack = true;
            attackHeld = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) up = false;
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) left = false;
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) down = false;
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) right = false;

        if (code == KeyEvent.VK_N) {
            nextLevel = false;
        }

        if (code == KeyEvent.VK_E) {
            action = false;
        }

        if (code == KeyEvent.VK_F) {
            pickUpHeld = false;
        }

        if (code == KeyEvent.VK_Q) {
            dropHeld = false;
        }


        if (code == KeyEvent.VK_SPACE) {
            attackHeld = false;
        }
    }

    public boolean isUp() {
        return up;
    }

    public boolean isDown() {
        return down;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }


    public boolean isNextLevel() {
        return nextLevel;
    }

    public boolean isAction() {
        return action;
    }

    public boolean consumePickUp() {
        boolean pressed = pickUp;
        pickUp = false;
        return pressed;
    }

    public boolean consumeDrop() {
        boolean pressed = drop;
        drop = false;
        return pressed;
    }

    /**
     * Indica si se presiono la tecla de espacio para acatar
     *
     * @return true en el fotograma donde se presiono
     */
    public boolean consumeAttack() {
        boolean pressed = attack;
        attack = false;
        return pressed;
    }
}
