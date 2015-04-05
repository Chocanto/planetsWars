package planetsWars;

import javax.media.opengl.GL2;
import com.jogamp.common.nio.PointerBuffer;
import com.jogamp.common.nio.Buffers;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

public class VBOModel {

    protected double[] vertices;
    protected int[] edges;
    protected int verticesVBO;
    protected int edgesVBO;

    public VBOModel(GL2 gl, int nVertices, int nTriangles) {
        vertices = new double[nVertices*3];
        edges = new int[nTriangles*3];

        build();
        initVBO(gl);
    }

    public void build() {

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

    public void computeNormals() {

    }
}
