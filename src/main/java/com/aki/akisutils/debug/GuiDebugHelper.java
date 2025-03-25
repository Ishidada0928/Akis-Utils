package com.aki.akisutils.debug;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GuiDebugHelper {
    private static final List<Consumer<List<String>>> MinecraftDebugReplaceConsumers = new ArrayList<>();
    private static final List<List<String>> StringList = new ArrayList<>();

    public static List<Consumer<List<String>>> getMinecraftDebugReplaceConsumers() {
        return MinecraftDebugReplaceConsumers;
    }

    public static List<List<String>> getStringList() {
        return StringList;
    }

    /*public static int addLastBeforeMinecraftStringConsumer(Consumer<List<String>> consumer) {
        MinecraftDebugReplaceConsumers.addLast(consumer);
        return MinecraftDebugReplaceConsumers.size() - 1;
    }*/

    //Return ReplaceIndex
    public static int ReplaceMinecraftDebugConsumers(Consumer<List<String>> oldConsumer, Consumer<List<String>> NewConsumer) {
        synchronized (MinecraftDebugReplaceConsumers) {
            int index = MinecraftDebugReplaceConsumers.indexOf(oldConsumer);
            if (index >= 0 && !MinecraftDebugReplaceConsumers.isEmpty()) {
                MinecraftDebugReplaceConsumers.remove(index);
                MinecraftDebugReplaceConsumers.add(index, NewConsumer);
                return index;
            } else {
                MinecraftDebugReplaceConsumers.add(NewConsumer);
                return MinecraftDebugReplaceConsumers.size() - 1;
            }
        }
    }

    /*public static int addLastDebugStringList(List<String> stringList) {
        StringList.addLast(stringList);
        return StringList.size() - 1;
    }*/

    //Return ReplaceIndex
    public static int ReplaceDebugStringList(List<String> oldDebugStringList, List<String> NewDebugStringList) {
        synchronized (StringList) {
            int index = StringList.indexOf(oldDebugStringList);//うまくいかない
            if (index >= 0 && !StringList.isEmpty()) {
                StringList.remove(index);
                StringList.add(index, new ArrayList<>(NewDebugStringList));
                return index;
            } else {
                StringList.add(new ArrayList<>(NewDebugStringList));
                return StringList.size() - 1;
            }
        }
    }
}
