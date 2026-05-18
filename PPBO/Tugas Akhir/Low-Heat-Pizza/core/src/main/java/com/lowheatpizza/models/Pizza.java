package com.lowheatpizza.models;

import com.badlogic.gdx.utils.Array;

public class Pizza {
    private boolean hasSauce;
    private boolean hasCheese;
    private final Array<String> toppings = new Array<>();
    private boolean baked;

    public void addSauce() {
        hasSauce = true;
    }

    public void addCheese() {
        hasCheese = true;
    }

    public void addTopping(String toppingId) {
        if (!toppings.contains(toppingId, false)) {
            toppings.add(toppingId);
        }
    }

    public void bake() {
        baked = true;
    }

    public void reset() {
        hasSauce = false;
        hasCheese = false;
        toppings.clear();
        baked = false;
    }

    public boolean hasSauce() {
        return hasSauce;
    }

    public boolean hasCheese() {
        return hasCheese;
    }

    public Array<String> getToppings() {
        return toppings;
    }

    public boolean isBaked() {
        return baked;
    }
}

