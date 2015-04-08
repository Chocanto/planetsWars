package td1_caneva;

import org.json.*;
import java.util.Scanner;
import java.io.*;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import com.jogamp.common.nio.Buffers;
import com.jogamp.common.nio.PointerBuffer;
import java.nio.*;
import com.jogamp.opengl.util.gl2.GLUT;

public class Maillage {

    private double vertices[];
    private int nbrVertices;
    private int connectivity[];
    private int nbrConnectivity; 
    
    private DoubleBuffer colorFB;
    private DoubleBuffer vertexFB;
    private IntBuffer indexBuf;

    private int verticesVBO;
    private int indexVBO;

    private double vertMin[];
    private double vertMax[];
    private double vertAvr[];

    private double ratio;

    GLAutoDrawable drawable;

    public Maillage(String path, GLAutoDrawable _drawable) {
        drawable = _drawable;
        readJSON(path);
        createVBO();
    }

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

    private void readJSON(String path) {
        vertMin = new double[3];
        vertMax = new double[3];
        String jsonString="";
         try { jsonString = new Scanner(new File(path)).useDelimiter("\\Z").next();
         } catch (FileNotFoundException e) {
             e.printStackTrace();
         }  
        System.out.println("Fin de la création du jsonString");
        try {
            JSONObject object = new JSONObject(jsonString);
            String com = object.getString("comment");
            JSONObject object2 = new JSONObject(jsonString);
            
            JSONArray arr = object2.getJSONArray("vertices");
            JSONArray arr2 = arr.getJSONObject(0).getJSONArray("values");
            
            nbrVertices = arr2.length();
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
            
            JSONArray conArr = object2.getJSONArray("connectivity");
            JSONArray conArr2 = conArr.getJSONObject(0).getJSONArray("indices");

            nbrConnectivity = conArr2.length();
            connectivity = new int[nbrConnectivity];
            for (int i = 0; i < conArr2.length(); i++)
            {   
                connectivity[i] = conArr2.getInt(i);
            }

            System.out.println("Point min : ("+
                    vertMin[0] + ";"+vertMin[1]+";"+vertMin[2]+")");
            System.out.println("Point max : ("+
                    vertMax[0] + ";"+vertMax[1]+";"+vertMax[2]+")");

            vertAvr = new double[3];
            for (int i=0; i<3; i++) {
                vertAvr[i] = (vertMin[i]+vertMax[i])/2;
            }
            System.out.println("Point moy : ("+
                    vertAvr[0] + ";"+vertAvr[1]+";"+vertAvr[2]+")");

            ratio();

        } catch (JSONException e) {
        System.out.println("pb quelque part");
        }
    }

    public void display() {
        GL2 gl = drawable.getGL().getGL2();

        //gl.glLoadIdentity();

        //gl.glLoadIdentity();
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, verticesVBO);
        gl.glVertexPointer(3, GL2.GL_DOUBLE, 0, 0);
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, indexVBO);
        gl.glDrawElements(GL2.GL_TRIANGLES, nbrConnectivity, GL2.GL_UNSIGNED_INT, 0);
        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
        
        /*gl.glPushMatrix();
        gl.glPushMatrix();
        gl.glColor3f(1,0,0);
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        gl.glTranslated(vertAvr[0], vertAvr[1], vertAvr[2]);
        glut.glutSolidSphere(0.1, 10, 10);

        gl.glPopMatrix();
        gl.glColor3f(0,1,0);
        gl.glTranslated(vertMin[0], vertMin[1], vertMin[2]);
        glut.glutSolidSphere(0.1, 10, 10);
        
        gl.glPopMatrix();
        gl.glTranslated(vertMax[0], vertMax[1], vertMax[2]);
        glut.glutSolidSphere(0.1, 10, 10);*/
    }

    public void ratio() {
        ratio = 1/(vertMax[0]-vertAvr[0]);
        for (int i=0; i < 3; i++) {
            ratio = Math.min(ratio, 1/(vertMax[i] - vertAvr[i]));
        }
        System.out.println("Ratio : "+ratio);


        //on applique le ratio :
        System.out.println("Application du ratio");
        for (int i=0; i<nbrVertices; i++) {
            vertices[i] -= vertAvr[i%3];
            vertices[i] *= ratio;
        }
        System.out.println("Ratio appliqué");

    }
}
