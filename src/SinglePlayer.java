import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

public class SinglePlayer extends Canvas implements Runnable {

    /* declaration of the Display class which holds JFrame and Canvas initializations */
    Display frame = new Display(NAME);
    BufferStrategy bs;
    public boolean running = false;
    public int tickCount = 0;

    /* Game constants and states */
    private static final long serialVersionUID = 1L;
    public static final String NAME = "Pong";

    /* Game objects */
    public Platform platformOne = new Platform(10, (frame.getHeight() / 2) - (Platform.HEIGHT / 2));
    public Platform platformTwo = new Platform(frame.getWidth() - 10 - Platform.WIDTH, (frame.getHeight() / 2) - (Platform.HEIGHT / 2));
    private Pong pong = new Pong(frame.getWidth() / 2, frame.getHeight() / 2);

    /* Local variables*/
    public int pongHold = 80;
    public int playerOneScore = 0;
    public int playerTwoScore = 0;
    public int aiSpeed = 1;
    public static int playerOneSpeed = 5;
    public int pongSpeed = 5;
    public String printScore = playerOneScore + " : " + playerTwoScore;
    public static boolean isPaused = false;
    public static int pauseDelay = 0;
    public static String pauseMessage = "-Game Paused-";

    /* Buffer */
    private BufferedImage image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    /* KeyInput object */
    public KeyInput input = new KeyInput(frame);

    public SinglePlayer() {
        // constructor
    }

