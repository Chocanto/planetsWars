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
import java.awt.Robot;


public class Souris implements MouseMotionListener{

    GLCanvas canvas;
    Scene dessin;
    JFrame frame;
    Robot robot;

    public  Souris(){
    }


    public  Souris(JFrame frame, GLCanvas canvas, Scene dessin){
        this.canvas=canvas;
        this.dessin=dessin;
        this.frame=frame;
        try {
            robot = new Robot();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
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
        /*dessin.setPhi(frame.getHeight()/2 - (me.getY()+38));
        dessin.setTheta(frame.getWidth()/2 - (me.getX()+10));
        robot.mouseMove(frame.getX() + frame.getWidth()/2, frame.getY() + frame.getHeight()/2);*/

    }

    public void mouseDragged (MouseEvent me) {}

}
