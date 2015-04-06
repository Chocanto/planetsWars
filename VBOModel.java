package planetsWars;

import javax.media.opengl.GL2;
import com.jogamp.common.nio.PointerBuffer;
import com.jogamp.common.nio.Buffers;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class VBOModel {

    protected double[] vertices;
    protected int[] edges;
    protected double[] normals;

    protected int[] normalsLine;
    protected double[] normalsLineVert;
    protected double normDispLen = 0.5; //coef. des normales à afficher

    protected int verticesVBO;
    protected int edgesVBO;
    protected int normalsVBO;

    protected int normalsLineVertVBO;
    protected int normalsLineVBO;

    public VBOModel(GL2 gl, int nVertices, int nTriangles) {
        vertices = new double[nVertices*3];
        edges = new int[nTriangles*3];
        normals = new double[nVertices*3];

        //Pour debug
        normalsLineVert = new double[nVertices*6];
        normalsLine = new int[nVertices*2];

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
        gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, normalsVBO);
        gl.glNormalPointer(GL2.GL_DOUBLE, 0, 0);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, verticesVBO);
        gl.glVertexPointer(3, GL2.GL_DOUBLE, 0, 0);

        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, edgesVBO);
        gl.glDrawElements(GL2.GL_TRIANGLES, 60, GL2.GL_UNSIGNED_INT, 0);

        /**Affichage des normales**/
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, normalsLineVertVBO);
        gl.glVertexPointer(3, GL2.GL_DOUBLE, 0, 0);

        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, normalsLineVBO);
        gl.glDrawElements(GL2.GL_LINES, normalsLine.length, GL2.GL_UNSIGNED_INT, 0);
        /**************************/

        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void computeNormals() {

        double[] normalsFaces = new double[edges.length];
        double[] normalVertex = new double[3];
        ArrayList<Integer> posVertex = new ArrayList<Integer>();
        double[] result = new double[3];
        double[] v1 = new double[3];
        double[] v2 = new double[3];
        double[] v3 = new double[3];
        double[] tmp1 = new double[3];
        double[] tmp2 = new double[3];
        double length = 0.0;

        /****************
        Normales par face
        ****************/
        for (int i=0; i<edges.length; i+=3) {
            //remplir les tableaux
            for (int j=0; j<3; j++) {
                v1[j] = vertices[edges[i]*3+j];
                v2[j] = vertices[edges[i+1]*3+j];
                v3[j] = vertices[edges[i+2]*3+j];
            }

            //faire le calcul de la normale
            for (int j=0; j<3; j++) {

                //TODO : supprimer
                /**HOT FIX pour l'isocaèdre**/
                if (i < 15 || (i > 30 && i%6!=0)) {
                    tmp1[j] = v1[j] - v2[j];
                    tmp2[j] = v1[j] - v3[j];
                }
                else {
                    tmp1[j] = v1[j] - v3[j];
                    tmp2[j] = v1[j] - v2[j];
                }
                /****************************/
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

            for (int j=0; j<3; j++)
                result[j] *= length;

            //stocker
            for (int j=0; j<3; j++) {
                normalsFaces[i+j] = result[j];
            }
        }

        /******************
        Normales par vertex
        ******************/
        for (int i=0; i<vertices.length; i+=3) {
            //remise à zéro:
            for (int j=0; j<3; j++)
                normalVertex[j] = 0.0;
            posVertex.clear();

            //recherche à quelles faces appartient le vertex

            for (int j=0; j<edges.length; j+=3) {
                if (edges[j]==i/3 ||
                    edges[j+1]==i/3 ||
                    edges[j+2]==i/3)
                    posVertex.add(j/3);
            }

            //on fait la moyenne de toutes les normales
            for (int normalIndex : posVertex) {
                normalVertex[0] += normalsFaces[normalIndex*3];
                normalVertex[1] += normalsFaces[normalIndex*3+1];
                normalVertex[2] += normalsFaces[normalIndex*3+2];
            }

            normalVertex[0] /= posVertex.size();
            normalVertex[1] /= posVertex.size();
            normalVertex[2] /= posVertex.size();

            //on stocke
            normals[i] = normalVertex[0];
            normals[i+1] = normalVertex[1];
            normals[i+2] = normalVertex[2];
        }

        for (int i=0; i<normals.length; i+=3)
            System.out.println(normals[i]+";"+normals[i+1]+";"+normals[i+2]);

        /**Génération de l'affichage des normales  : **/
        //for (int i=60; i<edges.length*2; i+=6) {
        for (int i=0; i<normals.length*2; i+=6) {
            normalsLineVert[i] = vertices[i/2];
            normalsLineVert[i+1] = vertices[(i/2)+1];
            normalsLineVert[i+2] = vertices[(i/2)+2];

            normalsLineVert[i+3] = vertices[i/2] + normals[i/2] * normDispLen;
            normalsLineVert[i+4] = vertices[(i/2)+1] + normals[(i/2)+1] * normDispLen;
            normalsLineVert[i+5] = vertices[(i/2)+2] + normals[(i/2)+2] * normDispLen;
        }

        for (int i=0; i<normalsLine.length; i++) {
            normalsLine[i] = i;
        }

        System.out.println("--------------------");
        for (int i=0; i<normalsLineVert.length; i+=3)
            System.out.println(normalsLineVert[i]+";"+normalsLineVert[i+1]+";"+normalsLineVert[i+2]);
    }
}
