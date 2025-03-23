package com.aki.akisutils.apis.renderer.OpenGL.util;

import com.aki.akisutils.apis.renderer.minecraft.Frustum;
import com.aki.akisutils.apis.util.matrixutil.Matrix4f;
import com.aki.akisutils.apis.util.memory.UnsafeBufferUtil;
import com.aki.akisutils.apis.util.memory.UnsafeFloatBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.*;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;

public class GLUtils {
    private static int frame;
    private static double partialTick;
    private static double partialTickDelta;

    private static int recursive;
    private static Matrix4f projectionMatrix;
    private static Matrix4f modelViewMatrix;
    private static Matrix4f projectionModelViewMatrix;
    private static double cameraEntityX;
    private static double cameraEntityY;
    private static double cameraEntityZ;
    private static double cameraOffsetX;
    private static double cameraOffsetY;
    private static double cameraOffsetZ;
    private static double cameraX;
    private static double cameraY;
    private static double cameraZ;
    private static Frustum frustum;

    private static boolean blend;
    private static int blendSrcFactor;
    private static int blendDstFactor;
    private static int blendSrcFactorAlpha;
    private static int blendDstFactorAlpha;
    private static boolean depthTest;
    private static int depthFunc;
    private static boolean depthMask;
    private static boolean cull;
    private static int cullFace;
    private static boolean colorMaskRed;
    private static boolean colorMaskGreen;
    private static boolean colorMaskBlue;
    private static boolean colorMaskAlpha;

    private static final UnsafeFloatBuffer FLOAT_BUFFER = UnsafeBufferUtil.allocateFloat(16);

    public static ContextCapabilities CAPS = null;

    public static void update(double partialTickIn) {
        frame++;
        partialTickDelta = partialTickIn - partialTick;
        if (partialTickDelta < 0.0D)
            partialTickDelta += 1.0D;
        partialTick = partialTickIn;
        recursive = 0;
    }

    public static void updateCamera() {
        Minecraft mc = Minecraft.getMinecraft();
        Entity cameraEntity = mc.getRenderViewEntity();
        Vec3d cameraOffset = ActiveRenderInfo.getCameraPosition();
        projectionMatrix = getMatrix(GL11.GL_PROJECTION_MATRIX);
        modelViewMatrix = getMatrix(GL11.GL_MODELVIEW_MATRIX);
        projectionModelViewMatrix = projectionMatrix.copy();
        projectionModelViewMatrix.multiply(modelViewMatrix);
        cameraEntityX = cameraEntity.lastTickPosX + (cameraEntity.posX - cameraEntity.lastTickPosX) * partialTick;
        cameraEntityY = cameraEntity.lastTickPosY + (cameraEntity.posY - cameraEntity.lastTickPosY) * partialTick;
        cameraEntityZ = cameraEntity.lastTickPosZ + (cameraEntity.posZ - cameraEntity.lastTickPosZ) * partialTick;
        cameraOffsetX = cameraOffset.x;
        cameraOffsetY = cameraOffset.y;
        cameraOffsetZ = cameraOffset.z;
        cameraX = cameraEntityX + cameraOffsetX;
        cameraY = cameraEntityY + cameraOffsetY;
        cameraZ = cameraEntityZ + cameraOffsetZ;
        frustum = new Frustum(projectionModelViewMatrix, cameraX, cameraY, cameraZ);
    }

    public static Frustum getFrustum() {
        return frustum;
    }

    public static void init() {
        CAPS = GLContext.getCapabilities();
    }

