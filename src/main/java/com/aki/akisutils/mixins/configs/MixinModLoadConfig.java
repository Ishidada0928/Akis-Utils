package com.aki.akisutils.mixins.configs;

import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.CoreModManager;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
@IFMLLoadingPlugin.SortingIndex(12000)
public class MixinModLoadConfig implements IFMLLoadingPlugin {
    /**
     * Vanillaの変更
     * [Loader] など、初期に実行するものは、jsonの[ "target" ]を [ "@env(INIT)" ]にしないといけない
     * GameSetting や RayTracing など、
     */
    public List<String> MixinFiles = Arrays.asList(
            "mixins.akisutils.json"
    );

    public MixinModLoadConfig() {
        //fixMixinClasspathOrder();
        MixinBootstrap.init();
        for (String fileName : MixinFiles) {
            Mixins.addConfiguration(fileName);
        }
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        if (Boolean.FALSE.equals(data.get("runtimeDeobfuscationEnabled"))) {
            MixinBootstrap.init();
            MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
            CoreModManager.getReparseableCoremods().removeIf(s -> StringUtils.containsIgnoreCase(s, "akisutils"));
        }
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}