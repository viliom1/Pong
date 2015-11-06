import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class GameOverMenu extends Canvas implements Runnable {

    /* declaration of the Display class which holds JFrame and Canvas initializations */
    Display frame = new Display(NAME);
    BufferStrategy bs;
    public boolean running = false;
    public int tickCount = 0;

    /* Game constants and states */
    private static final long serialVersionUID = 1L;
    public static final String NAME = "Pong";

    /* Local variables*/
    public static boolean isPaused = false;

    /* Buffer */
    private BufferedImage image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    /* KeyInput object */
    public KeyInput input = new KeyInput(frame);

    private Rectangle rectangle = new Rectangle(366, 185);

    public GameOverMenu() {
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
            if (rectangle.getY() < 200){
                StartMenu startMenu = new StartMenu().start();
                running = false;
                frame.dispouse();
            }
            else{
                System.exit(0);
            }
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
        g.drawString("GAME OVER", 379, 70);

        g.setColor(Color.WHITE);
        g.drawString("Your Score is " + SurvivalMode.getScore(), 372, 120);

        g.setColor(Color.WHITE);
        g.drawString("New Game", 383, 200);

        g.setColor(Color.WHITE);
        g.drawString("Exit", 404, 300);

        g.setColor(Color.WHITE);
        g.drawRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());

        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        new GameOverMenu().start();
    }

    public synchronized GameOverMenu start() {
        running = true;
        new Thread(this).start();
        return null;
    }
}