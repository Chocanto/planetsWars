package planetsWars;

import org.json.*;
import java.util.Scanner;
import java.io.*;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import com.jogamp.common.nio.Buffers;
import com.jogamp.common.nio.PointerBuffer;
import java.nio.*;
import com.jogamp.opengl.util.gl2.GLUT;

public class JsonModel extends VBOModel {

    private String path;

    private double vertMin[];
    private double vertMax[];
    private double vertAvr[];

    private double ratio;

    private Material material;

    public JsonModel(String path, GL2 gl) {
        super(gl, 0, 0);
        this.path = path;
        material = new Material();
        prepare(gl);
    }
/*
    private void createVBO() {
        GL2 gl = drawable.getGL().getGL2();
        vertexFB =Buffers.newDirectDoubleBuffer(vertices);
        colorFB =Buffers.newDirectDoubleBuffer(vertices);
        gl.glVertexPointer(3, GL2.GL_DOUBLE, 0, vertexFB);
        gl.glColorPointer(3, GL2.GL_DOUBLE, 0, colorFB);

        indexBuf=Buffers.newDirectIntBuffer(connectivity);

        int[] temp = new int[2];
        gl.glGenBuffers(2, temp, 0);

        //points
        verticesVBO = temp[0];
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, verticesVBO);
        System.out.println(
            "debut load vertex: sommets_fichierBUF.capacity= "
            + vertexFB.capacity() +" ; "
            + "Buffers.SIZEOF_FLOAT= " +Buffers.SIZEOF_DOUBLE
        );

        gl.glBufferData(GL2.GL_ARRAY_BUFFER,
            vertexFB.capacity() * Buffers.SIZEOF_DOUBLE,
            vertexFB, GL2.GL_STATIC_DRAW
        );
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
        System.out.println("fin  load vertex et bind");

        //indices
        indexVBO = temp[1];
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, indexVBO);

        gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER,
            indexBuf.capacity() * Buffers.SIZEOF_INT,
            indexBuf, GL2.GL_STATIC_DRAW
        );
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
        System.out.println("fin  load indices et bind");
    }
*/
    public void build() {

        System.out.print("Chargement de "+path+"... ");

        vertMin = new double[3];
        vertMax = new double[3];
        String jsonString="";
         try { jsonString = new Scanner(new File(path)).useDelimiter("\\Z").next();
         } catch (FileNotFoundException e) {
             e.printStackTrace();
         }
        try {
            JSONObject object2 = new JSONObject(jsonString);

            JSONArray arr = object2.getJSONArray("vertices");
            JSONArray arr2 = arr.getJSONObject(0).getJSONArray("values");

            int nbrVertices = arr2.length();
            vertices = new double[nbrVertices];
            vertMin[0] = 9999999999.0;
            vertMin[1] = 9999999999.0;
            vertMin[2] = 9999999999.0;

            vertMax[0] = -9999999999.0;
            vertMax[1] = -9999999999.0;
            vertMax[2] = -9999999999.0;

            for (int i = 0; i < arr2.length(); i++)
            {
                double point = arr2.getDouble(i);
                vertices[i] = point;
                vertMin[i%3] = Math.min(vertMin[i%3], point);
                vertMax[i%3] = Math.max(vertMax[i%3], point);
            }

            JSONArray arrNorm = arr.getJSONObject(1).getJSONArray("values");
            normals = new double[arrNorm.length()];
            for (int i = 0; i < arr2.length(); i++)
                normals[i] = arr2.getDouble(i);

            JSONArray conArr = object2.getJSONArray("connectivity");
            JSONArray conArr2 = conArr.getJSONObject(0).getJSONArray("indices");

            int nbrConnectivity = conArr2.length();
            edges = new int[nbrConnectivity];
            for (int i = 0; i < conArr2.length(); i++)
            {
                edges[i] = conArr2.getInt(i);
            }
            vertAvr = new double[3];
            for (int i=0; i<3; i++) {
                vertAvr[i] = (vertMin[i]+vertMax[i])/2;
            }
            ratio(nbrVertices);

        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Terminé !");
    }

    public void display(GL2 gl) {
        material.displayMaterial(gl);
        super.display(gl);
    }

    public void ratio(int nbrVertices) {
        ratio = 1/(vertMax[0]-vertAvr[0]);
        for (int i=0; i < 3; i++) {
            ratio = Math.min(ratio, 1/(vertMax[i] - vertAvr[i]));
        }
        //on applique le ratio :
        for (int i=0; i<nbrVertices; i++) {
            vertices[i] -= vertAvr[i%3];
            vertices[i] *= ratio;
        }
    }

    public Material getMaterial() {
        return material;
    }
}
