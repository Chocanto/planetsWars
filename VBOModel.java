package planetsWars;

import javax.media.opengl.GL2;
import com.jogamp.common.nio.PointerBuffer;
import com.jogamp.common.nio.Buffers;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

public class VBOModel {

    protected double[] vertices;
    protected int[] edges;
    protected double[] normals;

    protected int[] normalsLine;
    protected double[] normalsLineVert;
    protected double normDispLen = 1.0; //coef. des normales à afficher

    protected int verticesVBO;
    protected int edgesVBO;
    protected int normalsVBO;

    protected int normalsLineVertVBO;
    protected int normalsLineVBO;

    public VBOModel(GL2 gl, int nVertices, int nTriangles) {
        vertices = new double[nVertices*3];
        edges = new int[nTriangles*3];
        normals = new double[nTriangles*3];

        //Pour debug
        normalsLineVert = new double[nTriangles*6];
        normalsLine = new int[nTriangles*2];

        build();
        initVBO(gl);
    }

    public void build() {
        computeNormals();
    }

    public void initVBO(GL2 gl) {
        DoubleBuffer verticesBuf =Buffers.newDirectDoubleBuffer(vertices);
        DoubleBuffer normalsBuf =Buffers.newDirectDoubleBuffer(normals);
        DoubleBuffer normalsLineVertBuf =Buffers.newDirectDoubleBuffer(normalsLineVert);
        IntBuffer edgesBuf =Buffers.newDirectIntBuffer(edges);
        IntBuffer normalsLineBuf =Buffers.newDirectIntBuffer(normalsLine);

        int[] temp = new int[5];
        gl.glGenBuffers(5, temp, 0);

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

        normalsVBO = temp[2];
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, temp[2]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER,
            normals.length * Buffers.SIZEOF_DOUBLE,
            normalsBuf, GL2.GL_STATIC_DRAW);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);

        normalsLineVertVBO = temp[3];
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, temp[3]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER,
            normalsLineVert.length * Buffers.SIZEOF_DOUBLE,
            normalsLineVertBuf, GL2.GL_STATIC_DRAW);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);

        normalsLineVBO = temp[4];
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, temp[4]);
        gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER,
            normalsLine.length * Buffers.SIZEOF_INT,
            normalsLineBuf, GL2.GL_STATIC_DRAW);
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void display(GL2 gl) {
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, verticesVBO);
        gl.glVertexPointer(3, GL2.GL_DOUBLE, 0, 0);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, normalsVBO);
        gl.glNormalPointer(GL2.GL_DOUBLE, 0, 0);

        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, edgesVBO);
        gl.glDrawElements(GL2.GL_TRIANGLES, 60, GL2.GL_UNSIGNED_INT, 0);

        /**Affichage des normales**/
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, normalsLineVertVBO);
        gl.glVertexPointer(3, GL2.GL_DOUBLE, 0, 0);

        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, normalsLineVBO);
        gl.glDrawElements(GL2.GL_LINES, 24, GL2.GL_UNSIGNED_INT, 0);
        /**************************/

        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void computeNormals() {

        double[] result = new double[3];
        double[] v1 = new double[3];
        double[] v2 = new double[3];
        double[] v3 = new double[3];
        double[] tmp1 = new double[3];
        double[] tmp2 = new double[3];
        double length = 0.0;

        for (int i=0; i<vertices.length; i+=3)
            System.out.println(vertices[i]+";"+vertices[i+1]+";"+vertices[i+2]);

        System.out.println("#########################");

        for (int i=0; i<edges.length; i+=3)
            System.out.println(edges[i]+";"+edges[i+1]+";"+edges[i+2]);

        for (int i=0; i<edges.length; i+=3) {
            //remplir les tableaux
            for (int j=0; j<3; j++) {
                v1[j] = vertices[edges[i]*3+j];
                v2[j] = vertices[edges[i+1]*3+j];
                v3[j] = vertices[edges[i+2]*3+j];
            }


            System.out.println("----------------------------");
            System.out.println(v1[0]+";"+v1[1]+";"+v1[2]);
            System.out.println(v2[0]+";"+v2[1]+";"+v2[2]);
            System.out.println(v3[0]+";"+v3[1]+";"+v3[2]);

            //faire le calcul de la normale
            for (int j=0; j<3; j++) {
                tmp1[j] = v1[j] - v2[j];
                tmp2[j] = v1[j] - v3[j];
            }

            result[0] = tmp1[1] * tmp2[2] - tmp1[2] * tmp2[1];
            result[1] = tmp1[2] * tmp2[0] - tmp1[0] * tmp2[2];
            result[2] = tmp1[0] * tmp2[1] - tmp1[1] * tmp2[0];

            //"normaliser" la normale
            length = result[0]*result[0] +
                            result[1]*result[1] +
                            result[2]*result[2];
            if (length != 0)
                length = 1.0 / Math.sqrt(length);

            for (int j=0; j<3; j++) {
                result[j] *= length;
            }

            //stocker
            for (int j=0; j<3; j++) {
                normals[i+j] = result[j];
            }
        }


        /**Génération de l'affichage des normales  : **/
        //for (int i=0; i<edges.length*2; i+=6) {
        for (int i=0; i<30; i+=6) {
            normalsLineVert[i] = vertices[edges[i/2]*3];
            normalsLineVert[i+1] = vertices[edges[i/2]*3+1];
            normalsLineVert[i+2] = vertices[edges[i/2]*3+2];

            normalsLineVert[i+3] = vertices[edges[i/2]*3] + normals[i/2] * normDispLen;
            normalsLineVert[i+4] = vertices[edges[i/2]*3+1] + normals[(i+1)/2] * normDispLen;
            normalsLineVert[i+5] = vertices[edges[i/2]*3+2] + normals[(i+2)/2] * normDispLen;
        }

        for (int i=0; i<normalsLine.length; i+=2) {
            normalsLine[i] = i;
            normalsLine[i+1] = i+1;
        }

        //for (int i=0; i<normalsLineVert.length; i+=3)
        //    System.out.println(normalsLineVert[i]+";"+normalsLineVert[i+1]+";"+normalsLineVert[i+2]);
    }
}
