package com.aki.akisutils.apis.entity;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * 使用例:
 *   float yaw = YawPitchMovingUtil.getyaw(this.pos.getX() + 0.5, this.pos.getZ() + 0.5, tx, tz);
 *   float pitch = YawPitchMovingUtil.getPitch(this.pos.getX() + 0.5, this.pos.getY() + 1.5, this.pos.getZ() + 0.5, tx, ty, tz);
 *
 *   Vec3d vec = YawPitchMovingUtil.getVec3dMoving(yaw, pitch);
 *
 *   move(vec.x, vec.y, vec.z)  <--- 速度は[1]
 * */
public class EntityMoveUtil {
    public static Vec3d getVec3dMoving(float rotationYaw, float rotationPitch) {
        double motionX = 1 * (((double) (-MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI))));
        double motionY = 1 * (((double) (-MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI))));
        double motionZ = 1 * (((double) (MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI))));
        return new Vec3d(motionX, motionY, motionZ);
    }

    public static float getyaw(double xcoord, double zcoord, double tx, double tz) {
        double x = tx - xcoord;
        double z = tz - zcoord;
        /*double x = xcoord - tx;
        double z = zcoord - tz;*/
        return -(float)clampAngleTo360((float)(Math.atan2(x,z) / (float)Math.PI * 180.0F));
    }

    //上下
    public static float getPitch(double xcoord, double ycoord, double zcoord, double tx, double ty, double tz) {
        double x = tx - xcoord;
        double y = ty - ycoord;
        double z = tz - zcoord;
        /*double x = tx - xcoord;
        double y = ty - ycoord;
        double z = tz - zcoord;*/
        //return clamp360((float)(Math.atan2(Math.hypot(z, x),y) / (float)Math.PI * 180.0F));
        return clamp360((float)clampAngleTo360((float)(Math.atan2(y,Math.hypot(z, x)) / (float)Math.PI * 180.0F)));
    }

    public static float clamp360(float value) {
        float req = 0;

        if(value > 0) {
            req = value * -1;
        } else if(value < 0) {
            req = value - (value * 2);
        }
        return req;
    }

    public static double clampAngleTo360(double value) {
        return clampAngle(value, -360.0D, 360.0D);
    }

    public static double clampAngle(double value, double min, double max) {
        double result = value % 360.0D;
        while (result < min)
            result += 360.0D;
        while (result > max)
            result -= 360.0D;
        return result;
    }
}
