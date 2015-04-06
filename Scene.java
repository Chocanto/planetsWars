package planetsWars;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.awt.GLCanvas;
import java.lang.Math;
import java.nio.*;
import java.util.Scanner;
import com.jogamp.common.nio.Buffers;
import com.jogamp.common.nio.PointerBuffer;
import com.jogamp.opengl.util.texture.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.awt.PointerInfo;
import java.awt.MouseInfo;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import java.awt.Point;
import java.awt.Robot;
import com.jogamp.opengl.util.awt.TextRenderer;
import java.awt.Font;
import java.text.DecimalFormat;

public class Scene implements GLEventListener{

    private GLCanvas parent;
    private Robot robot;

    private TextRenderer textFPS;

    /**Event souris**/
    private boolean trackMouse = true;

    /**Event clavier**/
    boolean isMovingLeft = false;
    boolean isMovingRight = false;
    boolean isMovingDown = false;
    boolean isMovingUp = false;
    boolean isMovingForward = false;
    boolean isMovingBackward = false;

    private GLU glu = new GLU();
    private GLUT glut = new GLUT();
    private float acc = 0.5f;

    private float mouseSpeed = 0.002f; //vitesse de la souris

    private double R = 1.0f;
    private double phi = 1.0f;
    private double theta = 1.0f;

    private double eyeX = 20.0f;
    private double eyeY = 20.0f;
    private double eyeZ = 20.0f;
    private double delta = 0.1f;

    private double dirX;
    private double dirY;
    private double dirZ;

    float time_old=0f; //pour test perfo

    private Planet planet1;
    private Planet planet2;
    private Planet planet3;
    private Planet planet4;
    private Planet planet5;

    private Isocaedre isocaedre;

