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
                dessin.isMovingForward = true;
                break;
            case KeyEvent.VK_S:
                dessin.isMovingBackward = true;
                break;
            case KeyEvent.VK_D:
                dessin.isMovingRight = true;
                break;
            case KeyEvent.VK_Q:
                dessin.isMovingLeft = true;
                break;
            case KeyEvent.VK_E:
                dessin.isMovingUp = true;
                break;
            case KeyEvent.VK_A:
                dessin.isMovingDown = true;
                break;
            case KeyEvent.VK_ESCAPE:
                dessin.trackToggle();
                break;
        }

    }

    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_Z:
                dessin.isMovingForward = false;
                break;
            case KeyEvent.VK_S:
                dessin.isMovingBackward = false;
                break;
            case KeyEvent.VK_D:
                dessin.isMovingRight = false;
                break;
            case KeyEvent.VK_Q:
                dessin.isMovingLeft = false;
                break;
            case KeyEvent.VK_E:
                dessin.isMovingUp = false;
                break;
            case KeyEvent.VK_A:
                dessin.isMovingDown = false;
                break;
        }
    }

    public void keyTyped(KeyEvent e) {
    }



}