    public static int createBuffer(long size, int flags, int usage) {
        if (CAPS.OpenGL45) {
            int buffer = GL45.glCreateBuffers();
            GL45.glNamedBufferStorage(buffer, size, flags);
            return buffer;
        } else if (CAPS.OpenGL44) {
            int buffer = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
            GL44.glBufferStorage(GL15.GL_ARRAY_BUFFER, size, flags);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
            return buffer;
        } else {
            int buffer = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, size, usage);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
            return buffer;
        }
    }

    public static Matrix4f getMatrix(int matrix) {
        GL11.glGetFloat(matrix, FLOAT_BUFFER.getBuffer());
        Matrix4f m = new Matrix4f();
        m.load(FLOAT_BUFFER.getAddress());
        return m;
    }

    public static void setMatrix(int uniform, Matrix4f matrix) {
        matrix.store(FLOAT_BUFFER.getAddress());
        GL20.glUniformMatrix4(uniform, false, FLOAT_BUFFER.getBuffer());
    }

    public static UnsafeFloatBuffer getFloat(int pname) {
        GL11.glGetFloat(pname, FLOAT_BUFFER.getBuffer());
        return FLOAT_BUFFER;
    }

    public static ByteBuffer map(int buffer, long length, int accessRange, int access, @Nullable ByteBuffer oldBuffer) {
        if (CAPS.OpenGL45) {
            return GL45.glMapNamedBufferRange(buffer, 0L, length, accessRange, oldBuffer);
        } else if (CAPS.OpenGL30) {
            if (!CAPS.OpenGL44) {
                accessRange &= ~(GL44.GL_MAP_PERSISTENT_BIT | GL44.GL_MAP_COHERENT_BIT);
            }
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
            ByteBuffer byteBuffer = GL30.glMapBufferRange(GL15.GL_ARRAY_BUFFER, 0L, length, accessRange, oldBuffer);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
            return byteBuffer;
        } else {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
            ByteBuffer byteBuffer = GL15.glMapBuffer(GL15.GL_ARRAY_BUFFER, access, length, oldBuffer);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
            return byteBuffer;
        }
    }

    public static void unmap(int buffer) {
        if (CAPS.OpenGL45) {
            GL45.glUnmapNamedBuffer(buffer);
        } else {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
            GL15.glUnmapBuffer(GL15.GL_ARRAY_BUFFER);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        }
    }

    public static int getFrame() {
        return frame;
    }

    public static double getPartialTick() {
        return partialTick;
    }

    public static double getPartialTickDelta() {
        return partialTickDelta;
    }

    public static boolean isRecursive() {
        return recursive > 1;
    }

    public static Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public static Matrix4f getModelViewMatrix() {
        return modelViewMatrix;
    }

    public static Matrix4f getProjectionModelViewMatrix() {
        return projectionModelViewMatrix;
    }

    public static double getCameraEntityX() {
        return cameraEntityX;
    }

    public static double getCameraEntityY() {
        return cameraEntityY;
    }

    public static double getCameraEntityZ() {
        return cameraEntityZ;
    }

    public static double getCameraOffsetX() {
        return cameraOffsetX;
    }

    public static double getCameraOffsetY() {
        return cameraOffsetY;
    }

    public static double getCameraOffsetZ() {
        return cameraOffsetZ;
    }

    public static double getCameraX() {
        return cameraX;
    }

    public static double getCameraY() {
        return cameraY;
    }

    public static double getCameraZ() {
        return cameraZ;
    }

    public static void saveShaderGLState() {
        blend = GlStateManager.blendState.blend.currentState;
        blendSrcFactor = GlStateManager.blendState.srcFactor;
        blendDstFactor = GlStateManager.blendState.dstFactor;
        blendSrcFactorAlpha = GlStateManager.blendState.srcFactorAlpha;
        blendDstFactorAlpha = GlStateManager.blendState.dstFactorAlpha;

        depthTest = GlStateManager.depthState.depthTest.currentState;
        depthFunc = GlStateManager.depthState.depthFunc;
        depthMask = GlStateManager.depthState.maskEnabled;

        cull = GlStateManager.cullState.cullFace.currentState;
        cullFace = GlStateManager.cullState.mode;

        colorMaskRed = GlStateManager.colorMaskState.red;
        colorMaskGreen = GlStateManager.colorMaskState.green;
        colorMaskBlue = GlStateManager.colorMaskState.blue;
        colorMaskAlpha = GlStateManager.colorMaskState.alpha;
    }

    public static void restoreShaderGLState() {
        if (blend) {
            GlStateManager.enableBlend();
        } else {
            GlStateManager.disableBlend();
        }
        GlStateManager.tryBlendFuncSeparate(blendSrcFactor, blendDstFactor, blendSrcFactorAlpha, blendDstFactorAlpha);

        if (depthTest) {
            GlStateManager.enableDepth();
        } else {
            GlStateManager.disableDepth();
        }
        GlStateManager.depthFunc(depthFunc);
        GlStateManager.depthMask(depthMask);

        if (cull) {
            GlStateManager.enableCull();
        } else {
            GlStateManager.disableCull();
        }
        GlStateManager.cullFace(cullFace);

        GlStateManager.colorMask(colorMaskRed, colorMaskGreen, colorMaskBlue, colorMaskAlpha);
    }
}
