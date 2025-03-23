package com.aki.akisutils.utils.mixins;

import com.aki.akisutils.AkisUtilsConfig;

public class InformationCollector {
    private static long OneTickTime = AkisUtilsConfig.OneTickNanoBase;
    private static long LateTime = 0L;
    private static int LateTileEntities = 0;
    private static int MaxLateCycle = 0;

    private static int PlayerChunkTiles = 0;

    public static void setOneTickTime(long time) {
        OneTickTime = time;
    }

    public static void setLateTime(long lateTime) {
        LateTime = lateTime;
    }

    public static void setLateTileEntities(int lateTileEntities) {
        LateTileEntities = lateTileEntities;
    }

    public static void setMaxLateCycle(int maxLateCycle) {
        MaxLateCycle = maxLateCycle;
    }

    public static void setPlayerChunkTiles(int playerChunkTiles) {
        PlayerChunkTiles = playerChunkTiles;
    }

    public static long getOneTickTime() {
        return OneTickTime;
    }

    public static long getLateTime() {
        return LateTime;
    }

    public static int getLateTileEntities() {
        return LateTileEntities;
    }

    public static int getMaxLateCycle() {
        return MaxLateCycle;
    }

    public static int getPlayerChunkTiles() {
        return PlayerChunkTiles;
    }
}