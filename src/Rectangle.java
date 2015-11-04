public class Rectangle {
    private int x;
    private int y;
    public static final int WIDTH = 92;
    public static final int HEIGHT = 20;

    public Rectangle(int x, int y) {
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
        if (this.getY() > 200) {
            this.setY(this.getY() - 100);
        }
    }

    public void moveDown() {
        if (this.getY() < 200) {
            this.setY(this.getY() + 100);
        }
    }
}