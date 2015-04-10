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

        this.useCubemap = true;

        try {
            //Texture texture = TextureIO.newTexture(new File("Textures/spacebox.jpg"), false);
            this.setTexture(cubeMap(gl, new String[]{
                "Textures/skybox/1.jpg",
                "Textures/skybox/2.jpg",
                "Textures/skybox/3.jpg",
                "Textures/skybox/4.jpg",
                "Textures/skybox/5.jpg",
                "Textures/skybox/6.jpg",
            }));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (Exception e) {
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
    }

    @Override
    public void display(GL2 gl) {

        gl.glDisable(GL2.GL_LIGHTING);
        gl.glDisable(GL2.GL_DEPTH_TEST);
        gl.glDepthMask(false);

        material.displayMaterial(gl);
        //gl.glScaled(10.0, 10.0, 10.0);
        super.display(gl);

        gl.glDepthMask(true);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_LIGHTING);
    }

    private Texture cubeMap(GL2 gl, String[] files) throws Exception {
        if (files.length != 6)
            throw new Exception(
                "Nombre d'images insuffisant pour créer la cubeMap");

        Texture tex = TextureIO.newTexture(GL2.GL_TEXTURE_CUBE_MAP);
        tex.updateImage(gl, TextureIO.newTextureData(gl.getGLProfile(),
        new File(files[0]), true, null), GL2.GL_TEXTURE_CUBE_MAP_POSITIVE_X);

        tex.updateImage(gl, TextureIO.newTextureData(gl.getGLProfile(),
        new File(files[1]), true, null), GL2.GL_TEXTURE_CUBE_MAP_NEGATIVE_X);

        tex.updateImage(gl, TextureIO.newTextureData(gl.getGLProfile(),
        new File(files[2]), true, null), GL2.GL_TEXTURE_CUBE_MAP_POSITIVE_Y);

        tex.updateImage(gl, TextureIO.newTextureData(gl.getGLProfile(),
        new File(files[3]), true, null), GL2.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y);

        tex.updateImage(gl, TextureIO.newTextureData(gl.getGLProfile(),
        new File(files[4]), true, null), GL2.GL_TEXTURE_CUBE_MAP_POSITIVE_Z);

        tex.updateImage(gl, TextureIO.newTextureData(gl.getGLProfile(),
        new File(files[5]), true, null), GL2.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z);

        return tex;
    }
}
