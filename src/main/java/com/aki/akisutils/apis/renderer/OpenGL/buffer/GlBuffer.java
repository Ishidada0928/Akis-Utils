package com.aki.akisutils.apis.renderer.OpenGL.buffer;

import com.aki.akisutils.apis.util.list.Pair;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL31;

import java.nio.*;

public abstract class GlBuffer extends GlObject {
    protected int size;
    protected int target = -1;

    protected GlBuffer() {
        //バッファ生成
        this.setHandle(GL15.glGenBuffers());
    }

    public int getTarget() {
        return this.target;
    }

    public void unbind() {
        GL15.glBindBuffer(this.target, 0);
        this.target = -1;
    }

    public void bind(int target) {
        GL15.glBindBuffer(target, this.handle());
        this.target = target;
    }

    public abstract void upload(long data_size);

    public abstract void upload(ByteBuffer buf);

    public abstract void upload(FloatBuffer buf);

    public abstract void upload(IntBuffer buf);
    public abstract void upload(DoubleBuffer buf);
    public abstract void upload(ShortBuffer buf);

    //bufferSubData...
    public abstract void bufferSubData(long offset, ByteBuffer buf);

    public abstract void bufferSubData(long offset, FloatBuffer buf);

    public abstract void bufferSubData(long offset, IntBuffer buf);
    public abstract void bufferSubData(long offset, DoubleBuffer buf);
    public abstract void bufferSubData(long offset, ShortBuffer buf);

    public abstract void allocate(int size);

    /*
    public void upload(int target, VertexData data) {
        this.upload(target, data.buffer);
    }*/

    public void delete() {
        GL15.glDeleteBuffers(this.handle());

        this.invalidateHandle();
        this.size = 0;
    }

    public static void copy(GlBuffer src, GlBuffer dst, int readOffset, int writeOffset, int copyLen, int bufferSize) {
        src.bind(GL31.GL_COPY_READ_BUFFER);

        dst.bind(GL31.GL_COPY_WRITE_BUFFER);
        dst.allocate(bufferSize);

        GL31.glCopyBufferSubData(GL31.GL_COPY_READ_BUFFER, GL31.GL_COPY_WRITE_BUFFER, readOffset, writeOffset, copyLen);

        dst.unbind();
        src.unbind();
    }

    public int getSize() {
        return this.size;
    }

    public Pair<Integer, Integer> fixSize() {
        int old_size = this.size;
        this.size = GL15.glGetBufferParameteri(this.target, GL15.GL_BUFFER_SIZE);
        return new Pair<>(old_size, this.size);
    }

    @Deprecated
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public void setHandle(int new_handle) {
        super.setHandle(new_handle);
    }
}
