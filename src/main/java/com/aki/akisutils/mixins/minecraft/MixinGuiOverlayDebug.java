package com.aki.akisutils.mixins.minecraft;

import com.aki.akisutils.AkisUtils;
import com.aki.akisutils.debug.GuiDebugHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.crash.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = GuiOverlayDebug.class, priority = AkisUtils.ModPriority)
public class MixinGuiOverlayDebug {
    @Inject(method = "call", at = @At("RETURN"))
    public void AddModDataCall(CallbackInfoReturnable<List<String>> cir) {
        try {
            List<String> stringList = cir.getReturnValue();

            GuiDebugHelper.getMinecraftDebugReplaceConsumers().forEach(action -> action.accept(stringList));
            GuiDebugHelper.getStringList().forEach(stringList::addAll);
        } catch (Exception e) {
            AkisUtils.logger.error("MCUtils Mixin Error: GuiOverlayDebug Data");
            GuiDebugHelper.getStringList().forEach(list -> list.forEach(AkisUtils.logger::error));
            AkisUtils.logger.error("MCUtils Mixin Error: Data End");
            Minecraft.getMinecraft().crashed(new CrashReport("MCUtils Mixin GuiOverlayDebug Error", e));
        }
        //cir.setReturnValue(stringList);
    }
}
