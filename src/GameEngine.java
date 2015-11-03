import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class GameEngine extends Canvas implements Runnable {

    private static final long serialVersionUID = 1L;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    //public static final int SCALE = 3;
    public static final String NAME = "Pong";

    private JFrame frame;
    private Platform platformOne = new Platform(10, 120);
    private Platform platformTwo = new Platform(790, 120);
    private Pong pong = new Pong(WIDTH / 2, HEIGHT / 2);

    public boolean running = false;
    public int tickCount = 0;

    public int playerOneScore = 0;
    public int playerTwoScore = 0;
    public String printScore = playerOneScore + " : " + playerTwoScore;

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    public KeyInput input = new KeyInput(this);

    public GameEngine() {
        //setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        //setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        //setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setMaximumSize(new Dimension(WIDTH, HEIGHT));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        frame = new JFrame(NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.add(this, BorderLayout.CENTER);
        frame.pack();

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1_000_000_000D / 60D;

        int ticks = 0;
        int frames = 0;

        long lastTimer = System.currentTimeMillis();
        double delta = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean shoudRender = true;
            while (delta >= 1) {
                ticks++;
                tick();
                delta -= 1;
                shoudRender = true;
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            if (shoudRender) {
                frames++;
                render();
            }

            if (System.currentTimeMillis() - lastTimer >= 1000) {
                lastTimer += 1000;
                System.out.println("tick count: " + ticks + ", fps: " + frames);
                frames = 0;
                ticks = 0;
            }
        }
    }

    public void tick() {
        tickCount++;
        if (input.p1Up.isPressed()){
            moveP1Up();
        }
        if (input.p1Down.isPressed()){
            moveP1Down();
        }
        if (input.p2Up.isPressed()){
            moveP2Up();
        }
        if (input.p2Down.isPressed()){
            moveP2Down();
        }

        movePong();

        Ai();

        printScore = playerOneScore + " : " + playerTwoScore;


        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = i * tickCount;
        }
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        //background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        //draw platform one
        g.setColor(Color.WHITE);
        g.fillRect(platformOne.getX(), platformOne.getY(), platformOne.getWidth(), platformOne.getHeight());

        //draw platform two
        g.setColor(Color.WHITE);
        g.fillRect(platformTwo.getX(), platformTwo.getY(), platformTwo.getWidth(), platformTwo.getHeight());

        //draw pong
        g.setColor(Color.WHITE);
        g.fillOval(pong.getX(), pong.getY(), pong.getWidth(), pong.getHeight());

        g.setColor(Color.WHITE);
        g.drawString(printScore, WIDTH / 2 - (printScore.length() / 2), 10);

        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        new GameEngine().start();
    }

    public synchronized GameEngine start() {
        running = true;
        new Thread(this).start();

        return null;
    }

    public synchronized void stop() {
        running = false;
    }

    public void moveP1Up() {
        if (platformOne.getY() - 5 >= 10) {
            platformOne.setY(platformOne.getY() - 5);
        }
    }

    public void moveP1Down() {
        if (platformOne.getY() + 5 <= 540) {
            platformOne.setY(platformOne.getY() + 5);
        }
    }
    public void moveP2Up() {
        if (platformTwo.getY() - 3>= 10) {
            platformTwo.setY(platformTwo.getY() - 3);
        }
    }

    public void moveP2Down() {
        if (platformTwo.getY() + 3 <= 540) {
            platformTwo.setY(platformTwo.getY() + 3);
        }
    }



    public void Ai(){

        int pongYFinal = getPongYFinal(pong.getX(), pong.getY());

        if(pongYFinal > platformTwo.getY() + platformTwo.getHeight() && (IsMoveRight || IsMoveRightUp || IsMoveRightDown)){
            moveP2Down();
        }
        if(pongYFinal < platformTwo.getY() && (IsMoveRight || IsMoveRightUp || IsMoveRightDown)){
            moveP2Up();
        }

    }

    private int getPongYFinal(int x, int y) {
        int tempX = x;
        int tempY = y;
        if(IsMoveRight){
            return pong.getY();
        }
        if(IsMoveRightUp){
            while(tempX < WIDTH && tempY > 0){
                tempX++;
                tempY--;
            }
            return tempY;
        }
        if (IsMoveRightDown){
            while(tempX < WIDTH && tempY < HEIGHT ){
                tempX++;
                tempY++;
            }
            return tempY;
        }
        return HEIGHT / 2;
    }

    //move Pong

    //Move Pong in six different directions

    boolean IsMoveRight = true;
    boolean IsMoveRightUp = false;
    boolean IsMoveRightDown = false;
    boolean IsMoveLeft = false;
    boolean IsMoveLeftUp = false;
    boolean IsMoveLeftDown = false;
    public void movePong(){
        if (IsMoveRight){
            pong.setX(pong.getX() + 5);
            if (pong.getY() >= platformTwo.getY() && pong.getY() <= platformTwo.getY() + 20 && pong.getX() == platformTwo.getX()){
                IsMoveRight = false;
                IsMoveLeftUp = true;
            }
            else if (pong.getY() >= platformTwo.getY() + 20 && pong.getY() <= platformTwo.getY() + 40 && pong.getX() == platformTwo.getX()){
                IsMoveRight = false;
                IsMoveLeft = true;
            }
            else if (pong.getY() >= platformTwo.getY() + 40 && pong.getY() <= platformTwo.getY() + 60 && pong.getX() == platformTwo.getX()){
                IsMoveRight = false;
                IsMoveLeftDown = true;
            }
            if (pong.getX() > WIDTH){
                pong.setX(WIDTH / 2);
                pong.setY(HEIGHT / 2);
                playerOneScore++;
            }
        }
        else if (IsMoveRightUp){
            pong.setX(pong.getX() + 5);
            pong.setY(pong.getY() - 5);
            if (pong.getY() >= platformTwo.getY() && pong.getY() <= platformTwo.getY() + 20 && pong.getX() == platformTwo.getX()){
                IsMoveRightUp = false;
                IsMoveLeftUp = true;
            }
            else if (pong.getY() >= platformTwo.getY() + 20 && pong.getY() <= platformTwo.getY() + 40 && pong.getX() == platformTwo.getX()){
                IsMoveRightUp = false;
                IsMoveLeft = true;
            }
            else if (pong.getY() >= platformTwo.getY() + 40 && pong.getY() <= platformTwo.getY() + 60 && pong.getX() == platformTwo.getX()){
                IsMoveRightUp = false;
                IsMoveLeftDown = true;
            }
            if (pong.getX() > WIDTH){
                pong.setX(WIDTH / 2);
                pong.setY(HEIGHT / 2);
                IsMoveRight = true;
                IsMoveRightUp = false;
                playerOneScore++;
            }
            if (pong.getY() == 0){
                IsMoveRightUp = false;
                IsMoveRightDown = true;
            }
        }
        else if (IsMoveRightDown){
            pong.setX(pong.getX() + 5);
            pong.setY(pong.getY() + 5);
            if (pong.getY() >= platformTwo.getY() && pong.getY() <= platformTwo.getY() + 20 && pong.getX() == platformTwo.getX()){
                IsMoveRightDown = false;
                IsMoveLeftUp = true;
            }
            else if (pong.getY() >= platformTwo.getY() + 20 && pong.getY() <= platformTwo.getY() + 40 && pong.getX() == platformTwo.getX()){
                IsMoveRightDown = false;
                IsMoveLeft = true;
            }
            else if (pong.getY() >= platformTwo.getY() + 40 && pong.getY() <= platformTwo.getY() + 60 && pong.getX() == platformTwo.getX()){
                IsMoveRightDown = false;
                IsMoveLeftDown = true;
            }
            if (pong.getX() > WIDTH){
                pong.setX(WIDTH / 2);
                pong.setY(HEIGHT / 2);
                IsMoveRight = true;
                IsMoveRightDown = false;
                playerOneScore++;
            }
            if (pong.getY() == HEIGHT){
                IsMoveRightDown = false;
                IsMoveRightUp = true;
            }
        }
        else if (IsMoveLeft){
            pong.setX(pong.getX() - 5);
            if (pong.getY() >= platformOne.getY() && pong.getY() <= platformOne.getY() + 20 && pong.getX() == platformOne.getX()){
                IsMoveLeft = false;
                IsMoveRightUp = true;
            }
            else if (pong.getY() >= platformOne.getY() + 20 && pong.getY() <= platformOne.getY() + 40 && pong.getX() == platformOne.getX()){
                IsMoveLeft = false;
                IsMoveRight = true;
            }
            else if (pong.getY() >= platformOne.getY() + 40 && pong.getY() <= platformOne.getY() + 60 && pong.getX() == platformOne.getX()){
                IsMoveLeft = false;
                IsMoveRightDown = true;
            }
            if (pong.getX() < 0){
                pong.setX(WIDTH / 2);
                pong.setY(HEIGHT / 2);
                playerTwoScore++;
            }
        }
        else if (IsMoveLeftUp){
            pong.setX(pong.getX() - 5);
            pong.setY(pong.getY() - 5);
            if (pong.getY() >= platformOne.getY() && pong.getY() <= platformOne.getY() + 20 && pong.getX() == platformOne.getX()){
                IsMoveLeftUp = false;
                IsMoveRightUp = true;
            }
            else if (pong.getY() >= platformOne.getY() + 20 && pong.getY() <= platformOne.getY() + 40 && pong.getX() == platformOne.getX()){
                IsMoveLeftUp = false;
                IsMoveRight = true;
            }
            else if (pong.getY() >= platformOne.getY() + 40 && pong.getY() <= platformOne.getY() + 60 && pong.getX() == platformOne.getX()){
                IsMoveLeftUp = false;
                IsMoveRightDown = true;
            }
            if (pong.getX() < 0){
                pong.setX(WIDTH / 2);
                pong.setY(HEIGHT / 2);
                IsMoveLeft = true;
                IsMoveLeftUp = false;
                playerTwoScore++;
            }
            if (pong.getY() == 0){
                IsMoveLeftUp = false;
                IsMoveLeftDown = true;
            }
        }
        else if (IsMoveLeftDown){
            pong.setX(pong.getX() - 5);
            pong.setY(pong.getY() + 5);
            if (pong.getY() >= platformOne.getY() && pong.getY() <= platformOne.getY() + 20 && pong.getX() == platformOne.getX()){
                IsMoveLeftDown = false;
                IsMoveRightUp = true;
            }
            else if (pong.getY() >= platformOne.getY() + 20 && pong.getY() <= platformOne.getY() + 40 && pong.getX() == platformOne.getX()){
                IsMoveLeftDown = false;
                IsMoveRight = true;
            }
            else if (pong.getY() >= platformOne.getY() + 40 && pong.getY() <= platformOne.getY() + 60 && pong.getX() == platformOne.getX()){
                IsMoveLeftDown = false;
                IsMoveRightDown = true;
            }
            if (pong.getX() < 0){
                pong.setX(WIDTH / 2);
                pong.setY(HEIGHT / 2);
                IsMoveLeft = true;
                IsMoveLeftDown = false;
                playerTwoScore++;
            }
            if (pong.getY() == HEIGHT){
                IsMoveLeftDown = false;
                IsMoveLeftUp = true;
            }
        }
    }
}