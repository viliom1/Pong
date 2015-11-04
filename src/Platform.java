public class Platform {
    private int x;
    private int y;
    public static final int WIDTH = 10;
    public static final int HEIGHT = 60;

    public Platform (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public void moveUp() {
        if (this.getY() - MultiPlayer.playerOneSpeed >= 10) {
            this.setY(this.getY() - MultiPlayer.playerOneSpeed);
        }
    }

    public void moveDown() {
        if (this.getY() + MultiPlayer.playerOneSpeed <= Display.HEIGHT - 70) {
            this.setY(this.getY() + MultiPlayer.playerOneSpeed);
        }
    }
}