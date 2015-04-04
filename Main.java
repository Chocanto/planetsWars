package planetsWars;

import javax.media.opengl.*;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.Animator;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.Point;

public class Main extends JFrame{

    private static final long serialVersionUID = 1L;

    public Main() {
        setTitle("PlanetsWars");
        setSize(500, 500);

        //Masquer la souris

        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                    cursorImg, new Point(0, 0), "blank cursor");
        getContentPane().setCursor(blankCursor);

        final GLCanvas canvas = new GLCanvas ();
        final Scene scene = new Scene(canvas);
        final Clavier keyListener=new Clavier(canvas,scene);
        //final Souris mouseListener=new Souris(this, canvas,scene);
        final FPSAnimator anim = new FPSAnimator(canvas,60);

        canvas.addGLEventListener(scene);
        canvas.addKeyListener(keyListener);
        //canvas.addMouseMotionListener(mouseListener);
        this.getContentPane().add(canvas);
        anim.start();
    }

    public static void main(String [] args){
        new Main().setVisible(true);
    }
}
