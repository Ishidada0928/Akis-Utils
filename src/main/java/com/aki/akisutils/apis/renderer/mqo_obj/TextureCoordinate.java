package com.aki.akisutils.apis.renderer.mqo_obj;


public class TextureCoordinate
{
    public float u, v, w;

    public TextureCoordinate(float u, float v)
    {
        this(u, v, 0F);
    }

    public TextureCoordinate(float u, float v, float w)
    {
        this.u = u;
        this.v = v;
        this.w = w;
    }
}