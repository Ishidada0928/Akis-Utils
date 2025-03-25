package com.aki.akisutils.mixins.minecraft.renderhooks;

import com.aki.akisutils.AkisUtils;
import com.aki.akisutils.apis.renderer.OpenGL.util.GLUtils;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RenderGlobal.class, priority = AkisUtils.ModPriority)
public class MixinRenderGlobal {
    @Inject(method = "setupTerrain", at = @At("HEAD"))
    public void setupCamera(Entity viewEntity, double partialTicks, ICamera camera, int frameCount, boolean playerSpectator, CallbackInfo info) {
        GLUtils.updateCamera();
    }
}
