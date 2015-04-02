package planetsWars;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

public class Planet {
    private double size;
    private double orbitalSpeed;
    private double rotationPeriod;
    private double semiMajorAxis;
    private int it;
    private GLUquadric sphere;
    private GLU glu;

    public Planet(double _size, double _orbitalSpeed, double _rotationPeriod, double _sma
                    , GLU _glu) {
        size = _size;
        orbitalSpeed = _orbitalSpeed;
        rotationPeriod = _rotationPeriod;
        semiMajorAxis = _sma;
        glu = _glu;
        
        it=0;

        sphere = glu.gluNewQuadric();

    }

    public void display(GL2 gl) {
        gl.glRotated(it*rotationPeriod, 0., 1., 0.);
        gl.glTranslated(semiMajorAxis, 0., 0.);
        
        glu.gluQuadricDrawStyle(sphere, GLU.GLU_FILL);
        glu.gluQuadricTexture(sphere, true);
        glu.gluQuadricNormals(sphere, GLU.GLU_SMOOTH);
        gl.glColor3d(1.0, 1.0, 1.0);
        glu.gluSphere(sphere, size, 20, 20);

        it++;
    }

}
