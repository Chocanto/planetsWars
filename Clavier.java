package planetsWars;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.media.opengl.awt.GLCanvas;

public class Clavier implements KeyListener{

    private GLCanvas canvas;
    private Scene dessin;

    public Clavier(GLCanvas canvas, Scene dessin) {
        this.canvas=canvas;
        this.dessin=dessin;
    }

    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_Z:
                dessin.forward();
                break;
            case KeyEvent.VK_S:
                dessin.backward();
                break;
            case KeyEvent.VK_D:
                dessin.moveRight();
                break;
            case KeyEvent.VK_Q:
                dessin.moveLeft();
                break;
            case KeyEvent.VK_E:
                dessin.moveUp();
                break;
            case KeyEvent.VK_A:
                dessin.moveDown();
                break;

        }

    }

    public void keyReleased(KeyEvent e) {

    }
    
    public void keyTyped(KeyEvent e) {

    }

    

}
