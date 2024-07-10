package com.mygdx.game.model;

import java.util.HashSet;
import java.util.Set;

public class PlayerInventory {
    private static Set<String> items = new HashSet<>();

    public static void addItem(String item) {
        items.add(item);
    }

    public static boolean hasItem(String item) {
        return items.contains(item);
    }
}
