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
    private Material material;

    public Planet(double _size, double _orbitalSpeed, double _rotationPeriod, double _sma
                    , GLU _glu, String texturePath) {
        size = _size;
        orbitalSpeed = _orbitalSpeed;
        rotationPeriod = _rotationPeriod;
        semiMajorAxis = _sma;
        glu = _glu;

        material = new Material();

        it=0;

        sphere = glu.gluNewQuadric();

        try {
            texture = TextureIO.newTexture(new File(texturePath), false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        material.setAmbient(new float[]{
            0.1f, 0.1f, 0.1f
        });
        material.setDiffuse(new float[]{
            0.5f, 0.5f, 0.5f
        });
        material.setSpecular(new float[]{
            0.2f, 0.2f, 0.2f
        });
        material.setShininess(new float[]{
            0.5f, 0.5f, 0.5f
        });

    }

    public void display(GL2 gl) {
        texture.enable(gl);
        texture.bind(gl);
        gl.glRotated(it*rotationPeriod, 0., 1., 0.);
        gl.glTranslated(semiMajorAxis, 0., 0.);

        material.displayMaterial(gl);

        glu.gluQuadricDrawStyle(sphere, GLU.GLU_FILL);
        glu.gluQuadricTexture(sphere, true);
        glu.gluQuadricNormals(sphere, GLU.GLU_SMOOTH);
        gl.glColor3d(1.0, 1.0, 1.0);
        glu.gluSphere(sphere, size, 30, 30);

        it++;
    }

    public Material getMaterial() {
        return material;
    }
}
