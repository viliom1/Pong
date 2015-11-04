import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInput implements KeyListener {

    class Key{
        private boolean pressed = false;
        public boolean isPressed(){
            return pressed;
        }
        public void toggle (boolean isPressed){
            pressed = isPressed;
        }
    }

    public KeyInput(Display frame){
        frame.getCanvas().addKeyListener(this);
    }
    public Key p1Up = new Key();
    public Key p2Up = new Key();
    public Key p1Down = new Key();
    public Key p2Down = new Key();
    public Key pause = new Key();
    //for StartMenu
    public Key up = new Key();
    public Key down = new Key();
    public Key choseMode = new Key();


    public void keyTyped(KeyEvent e) {
    }
    public void keyPressed(KeyEvent e) {
        toggleKey(e.getKeyCode(),true);
    }
    public void keyReleased(KeyEvent e) {
        toggleKey(e.getKeyCode(),false);
    }
    public void toggleKey(int keyCode, boolean isPressed){
        if (keyCode == KeyEvent.VK_Q){
            p1Up.toggle(isPressed);
        }
        if (keyCode == KeyEvent.VK_A) {
            p1Down.toggle(isPressed);
        }
        if (keyCode == KeyEvent.VK_P){
            p2Up.toggle(isPressed);
        }
        if (keyCode == KeyEvent.VK_L){
            p2Down.toggle(isPressed);
        }
        if (keyCode == KeyEvent.VK_ESCAPE){
            // Not implemented yet
            pause.toggle(isPressed);
        }
        if (keyCode == KeyEvent.VK_UP){
            up.toggle(isPressed);
        }
        if (keyCode == KeyEvent.VK_DOWN){
            down.toggle(isPressed);
        }
        if (keyCode == KeyEvent.VK_ENTER){
            choseMode.toggle(isPressed);
        }
    }
}
