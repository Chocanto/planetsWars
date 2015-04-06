package planetsWars;

import javax.media.opengl.GL2;

public class Isocaedre extends VBOModel {
    private double x;
    private double y;
    private double z;
    private int it=0;
    private Material material;

    public Isocaedre(GL2 gl) {
        super(gl, 12, 20);

        material = new Material();
        material.setAmbient(new float[]{
            0.0215f, 0.1745f, 0.0215f
        });
        material.setDiffuse(new float[]{
            0.07568f, 0.61424f, 0.07568f
        });
        material.setSpecular(new float[]{
            0.633f, 0.727811f, 0.633f
        });
        material.setShininess(new float[]{
            0.6f, 0.6f, 0.6f
        });
    }

    @Override
    public void build() {
        //point le plus "bas"
        vertices[0] = 0;
        vertices[1] = 0;
        vertices[2] = 0;

        //première "couche"
        //pi/3 = 1,047197
        for (int i=3; i<18; i+=3) {
            vertices[i] = Math.cos(1.256637*(i/3))/2;
            vertices[i+1] = 0.333333;
            vertices[i+2] = Math.sin(1.256637*(i/3))/2;
        }

        //seconde "couche"
        //pi/6 = 1,570796
        for (int i=18; i<33; i+=3) {
            vertices[i] = Math.cos(1.256637*(i/3)+0.523598)/2;
            vertices[i+1] = 0.666666;
            vertices[i+2] = Math.sin(1.256637*(i/3)+0.523598)/2;
        }

        //point le plus "haut"
        vertices[33] = 0;
        vertices[34] = 1;
        vertices[35] = 0;

        /**Génération des faces**/
        //faces "haut" et "bas"
        for (int i=0; i<30; i+=3) {
            if (i < 15)
                edges[i] = 0;
            else
                edges[i] = 11;

            edges[i+1] = (i/3)+1;

            if (i==12) {
                edges[i+2] = 1;
            }
            else if (i==27) {
                edges[i+2] = 6;
            }
            else {
                edges[i+2] = (i/3)+2;
            }

            //On "retourne" le triangle pour plaquer
            //la face du bon côté
            if (i >= 15) {
                int tmp = edges[i];
                edges[i] = edges[i+2];
                edges[i+2] = tmp;
            }
        }

        //faces au "centre"
        for (int i=30; i<60; i+=3) {
            if (i%6 == 0) {
                edges[i+2] = 5+((1+(i/6-5))%6);
                edges[i+1] = 1+((i/6-5)%5);
                edges[i] = 1+((1+(i/6-5))%5);
            }
            else {
                edges[i] = 1+((1+(i/6-5))%5);
                edges[i+1] = 5+((1+(i/6-5))%6);
                edges[i+2] = 6+((1+(i/6-5))%5);
            }
        }

        super.build();
    }

    @Override
    public void display(GL2 gl) {
        it++;
        //gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
        gl.glRotated(it*0.1, 0.0, 0.0, 1.0);
        gl.glTranslated(20, 0, 0);
        gl.glRotated(it, 0.0, 1.0, 0.0);
        gl.glScaled(5.0+Math.cos(it*0.05), 5.0+Math.cos(it*0.05), 5.0+Math.cos(it*0.05));

        //matériaux
        material.displayMaterial(gl);

        super.display(gl);
    }
}
