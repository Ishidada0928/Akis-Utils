package com.aki.akisutils.apis.renderer.OpenGL.buffer;

import org.lwjgl.opengl.GL15;

import java.nio.*;

public class GlMutableBuffer extends GlBuffer {
    private final int hints;

    public GlMutableBuffer(int hints) {
        this.hints = hints;
    }

    /**
     * データの保存(格納)
     * 座標や色、ライトマッピングなど
     * */
    @Override
    public void upload(long data_size) {
        GL15.glBufferData(this.target, data_size, this.hints);
        this.size = (int)data_size;
    }

    @Override
    public void upload(ByteBuffer buf) {
        GL15.glBufferData(this.target, buf, this.hints);
        this.size = buf.capacity();
    }

    @Override
    public void upload(FloatBuffer buf) {
        GL15.glBufferData(this.target, buf, this.hints);
        this.size = buf.capacity();
    }

    @Override
    public void upload(IntBuffer buf) {
        GL15.glBufferData(this.target, buf, this.hints);
        this.size = buf.capacity();
    }

    @Override
    public void upload(DoubleBuffer buf) {
        GL15.glBufferData(this.target, buf, this.hints);
        this.size = buf.capacity();
    }

    @Override
    public void upload(ShortBuffer buf) {
        GL15.glBufferData(this.target, buf, this.hints);
        this.size = buf.capacity();
    }

    @Override
    public void bufferSubData(long offset, ByteBuffer buf) {
        int new_size = this.size + (buf.limit() - this.size + (int)offset);
        if(new_size > this.size)
            this.setHandle(GLHelper.growBuffer(this.target, this.handle(), this.size, new_size));
        this.bind(this.target);
        GL15.glBufferSubData(this.target, offset, buf);
        this.size = Math.max(this.size, new_size);
    }

    @Override
    public void bufferSubData(long offset, FloatBuffer buf) {
        int new_size = this.size + (buf.limit() - this.size + (int)offset);
        if(new_size > this.size)
            this.setHandle(GLHelper.growBuffer(this.target, this.handle(), this.size, new_size));
        this.bind(this.target);
        GL15.glBufferSubData(this.target, offset, buf);
        this.size = Math.max(this.size, new_size);
    }

    @Override
    public void bufferSubData(long offset, IntBuffer buf) {
        int new_size = this.size + (buf.limit() - this.size + (int)offset);
        if(new_size > this.size)
            this.setHandle(GLHelper.growBuffer(this.target, this.handle(), this.size, new_size));
        this.bind(this.target);
        GL15.glBufferSubData(this.target, offset, buf);
        this.size = Math.max(this.size, new_size);
    }

    @Override
    public void bufferSubData(long offset, DoubleBuffer buf) {
        int new_size = this.size + (buf.limit() - this.size + (int)offset);
        if(new_size > this.size)
            this.setHandle(GLHelper.growBuffer(this.target, this.handle(), this.size, new_size));
        this.bind(this.target);
        GL15.glBufferSubData(this.target, offset, buf);
        this.size = Math.max(this.size, new_size);
    }

    @Override
    public void bufferSubData(long offset, ShortBuffer buf) {
        int new_size = this.size + (buf.limit() - this.size + (int)offset);
        if(new_size > this.size)
            this.setHandle(GLHelper.growBuffer(this.target, this.handle(), this.size, new_size));
        this.bind(this.target);
        GL15.glBufferSubData(this.target, offset, buf);
        this.size = Math.max(this.size, new_size);
    }

    @Override
    public void allocate(int size) {
        GL15.glBufferData(this.target, size, this.hints);
        this.size = size;
    }
}