package com.lowheatpizza.systems;

import com.badlogic.gdx.utils.Array;
import com.lowheatpizza.models.EvaluationResult;
import com.lowheatpizza.models.Order;
import com.lowheatpizza.models.Pizza;

public class OrderEvaluator {
    public EvaluationResult evaluate(Order order, Pizza pizza) {
        Array<String> servedToppings = new Array<>();
        if (pizza.hasSauce()) {
            servedToppings.add("sauce");
        }
        if (pizza.hasCheese()) {
            servedToppings.add("cheese");
        }
        servedToppings.addAll(pizza.getToppings());

        int correctToppings = 0;
        for (String topping : order.getRequiredToppings()) {
            if (servedToppings.contains(topping, false)) {
                correctToppings++;
            }
        }
        boolean exactMatch = servedToppings.size == order.getRequiredToppings().size;
        boolean perfect = pizza.isBaked() && exactMatch && correctToppings == order.getRequiredToppings().size;

        String message;
        if (perfect) {
            message = "Order sesuai. Pelanggan menerima pizza sesuai pesanan.";
        } else if (!pizza.isBaked()) {
            message = "Pizza belum dibake. Pesanan gagal disajikan.";
        } else {
            message = "Pesanan tidak sesuai. Topping benar: " + correctToppings + "/" + order.getRequiredToppings().size + ".";
        }
        return new EvaluationResult(
            perfect ? order.getRewardMoney() : 0,
            perfect ? order.getRewardExp() : 0,
            perfect,
            message
        );
    }
}

