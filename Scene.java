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

public class Scene implements GLEventListener{

    private GLU glu = new GLU();
    private GLUT glut = new GLUT();
    private float rot = 0f;
    private float rotAcc = 0.1f;

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
    
    private Texture texture;

    float time_old=0f; //pour test perfo

    private Planet planet1; 
    private Planet planet2;
    private Planet planet3;

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear (GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        float lmodel_ambient[]={0.70f,0.70f,0.70f,1.0f}; 

        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        
        gl.glLoadIdentity();

        /**Lumière**/
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT,lmodel_ambient, 0); 
        gl.glEnable(GL2.GL_LIGHTING);

        gl.glEnable(GL2.GL_LIGHT0);
        float dif[] = {1.0f,1.0f,0.0f,1.0f};
        gl.glLightfv(GL2.GL_LIGHT0,GL2.GL_DIFFUSE,dif, 0);

        float amb[] = {1.0f,0.0f,0.0f,1.0f};
        gl.glLightfv(GL2.GL_LIGHT0,GL2.GL_AMBIENT,amb, 0);

        float l_pos[] = { 10.0f,10.0f,10.0f,0.0f };
        gl.glLightfv(GL2.GL_LIGHT0,GL2.GL_POSITION,l_pos, 0);



        dirX = R*Math.sin(phi)*Math.sin(theta);
        dirY = R*Math.cos(phi);
        dirZ = R*Math.sin(phi)*Math.cos(theta);

        glu.gluLookAt(  eyeX, eyeY, eyeZ,
                        eyeX+dirX, eyeY+dirY, eyeZ+dirZ,
                        0,1,0);
        gl.glPushMatrix();
        repere( drawable, 5.0f);

        planet1.display(gl);
        
        gl.glPopMatrix();
        planet2.display(gl);
        
        gl.glPopMatrix();
        planet3.display(gl);



        rot += rotAcc;
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

        /**Affichage fps**/
        float time=drawable.getAnimator().getLastFPS();
        //if (time!=time_old) System.out.println("Framerate: "+time + "timeold: "+time_old);
        //    time_old=time;
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
        /*try {
            texture = TextureIO.newTexture(new File("./briques.png"), false);
            texture.enable(gl);
            texture.bind(gl);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //chargement des planètes
        planet1 = new Planet(10., 10., 0., 0., glu);
        planet2 = new Planet(3., 10., 1., 20., glu);
        planet3 = new Planet(1., 10., 0.5, 5., glu);
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

    public void addZoom(float a) {
        this.R += a;
    }

    public void addRotAcc(float a) {
        this.rotAcc += a;
    }

    public void moveEyeX(float a) {
       theta += a; 
    }

    public void moveEyeY(float a) {
        phi += a;
    }

    public void setTheta(float a) {
        a *= 0.005;
        if (theta + a <= 90 || theta + a >= -90)
            theta = -a;
    }
    
    public void setPhi(float a) {
        phi = a*0.005;
    }

    /**Méthodes de déplacement au clavier**/
    public void forward() {
        eyeX = eyeX+dirX;
        eyeY = eyeY+dirY;
        eyeZ = eyeZ+dirZ;
    }
    
    public void backward() {
        eyeX = eyeX-dirX;
        eyeY = eyeY-dirY;
        eyeZ = eyeZ-dirZ;
    }

    public void moveRight() {
    }
    
    public void moveLeft() {
    }
    
    public void moveUp() {
    }
    
    public void moveDown() {
    }

}

