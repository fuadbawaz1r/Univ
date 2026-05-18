package com.lowheatpizza.kitchen;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class KitchenLayout {
    public int virtualWidth;
    public int virtualHeight;
    public String background;
    public String backgroundClean;
    public String backgroundNumbered;
    public boolean debug;
    public Array<KitchenSlot> slots = new Array<>();
    public Array<PrepAreaConfig> prepAreas = new Array<>();
    public Array<StationConfig> stations = new Array<>();
    public Array<Vector2> pepperoniPositions = new Array<>();

    public KitchenSlot getSlot(int number) {
        for (KitchenSlot slot : slots) {
            if (slot.number == number) {
                return slot;
            }
        }
        throw new IllegalArgumentException("Missing kitchen slot: " + number);
    }

    public static class PrepAreaConfig {
        public String id;
        public int slot;
    }

    public static class StationConfig {
        public String id;
        public String type;
        public int slot;
        public String asset;
        public boolean unlocked;
    }
}

