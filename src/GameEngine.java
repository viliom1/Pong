
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class GameEngine extends Canvas implements Runnable{

    private static final long serialVersionUID = 1L;
    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;
    //public static final int SCALE = 3;
    public static final String NAME = "GAME";

    private JFrame frame;
    private Platform platformOne = new Platform(10, 130);
    private Platform platformTwo = new Platform(390, 130);

    public boolean running = false;
    public int tickCount = 0;

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

    public GameEngine() {
        //setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        //setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        //setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        setMinimumSize(new Dimension(WIDTH, HEIGHT ));
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
        double nsPerTick = 1_000_000_000D/60D;

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
                delta -=1;
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
                System.out.println("tick count, fps: " + frames + "," + ticks);
                frames = 0;
                ticks = 0;
            }
        }
    }

    public void tick(){
        tickCount++;

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
        g.fillOval(WIDTH / 2, HEIGHT / 2, 5, 5);

        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        new GameEngine().start();
    }

    public synchronized void start() {
        running = true;
        new Thread(this).start();
    }

    public synchronized void stop() {
        running = false;
    }
}