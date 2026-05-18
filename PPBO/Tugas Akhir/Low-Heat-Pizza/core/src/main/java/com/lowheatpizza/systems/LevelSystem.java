package com.lowheatpizza.systems;

import com.lowheatpizza.managers.GameConfigManager;
import com.lowheatpizza.models.Player;

public class LevelSystem {
    private final GameConfigManager configManager;

    public LevelSystem(GameConfigManager configManager) {
        this.configManager = configManager;
    }

    public int grantExp(Player player, int exp) {
        player.addExp(exp);
        return normalizeProgress(player);
    }

    public int normalizeProgress(Player player) {
        player.setExpToNextLevel(configManager.getExpToNextLevel(player.getLevel()));
        int levelsGained = 0;
        while (player.getExp() >= player.getExpToNextLevel()) {
            player.setExp(player.getExp() - player.getExpToNextLevel());
            player.setLevel(player.getLevel() + 1);
            player.setExpToNextLevel(configManager.getExpToNextLevel(player.getLevel()));
            levelsGained++;
        }
        return levelsGained;
    }
}

