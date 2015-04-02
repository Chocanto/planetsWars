package planetsWars;

import javax.media.opengl.*;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.awt.event.*;


public class Souris implements MouseMotionListener{

    GLCanvas canvas;
    Scene dessin;

    public  Souris(){
    }


    public  Souris(GLCanvas canvas, Scene dessin){
        this.canvas=canvas;
        this.dessin=dessin;
    }


    public void mouseClicked (MouseEvent me) {
    }
    public void mouseEntered (MouseEvent me) {}
    public void mousePressed (MouseEvent me) {
    }
    public void mouseReleased (MouseEvent me) {
    }
    public void mouseExited (MouseEvent me) {}

    public void mouseMoved (MouseEvent me) {
        dessin.setTheta(me.getXOnScreen());
        dessin.setPhi(me.getYOnScreen());
    }

    public void mouseDragged (MouseEvent me) {}

}
