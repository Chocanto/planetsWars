package planetsWars;

import javax.media.opengl.GL2;

public class Material {
    protected float[] ambient;
    protected float[] emission;
    protected float[] diffuse;
    protected float[] specular;
    protected float[] shininess;

    public Material() {
        ambient = new float[3];
        emission = new float[3];
        specular = new float[3];
        diffuse = new float[3];
        shininess = new float[3];
    }

    public void setAmbient(float[] am) {
        ambient = am;
    }

    public void setEmission(float[] em) {
        emission = em;
    }

    public void setDiffuse(float[] dif) {
        diffuse = dif;
    }

    public void setSpecular(float[] spe) {
        specular = spe;
    }

    public void setShininess(float[] shi) {
        shininess = shi;
    }

    public void displayMaterial(GL2 gl) {
        gl.glMaterialfv(GL2.GL_FRONT,GL2.GL_EMISSION, emission, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, shininess, 0);
    }
}
