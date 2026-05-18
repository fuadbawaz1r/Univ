package com.lowheatpizza.kitchen;

import com.badlogic.gdx.utils.Array;
import com.lowheatpizza.models.Pizza;

public class PizzaModel {
    private boolean hasDough;
    private boolean hasSauce;
    private boolean hasCheese;
    private final Array<String> toppings = new Array<>();
    private boolean baked;
    private boolean baking;

    public void addDough() {
        hasDough = true;
    }

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

    public boolean isBaked() {
        return baked;
    }

    public void setBaking(boolean baking) {
        this.baking = baking;
    }

    public boolean isBaking() {
        return baking;
    }

    public void reset() {
        hasDough = false;
        hasSauce = false;
        hasCheese = false;
        toppings.clear();
        baked = false;
        baking = false;
    }

    public Pizza toGamePizza() {
        Pizza pizza = new Pizza();
        if (hasSauce) {
            pizza.addSauce();
        }
        if (hasCheese) {
            pizza.addCheese();
        }
        for (String topping : toppings) {
            pizza.addTopping(topping);
        }
        if (baked) {
            pizza.bake();
        }
        return pizza;
    }

    public boolean hasDough() {
        return hasDough;
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
}

