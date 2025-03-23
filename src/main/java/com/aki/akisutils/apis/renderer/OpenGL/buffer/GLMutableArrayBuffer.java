package com.aki.akisutils.apis.renderer.OpenGL.buffer;

import org.lwjgl.opengl.GL30;

public class GLMutableArrayBuffer extends GlObject {
    public GLMutableArrayBuffer() {
        this.setHandle(GL30.glGenVertexArrays());
    }

    public void bind() {
        GL30.glBindVertexArray(this.handle());
    }

    public void ChangeNewVAO() {
        GL30.glDeleteVertexArrays(this.handle());
        this.setHandle(GL30.glGenVertexArrays());
    }

    public void unbind() {
        GL30.glBindVertexArray(0);
    }

    public void delete() {
        unbind();
        System.out.println("VAO -> Delete");
        GL30.glDeleteVertexArrays(this.handle());
        this.invalidateHandle();
    }
}
