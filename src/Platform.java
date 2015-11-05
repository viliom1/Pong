public class Platform {
    private int x;
    private int y;
    public static final int WIDTH = 10;
    public static final int HEIGHT = 60;
    private int speed;

    public Platform (int x, int y) {
        this.x = x;
        this.y = y;
        this.speed = 5;
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

    public int getSpeed() {return  speed;}

    public void setSpeed(int speed) {this.speed = speed;}

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public void moveUp() {
        if (this.getY() - this.getSpeed() >= 10) {
            this.setY(this.getY() - this.getSpeed());
        }
    }

    public void moveDown() {
        if (this.getY() + this.getSpeed() <= Display.HEIGHT - 70) {
            this.setY(this.getY() + this.getSpeed());
        }
    }
}