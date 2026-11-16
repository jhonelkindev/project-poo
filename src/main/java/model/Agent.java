package model;

public class Agent extends Entity {
    public static final int MAX_LIVES = 3;

    private String playerName;
    private int lives;
    private int score;

    public Agent(double x, double y) {
        super(x, y, 48, 64, 4.0);
        this.playerName = "Jugador";
        this.lives = MAX_LIVES;
        this.score = 0;
    }

    @Override
    public void update() {

    }

    public void move(int dx, int dy, int maxWidth, int masHeight) {
        double nextX = x + dx * speed;
        double nextY = y + dy * speed;

        if (nextX >= 0 && nextX <= maxWidth - width) {
            this.x = nextX;
        }
        if (nextY >= 0 && nextY <= masHeight - height) {
            this.y = nextY;
        }
    }

    public void takeDamage() {
        if (lives > 0) {
            lives--;
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
