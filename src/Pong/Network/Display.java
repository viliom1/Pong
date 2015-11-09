package Pong.Network;

import javax.swing.*;
import java.awt.*;

public class Display {
    private String title;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private JFrame frame;
    private Canvas canvas;

    public Display(String title) {
        init(title);
    }

    private void init(String title) {
        /* initialize JFrame with constant dimensions */
        this.frame = new JFrame(title);
        //this.frame.setFocusable(true);
        this.frame.setResizable(false);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* initialize Canvas to add to the frame */
        this.canvas = new Canvas();
        this.canvas.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        this.canvas.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        this.canvas.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        /* set layout and add the canvas to the frame */
        this.frame.setLayout(new BorderLayout());
        this.frame.add(this.getCanvas(), BorderLayout.CENTER);

        /* pack the frame and set it to visible */
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
    }

    public Canvas getCanvas() {
        return this.canvas;
    }

    public int getHeight() {
        return this.HEIGHT;
    }

    public int getWidth() {
        return this.WIDTH;
    }

    public void dispouse(){
        frame.dispose();
    }

}
