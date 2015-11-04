import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

public class StartMenu extends Canvas implements Runnable {

    /* declaration of the Display class which holds JFrame and Canvas initializations */
    DisplayStartMenu frame = new DisplayStartMenu(NAME);
    BufferStrategy bs;
    public boolean running = false;
    public int tickCount = 0;

    /* Game constants and states */
    private static final long serialVersionUID = 1L;
    public static final String NAME = "StartMenu";

    /* Local variables*/
    public int pongHold = 80;
    public int playerOneScore = 0;
    public int playerTwoScore = 0;
    public int aiSpeed = 1;
    public static int playerOneSpeed = 5;
    public int pongSpeed = 5;
    public String printScore = playerOneScore + " : " + playerTwoScore;
    public static boolean isPaused = false;

    /* Buffer */
    private BufferedImage image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    /* KeyInput object */
    public KeyInput input = new KeyInput(frame);

    private Rectangle rectangle = new Rectangle(366, 185);

    public StartMenu() {
        // constructor
    }

    public void run() {
        while (!isPaused) {
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
    }

    public void tick() {
        tickCount++;

        if (input.up.isPressed()){
            rectangle.moveUp();
        }
        if (input.down.isPressed()){
            rectangle.moveDown();
        }
        if (input.choseMode.isPressed()){
            GameEngine gameEngine = new GameEngine().start();
            running = false;
        }

        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = i * tickCount;
        }
    }

    public void render() {
        this.bs = this.frame.getCanvas().getBufferStrategy();
        if (this.bs == null) {
            this.frame.getCanvas().createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        //background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, frame.getWidth(), frame.getHeight());

        g.setColor(Color.WHITE);
        g.drawString("Single Player", 377, 200);

        g.setColor(Color.WHITE);
        g.drawString("Multi Player", 383, 300);

        g.setColor(Color.WHITE);
        g.drawRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());

        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        new GameEngine().start();
    }

    public synchronized StartMenu start() {
        running = true;
        new Thread(this).start();
        return null;
    }
}