    public void run() {
        while (true) {
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
        if(pauseDelay > 0) {
            pauseDelay--;
        }

        if (input.pause.isPressed() && !(pauseDelay > 0)) {
            isPaused = !isPaused;
            pauseDelay = 30;
        }

        if(!isPaused) {
            platformOne.setSpeed(playerOneSpeed);
            platformTwo.setSpeed(aiSpeed);

            if (input.p1Up.isPressed() && !isPaused) {
                platformOne.moveUp();
            }
            if (input.p1Down.isPressed() && !isPaused) {
                platformOne.moveDown();
            }

            if (pongHold > 0) {
                pongHold--;
            }
            if (hasBeenOffset) {
                offset = offsetArray[(generator.nextInt(12))];
                hasBeenOffset = false;
            }

            Ai(offset);
            movePong();

            printScore = playerOneScore + " : " + playerTwoScore;

            for (int i = 0; i < pixels.length; i++) {
                pixels[i] = i * tickCount;
            }
        }
    }

    public void render() {
        this.bs = this.frame.getCanvas().getBufferStrategy();
        if (this.bs == null) {
            this.frame.getCanvas().createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        if(!isPaused) {
            //background
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, frame.getWidth(), frame.getHeight());

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
            g.drawString(printScore, frame.getWidth() / 2 - (printScore.length() / 2), 10);
        } else{
            g.setColor(Color.WHITE);
            g.drawString(pauseMessage, frame.getWidth() / 2 - (pauseMessage.length() + 17), 25);
        }

        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        new MultiPlayer().start();
    }

    public synchronized SinglePlayer start() {
        running = true;
        new Thread(this).start();
        return null;
    }

    public synchronized void stop() {
        running = false;
    }


    int[] offsetArray = {-5,-10,-15,-20,-25,-30,-35,0,5,10,15,20,25,30,35};
    Random generator = new Random();
    int offset = offsetArray[(generator.nextInt(15))];
    boolean hasBeenOffset = false;

    public void Ai(int offset){

        int pongYFinal = getPongYFinal(pong.getX(), pong.getY()) + offset;

        int platformMin = 0;
        int platformMax = platformTwo.getHeight();
        int randomDistance = platformMin + (int)(Math.random() * ((platformMax  - platformMin) + 1));
        if (!isPaused) {
            if (pongYFinal > platformTwo.getY() + randomDistance && (IsMoveRight || IsMoveRightUp || IsMoveRightDown) && pong.getX() > frame.getWidth() / 4) {

                platformTwo.moveDown();

            }
            if (pongYFinal < (platformTwo.getY() + platformTwo.getHeight()) - randomDistance && (IsMoveRight || IsMoveRightUp || IsMoveRightDown) && pong.getX() > frame.getWidth() / 2) {

                platformTwo.moveUp();

            }
        }

    }

    private int getPongYFinal(int x, int y) {
        int tempX = x;
        int tempY = y;
        if (!isPaused) {
            if (IsMoveRight) {
                return pong.getY();
            }
            if (IsMoveRightUp) {
                while (tempX < frame.getWidth() && tempY > 0) {
                    tempX++;
                    tempY--;
                }
                return tempY;
            }
            if (IsMoveRightDown) {
                while (tempX < frame.getWidth() && tempY < frame.getHeight()) {
                    tempX++;
                    tempY++;
                }
                return tempY;
            }
        }
        return frame.getHeight() / 2;
    }


    //Move Pong in six different directions
    boolean IsMoveRight = true;
    boolean IsMoveRightUp = false;
    boolean IsMoveRightDown = false;
    boolean IsMoveLeft = false;
    boolean IsMoveLeftUp = false;
    boolean IsMoveLeftDown = false;
    public void movePong(){
        // necessary temporary speed variable in order to control it without changing pongSpeed which remains constant trough the whole round
        int tempSpeed = pongSpeed;
        if(pongHold <= 0) {
            if (IsMoveRight) {
                while ((pong.getX() < platformTwo.getX()) && ((pong.getX() + tempSpeed) > platformTwo.getX())){
                    tempSpeed--;
                }
                pong.setX(pong.getX() + tempSpeed);
                if (pong.getY() >= platformTwo.getY() && pong.getY() <= platformTwo.getY() + 20 && pong.getX() == platformTwo.getX()) {
                    IsMoveRight = false;
                    IsMoveLeftUp = true;
                    hasBeenOffset = true;
                } else if (pong.getY() >= platformTwo.getY() + 20 && pong.getY() <= platformTwo.getY() + 40 && pong.getX() == platformTwo.getX()) {
                    IsMoveRight = false;
                    IsMoveLeft = true;
                    hasBeenOffset = true;

                } else if (pong.getY() >= platformTwo.getY() + 40 && pong.getY() <= platformTwo.getY() + 60 && pong.getX() == platformTwo.getX()) {
                    IsMoveRight = false;
                    IsMoveLeftDown = true;
                    hasBeenOffset = true;

                }
                if (pong.getX() > frame.getWidth()) {
                    pong.setX(frame.getWidth() / 2);
                    pong.setY(frame.getHeight() / 2);
                    playerOneScore++;
                    aiSpeed++;
                    if (playerOneScore % 3 == 0) {
                        pongSpeed++;
                        playerOneSpeed += 1;
                    }
                    pongHold = 80;
                    hasBeenOffset = true;

                }
            } else if (IsMoveRightUp) {
                while ((pong.getX() < platformTwo.getX()) && ((pong.getX() + tempSpeed) > platformTwo.getX())){
                    tempSpeed--;
                }
                pong.setX(pong.getX() + tempSpeed);
                pong.setY(pong.getY() - tempSpeed);
                if (pong.getY() >= platformTwo.getY() && pong.getY() <= platformTwo.getY() + 20 && pong.getX() == platformTwo.getX()) {
                    IsMoveRightUp = false;
                    IsMoveLeftUp = true;
                    hasBeenOffset = true;

                } else if (pong.getY() >= platformTwo.getY() + 20 && pong.getY() <= platformTwo.getY() + 40 && pong.getX() == platformTwo.getX()) {
                    IsMoveRightUp = false;
                    IsMoveLeft = true;
                    hasBeenOffset = true;

                } else if (pong.getY() >= platformTwo.getY() + 40 && pong.getY() <= platformTwo.getY() + 60 && pong.getX() == platformTwo.getX()) {
                    IsMoveRightUp = false;
                    IsMoveLeftDown = true;
                    hasBeenOffset = true;

                }
                if (pong.getX() > frame.getWidth()) {
                    pong.setX(frame.getWidth() / 2);
                    pong.setY(frame.getHeight() / 2);
                    IsMoveRight = true;
                    IsMoveRightUp = false;
                    playerOneScore++;
                    aiSpeed++;
                    if (playerOneScore % 3 == 0) {
                        pongSpeed++;
                        playerOneSpeed += 1;
                    }

                    pongHold = 80;
                    hasBeenOffset = true;

                }
                if (pong.getY() <= 0) {
                    IsMoveRightUp = false;
                    IsMoveRightDown = true;


                }
            } else if (IsMoveRightDown) {
                while ((pong.getX() < platformTwo.getX()) && ((pong.getX() + tempSpeed) > platformTwo.getX())){
                    tempSpeed--;
                }
                pong.setX(pong.getX() + tempSpeed);
                pong.setY(pong.getY() + tempSpeed);
                if (pong.getY() >= platformTwo.getY() && pong.getY() <= platformTwo.getY() + 20 && pong.getX() == platformTwo.getX()) {
                    IsMoveRightDown = false;
                    IsMoveLeftUp = true;
                    hasBeenOffset = true;

                } else if (pong.getY() >= platformTwo.getY() + 20 && pong.getY() <= platformTwo.getY() + 40 && pong.getX() == platformTwo.getX()) {
                    IsMoveRightDown = false;
                    IsMoveLeft = true;
                } else if (pong.getY() >= platformTwo.getY() + 40 && pong.getY() <= platformTwo.getY() + 60 && pong.getX() == platformTwo.getX()) {
                    IsMoveRightDown = false;
                    IsMoveLeftDown = true;
                    hasBeenOffset = true;

                }
                if (pong.getX() > frame.getWidth()) {
                    pong.setX(frame.getWidth() / 2);
                    pong.setY(frame.getHeight() / 2);
                    IsMoveRight = true;
                    IsMoveRightDown = false;
                    playerOneScore++;
                    aiSpeed++;
                    if (playerOneScore % 3 == 0) {
                        pongSpeed++;
                        playerOneSpeed += 1;
                    }
                    pongHold = 80;
                    hasBeenOffset = true;

                }
                if (pong.getY() >= frame.getHeight()) {
                    IsMoveRightDown = false;
                    IsMoveRightUp = true;
                }
            } else if (IsMoveLeft) {
                while((pong.getX() > platformOne.getX()) && ((pong.getX() - tempSpeed) < platformOne.getX())){
                    tempSpeed--;
                }
                pong.setX(pong.getX() - tempSpeed);
                if (pong.getY() >= platformOne.getY() && pong.getY() <= platformOne.getY() + 20 && pong.getX() == platformOne.getX()) {
                    IsMoveLeft = false;
                    IsMoveRightUp = true;
                    hasBeenOffset = true;

                } else if (pong.getY() >= platformOne.getY() + 20 && pong.getY() <= platformOne.getY() + 40 && pong.getX() == platformOne.getX()) {
                    IsMoveLeft = false;
                    IsMoveRight = true;
                    hasBeenOffset = true;

                } else if (pong.getY() >= platformOne.getY() + 40 && pong.getY() <= platformOne.getY() + 60 && pong.getX() == platformOne.getX()) {
                    IsMoveLeft = false;
                    IsMoveRightDown = true;
                    hasBeenOffset = true;

                }
                if (pong.getX() < 0) {
                    pong.setX(frame.getWidth() / 2);
                    pong.setY(frame.getHeight() / 2);
                    playerTwoScore++;
                    pongHold = 80;
                    hasBeenOffset = true;

                }
            } else if (IsMoveLeftUp) {
                while((pong.getX() > platformOne.getX()) && ((pong.getX() - tempSpeed) < platformOne.getX())){
                    tempSpeed--;
                }
                pong.setX(pong.getX() - tempSpeed);
                pong.setY(pong.getY() - tempSpeed);
                if (pong.getY() >= platformOne.getY() && pong.getY() <= platformOne.getY() + 20 && pong.getX() == platformOne.getX()) {
                    IsMoveLeftUp = false;
                    IsMoveRightUp = true;
                    hasBeenOffset = true;

                } else if (pong.getY() >= platformOne.getY() + 20 && pong.getY() <= platformOne.getY() + 40 && pong.getX() == platformOne.getX()) {
                    IsMoveLeftUp = false;
                    IsMoveRight = true;
                    hasBeenOffset = true;

                } else if (pong.getY() >= platformOne.getY() + 40 && pong.getY() <= platformOne.getY() + 60 && pong.getX() == platformOne.getX()) {
                    IsMoveLeftUp = false;
                    IsMoveRightDown = true;
                    hasBeenOffset = true;

                }
                if (pong.getX() < 0) {
                    pong.setX(frame.getWidth() / 2);
                    pong.setY(frame.getHeight() / 2);
                    IsMoveLeft = true;
                    IsMoveLeftUp = false;
                    playerTwoScore++;
                    pongHold = 80;
                    hasBeenOffset = true;

                }
                if (pong.getY() <= 0) {
                    IsMoveLeftUp = false;
                    IsMoveLeftDown = true;
                }
            } else if (IsMoveLeftDown) {
                while((pong.getX() > platformOne.getX()) && ((pong.getX() - tempSpeed) < platformOne.getX())){
                    tempSpeed--;
                }
                pong.setX(pong.getX() - tempSpeed);
                pong.setY(pong.getY() + tempSpeed);
                if (pong.getY() >= platformOne.getY() && pong.getY() <= platformOne.getY() + 20 && pong.getX() == platformOne.getX()) {
                    IsMoveLeftDown = false;
                    IsMoveRightUp = true;
                    hasBeenOffset = true;

                } else if (pong.getY() >= platformOne.getY() + 20 && pong.getY() <= platformOne.getY() + 40 && pong.getX() == platformOne.getX()) {
                    IsMoveLeftDown = false;
                    IsMoveRight = true;
                    hasBeenOffset = true;

                } else if (pong.getY() >= platformOne.getY() + 40 && pong.getY() <= platformOne.getY() + 60 && pong.getX() == platformOne.getX()) {
                    IsMoveLeftDown = false;
                    IsMoveRightDown = true;
                    hasBeenOffset = true;

                }
                if (pong.getX() < 0) {
                    pong.setX(frame.getWidth() / 2);
                    pong.setY(frame.getHeight() / 2);
                    IsMoveLeft = true;
                    IsMoveLeftDown = false;
                    playerTwoScore++;
                    pongHold = 80;
                    hasBeenOffset = true;

                }
                if (pong.getY() >= frame.getHeight()) {
                    IsMoveLeftDown = false;
                    IsMoveLeftUp = true;
                }
            }
        }
    }
}