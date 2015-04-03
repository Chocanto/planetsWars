package planetsWars;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import com.jogamp.opengl.util.texture.*;

public class Planet {
    private double size;
    private double orbitalSpeed;
    private double rotationPeriod;
    private double semiMajorAxis;
    private int it;
    private GLUquadric sphere;
    private GLU glu;
    private Texture texture;
    private float[] emission = {0f, 0f, 0f, 1f};

    public Planet(double _size, double _orbitalSpeed, double _rotationPeriod, double _sma
                    , GLU _glu, String texturePath) {
        size = _size;
        orbitalSpeed = _orbitalSpeed;
        rotationPeriod = _rotationPeriod;
        semiMajorAxis = _sma;
        glu = _glu;
        
        it=0;

        sphere = glu.gluNewQuadric();
        
        try {
            texture = TextureIO.newTexture(new File(texturePath), false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void display(GL2 gl) {
        texture.enable(gl);
        texture.bind(gl);
        gl.glMaterialfv(GL2.GL_FRONT,GL2.GL_EMISSION, emission, 0);
        gl.glRotated(it*rotationPeriod, 0., 1., 0.);
        gl.glTranslated(semiMajorAxis, 0., 0.);
        
        glu.gluQuadricDrawStyle(sphere, GLU.GLU_FILL);
        glu.gluQuadricTexture(sphere, true);
        glu.gluQuadricNormals(sphere, GLU.GLU_SMOOTH);
        gl.glColor3d(1.0, 1.0, 1.0);
        glu.gluSphere(sphere, size, 20, 20);

        it++;
    }

    public void setEmission(float[] em) {
        emission = em;
    }

}