    public Scene(GLCanvas parent) {
        this.parent = parent;

        try {
            robot = new Robot();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        mouseAction(drawable);
        keyboardAction();

        GL2 gl = drawable.getGL().getGL2();
        gl.glClear (GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        //gl.glEnable(GL2.GL_CULL_FACE);

        float lmodel_ambient[]={1.f,1.f,1.f,0.0f};

        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        gl.glLoadIdentity();

        /**Lumière**/
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT,lmodel_ambient, 0);
        gl.glEnable(GL2.GL_LIGHTING);

        gl.glEnable(GL2.GL_LIGHT0);
        float dif[] = {1.0f,1.0f,0.5f,1.0f};
        gl.glLightfv(GL2.GL_LIGHT0,GL2.GL_DIFFUSE,dif, 0);

        float amb[] = {1.0f,0.0f,0.0f,0.1f};
        gl.glLightfv(GL2.GL_LIGHT0,GL2.GL_AMBIENT,amb, 0);

        float l_pos[] = { 0.0f,0.0f,0.0f,1.0f };

        dirX = R*Math.sin(phi)*Math.sin(theta);
        dirY = R*Math.cos(phi);
        dirZ = R*Math.sin(phi)*Math.cos(theta);

        glu.gluLookAt(  eyeX, eyeY, eyeZ,
                        eyeX+dirX, eyeY+dirY, eyeZ+dirZ,
                        0,1,0);
        gl.glLightfv(GL2.GL_LIGHT0,GL2.GL_POSITION,l_pos, 0);

        /**Affichage repère**/
        gl.glPushMatrix();
        repere( drawable, 5.0f);

        /**Affichage planètes**/
        planet1.display(gl);

        gl.glPopMatrix();
        gl.glPushMatrix();
        planet2.display(gl);

        planet3.display(gl);

        gl.glPopMatrix();
        gl.glPushMatrix();
        planet4.display(gl);

        gl.glPopMatrix();
        gl.glPushMatrix();
        planet5.display(gl);

        gl.glPopMatrix();
        gl.glTranslated(50, 0, 0);
        isocaedre.display(gl);

        /*
            gl.glPopMatrix();
            gl.glTexParameteri(GL2.GL_TEXTURE_2D,
                    GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D,
                    GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);

            gl.glMatrixMode(GL2.GL_TEXTURE);
            gl.glLoadIdentity();
            gl.glRotated(rot*0.1, 0., 0., 1);
            gl.glMatrixMode(GL2.GL_MODELVIEW);
*/
        /*gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        IntBuffer pignonsbuf = Buffers.newDirectIntBuffer(pignons);
        gl.glVertexPointer(3, GL2.GL_INT,0,pignonsbuf);
        gl.glDrawElements(GL2.GL_TRIANGLES,6*3,GL2.GL_UNSIGNED_INT,pignonsbuf);*/

        /**Affichage fps**/
        float time=drawable.getAnimator().getLastFPS();
        textFPS.beginRendering(drawable.getWidth(), drawable.getHeight());
        textFPS.setColor(1.0f, 1.0f, 1.0f, 0.5f);

        drawInfos(drawable);

        textFPS.draw("FPS:\n"+time, 5, 20);
        if (trackMouse)
            textFPS.draw("Tracking mouse (ESC to disable)", 5, 5);
        else {
            textFPS.setColor(1.0f, 0.2f, 0.2f, 1.0f);
            textFPS.draw("Not tracking mouse (ESC to enable)", 5, 5);
        }
        textFPS.endRendering();
        time_old=time;
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}

    @Override
    public void init(GLAutoDrawable drawable) {
        /**Augmente FPS pour tester performances**/
        drawable.getAnimator().setUpdateFPSFrames(100, null);
        drawable.getGL().setSwapInterval(0);

        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor (0f, 0f, 0f, 1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);

        textFPS = new TextRenderer(new Font("SansSerif", Font.BOLD, 11));

        //chargement des planètes
        planet1 = new Planet(25., 10., 0., 0., glu, "Textures/sun01.jpg");
        planet1.setEmission(new float[]{1f, 0.8f, 0f, 1f});

        planet2 = new Planet(3., 10., 1., 50., glu, "Textures/planet01.jpg");

        planet3 = new Planet(1., 10., 0.5, 5., glu, "Textures/planet02.jpg");

        planet4 = new Planet(5., 10., 0.5, 85., glu, "Textures/planet03.jpg");
        planet4.setEmission(new float[]{0.5f, 0f, 0f, 0.5f});

        planet5 = new Planet(8., 10., 0.2, 100., glu, "Textures/planet05.png");

        isocaedre = new Isocaedre(gl);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
                    int height) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glViewport (0, 0, width, height);
        gl.glMatrixMode (GL2.GL_PROJECTION);
        gl.glLoadIdentity ();
        glu.gluPerspective(70.0,  width/(float) height, 0.01, 3000.0);
                              gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    void fleche(GLAutoDrawable drawable,float t1,float t2,float t3,float taille,char dim){
        GL2 gl = drawable.getGL().getGL2();

        switch(dim)
        {
            case 'x':
                gl.glBegin(GL2.GL_LINE_STRIP);
                gl.glVertex3f (t1-taille, t2+taille, t3);
                gl.glVertex3f (t1, t2, t3);
                gl.glVertex3f (t1-taille, t2-taille, t3);
                gl.glEnd();
                break;
            case 'y':
                gl.glBegin(GL2.GL_LINE_STRIP);
                gl.glVertex3f (t1, t2-taille, t3+taille);
                gl.glVertex3f (t1, t2, t3);
                gl.glVertex3f (t1, t2-taille, t3-taille);
                gl.glEnd();
                break;
            case 'z':
                gl.glBegin(GL2.GL_LINE_STRIP);
                gl.glVertex3f (t1, t2+taille, t3-taille);
                gl.glVertex3f (t1, t2, t3);
                gl.glVertex3f (t1, t2-taille, t3-taille);
                gl.glEnd();
                break;
            default :;
        }
    }

    void repere(GLAutoDrawable drawable,float t) {
        // TODO Auto-generated method stub
        GL2 gl = drawable.getGL().getGL2();

        float coef=1;
        gl.glPushAttrib(GL2.GL_LIGHTING);

        gl.glDisable(GL2.GL_LIGHTING);

        gl.glLineWidth(1.0f);
        gl.glBegin(GL2.GL_LINES);
        gl.glColor3f(1.0f,0.0f,0.0f);
        gl.glVertex3f (t, 0.0f, 0.0f);
        gl.glVertex3f (-t, 0.0f, 0.0f);
        gl.glColor3f(0.0f,1.0f,0.0f);
        gl.glVertex3f ( 0.0f, 0.0f, coef*t);
        gl.glVertex3f ( 0.0f, 0.0f, -t*coef);
        gl.glColor3f(0.0f,1.0f,1.0f);
        gl.glVertex3f ( 0.0f, t, 0.0f);
        gl.glVertex3f ( 0.0f, -t, 0.0f);
        gl.glEnd();
        gl.glColor3f(1.0f,0.0f,0.0f);
        fleche(drawable,t, 0.0f, 0.0f,t*0.1f ,'x');
        gl.glColor3f(0.0f,1.0f,0.0f);
        fleche(drawable,0.0f, 0.0f, coef*t,t*0.1f ,'z');
        gl.glColor3f(0.0f,1.0f,1.0f);
        fleche(drawable,0.0f, t, 0.0f,t*0.1f ,'y');
        gl.glLineWidth(0.1f);
        gl.glPopAttrib();

    }

    public void drawInfos(GLAutoDrawable drawable) {
        textFPS.draw("eyeX: "+new DecimalFormat("#.###").format(eyeX),
                    5, drawable.getHeight()-15);
        textFPS.draw("eyeY: "+new DecimalFormat("#.###").format(eyeY),
                    5, drawable.getHeight()-30);
        textFPS.draw("eyeZ: "+new DecimalFormat("#.###").format(eyeZ),
                    5, drawable.getHeight()-45);
    }

    public void moveEyeX(float a) {
       theta += a;
    }

    public void moveEyeY(float a) {
        phi += a;
    }

    public void setTheta(float a) {
        theta += a * mouseSpeed;
    }

    public void setPhi(float a) {
        phi -= a * mouseSpeed;
        if (phi > 3.14)
            phi = 3.14;
        else if (phi < 0)
            phi = 0;
    }

    /**Méthodes de déplacement au clavier**/
    public void forward() {
        eyeX += acc*Math.sin(phi)*Math.sin(theta);
        eyeY += acc*Math.cos(phi);
        eyeZ += acc*Math.sin(phi)*Math.cos(theta);
    }

    public void backward() {
        eyeX -= acc*Math.sin(phi)*Math.sin(theta);
        eyeY -= acc*Math.cos(phi);
        eyeZ -= acc*Math.sin(phi)*Math.cos(theta);
    }

    public void moveRight() {
        eyeX += acc*Math.sin(theta-1.570796);
        eyeZ += acc*Math.cos(theta-1.570796);
    }

    public void moveLeft() {
        eyeX += acc*Math.sin(theta+1.570796);
        eyeZ += acc*Math.cos(theta+1.570796);
    }

    public void moveUp() {
        eyeX += acc*Math.sin(phi-1.570796)*Math.sin(theta);
        eyeY += acc*Math.cos(phi-1.570796);
        eyeZ += acc*Math.sin(phi-1.570796)*Math.cos(theta);
    }

    public void moveDown() {
        eyeX += acc*Math.sin(phi+1.570796)*Math.sin(theta);
        eyeY += acc*Math.cos(phi+1.570796);
        eyeZ += acc*Math.sin(phi+1.570796)*Math.cos(theta);
    }

    public void trackToggle() {
        trackMouse = !trackMouse;
    }

    public void mouseAction(GLAutoDrawable drawable) {
        if (trackMouse) {
            Point mouseLocation;
            mouseLocation = MouseInfo.getPointerInfo().getLocation();

            SwingUtilities.convertPointFromScreen(mouseLocation, parent);

            setPhi(drawable.getHeight()/2 - (int)mouseLocation.getY());
            setTheta(drawable.getWidth()/2 - (int)mouseLocation.getX());

            robot.mouseMove(
                (int)parent.getLocationOnScreen().getX() + drawable.getWidth()/2,
                (int)parent.getLocationOnScreen().getY() + drawable.getHeight()/2
            );
        }
    }

    public void keyboardAction() {
        if (isMovingForward)
            forward();
        if (isMovingBackward)
            backward();
        if (isMovingUp)
            moveUp();
        if (isMovingDown)
            moveDown();
        if (isMovingRight)
            moveRight();
        if (isMovingLeft)
            moveLeft();
    }

}
