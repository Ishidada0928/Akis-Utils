package com.aki.akisutils;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class AkisUtilsConfig {
    public static Configuration cfg;

    public static String category = "mod";

    public static long OneTickNanoBase = 50000000;// -> 50ms (1/20_1Tick)

    public static void PreInit(FMLPreInitializationEvent event) {
        cfg = new Configuration(event.getSuggestedConfigurationFile());
        SyncConfig();
    }

    public static void SettingConfig() {
        if(cfg != null) {
            cfg.load();
            cfg.addCustomCategoryComment(category, "Mod Settings");
            cfg.setCategoryRequiresMcRestart(category, false);
        }
    }

    public static void SyncConfig() {
        SettingConfig();

        OneTickNanoBase = cfg.getInt("OneTickNanoBase", category, 50000000, 0, Integer.MAX_VALUE, "This is the standard for measuring Update Speed. [Unit Nano]");

        cfg.save();
    }
}