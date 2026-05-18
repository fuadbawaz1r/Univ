package com.lowheatpizza.progress;

import com.lowheatpizza.models.Player;

public class PlayerProgress {
    private final Player player;

    public PlayerProgress(Player player) {
        this.player = player;
    }

    public int getLevel() {
        return player.getLevel();
    }

    public int getMoney() {
        return player.getMoney();
    }

    public void subtractMoney(int amount) {
        player.spendMoney(amount);
    }
}

