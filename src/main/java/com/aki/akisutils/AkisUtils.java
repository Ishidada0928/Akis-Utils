package com.aki.akisutils;

import com.aki.akisutils.apis.renderer.OpenGL.util.GLUtils;
import com.aki.akisutils.commands.AkisUtilsCommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = AkisUtils.MOD_ID,
        name = AkisUtils.MOD_NAME,
        version = AkisUtils.VERSION
        //guiFactory = "com.aki.modfix.ConfigGuiFactory"
)
public class AkisUtils {
    public static final String MOD_ID = "akisutils";
    public static final String MOD_NAME = "Aki's utils";
    public static final String VERSION = "v1.0.0-mc1.12.2";
    public static final int ModPriority = 2389;

    //割合を使って処理
    //整数になった時にforを回す。
    //21の時は
    public static double EntityUpdateTick = 20.0d;
    public static double TileUpdateTick = 20.0d;
    public static final double BaseTick = 20.0d;

    @Mod.Instance(MOD_ID)
    public static AkisUtils INSTANCE;
    public static Logger logger;

    //mod構成時
    @Mod.EventHandler
    public void construct(FMLConstructionEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        // コンフィグが変更された時に呼ばれる。
        if (event.getModID().equals(MOD_ID)) {
            ConfigManager.sync(MOD_ID, Config.Type.INSTANCE);
            AkisUtilsConfig.SyncConfig();
        }
    }

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        logger = event.getModLog();

        AkisUtilsConfig.PreInit(event);
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        //MinecraftForge.EVENT_BUS.register(new ProcessHandler());
        GLUtils.init();
    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new AkisUtilsCommand());
    }
}