package com.lowheatpizza.systems;

import com.lowheatpizza.models.Player;

public class EconomySystem {
    public void grantMoney(Player player, int amount) {
        player.addMoney(amount);
    }
}

