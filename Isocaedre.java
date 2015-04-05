package planetsWars;

import javax.media.opengl.GL2;
import com.jogamp.common.nio.PointerBuffer;
import com.jogamp.common.nio.Buffers;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

public class Isocaedre {
    private double x;
    private double y;
    private double z;
    private double[] vertices;
    private int[] edges;
    private int verticesVBO;
    private int edgesVBO;

    public Isocaedre(double x, double y, double z, GL2 gl) {
        this.x = x;
        this.y = y;
        this.z = z;
        vertices = new double[12*3];
        edges = new int[20*3];
        build();
        initVBO(gl);
    }

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
        }

        //faces au "centre"
        for (int i=30; i<60; i+=3) {
            if (i%6 == 0) {
                edges[i] = 5+((1+(i/6-5))%6);
                edges[i+1] = 1+((i/6-5)%5);
                edges[i+2] = 1+((1+(i/6-5))%5);
            }
            else {
                edges[i] = 1+((1+(i/6-5))%5);
                edges[i+1] = 5+((1+(i/6-5))%6);
                edges[i+2] = 6+((1+(i/6-5))%5);
            }
        }

        for (int i=0; i<60; i+=3)
            System.out.println(edges[i]+";"+edges[i+1]+";"+edges[i+2]);
    }

    public void initVBO(GL2 gl) {
        DoubleBuffer verticesBuf =Buffers.newDirectDoubleBuffer(vertices);
        IntBuffer edgesBuf =Buffers.newDirectIntBuffer(edges);

        int[] temp = new int[2];
        gl.glGenBuffers(2, temp, 0);

        verticesVBO = temp[0];
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, temp[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER,
            vertices.length * Buffers.SIZEOF_DOUBLE,
            verticesBuf, GL2.GL_STATIC_DRAW);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);

        edgesVBO = temp[1];
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, temp[1]);
        gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER,
            edges.length * Buffers.SIZEOF_INT,
            edgesBuf, GL2.GL_STATIC_DRAW);
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void display(GL2 gl) {

        //gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);

        //gl.glDisable(GL2.GL_LIGHTING); //Pour afficher les couleurs

        gl.glScaled(5.0, 5.0, 5.0);

        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL2.GL_COLOR_ARRAY);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, verticesVBO);
        gl.glVertexPointer(3, GL2.GL_DOUBLE, 0, 0);

        gl.glColorPointer(3, GL2.GL_DOUBLE, 0, 0);

        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, edgesVBO);
        gl.glDrawElements(GL2.GL_TRIANGLES, 60, GL2.GL_UNSIGNED_INT, 0);

        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_COLOR_ARRAY);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
    }
}
