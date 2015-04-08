package planetsWars;

import javax.media.opengl.GL2;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import com.jogamp.opengl.util.texture.*;

public class Skybox extends VBOModel {

    Material material;

    public Skybox(GL2 gl) {
        super(gl, 108, 36);

        try {
            Texture texture = TextureIO.newTexture(new File("Textures/spacebox.jpg"), false);
            this.setTexture(texture);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        material = new Material();
        material.setShininess(new float[] {
            1.0f, 1.0f, 1.0f
        });

        prepare(gl);
    }

    @Override
    public void build() {

        for (int i=0; i<vertices.length; i+=3) {
            vertices[i] = ((i/3)/4%2)-0.5;
            vertices[i+1] = ((i/3)/2%2)-0.5;
            vertices[i+2] = ((i/3)%2)-0.5;
        }

        vertices = new double[] {
            -0.5, -0.5, -0.5,
            -0.5, -0.5, 0.5,
            0.5, -0.5, 0.5,

            -0.5, -0.5, -0.5,
            0.5, -0.5, 0.5,
            0.5, -0.5, -0.5,


            -0.5, 0.5, -0.5,
            0.5, 0.5, 0.5,
            -0.5, 0.5, 0.5,

            -.5, .5, -.5,
            .5, .5, -.5,
            .5, .5, .5,


            -.5, .5, .5,
            .5, -.5, .5,
            -.5, -.5, .5,

            -.5, .5, .5,
            .5, .5, .5,
            .5, -.5, .5,


            .5, .5, .5,
            .5, -.5, -.5,
            .5, -.5, .5,

            .5, .5, .5,
            .5, .5, -.5,
            .5, -.5, -.5,


            .5, .5, -.5,
            -.5, -.5, -.5,
            .5, -.5, -.5,

            .5, .5, -.5,
            -.5, .5, -.5,
            -.5, -.5, -.5,


            -.5, .5, -.5,
            -.5, -.5, .5,
            -.5, -.5, -.5,

            -.5, .5, -.5,
            -.5, .5, .5,
            -.5, -.5, .5,
        };

        for (int i=0; i<edges.length; i++) {
            edges[i] = i;
        }

        textureCoord = new double[] {
            0.25, 0.33,
            1., 0.66,
            /*0.5, 0.33,
            0., 0.,
            2., 2.,
            2., 0.,
            0.2, 0.8,
            0.5, 0.5,
            0.5, 0.5,
            0.5, 0.5,
            0.5, 0.5,
            0.5, 0.5,*/
        };
    }

    @Override
    public void display(GL2 gl) {

        gl.glDisable(GL2.GL_LIGHTING);

        material.displayMaterial(gl);

        gl.glDisable(GL2.GL_DEPTH_TEST);
        gl.glDepthMask(false);

        gl.glScaled(10.0, 10.0, 10.0);
        super.display(gl);


        gl.glDepthMask(true);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_LIGHTING);
    }
}
