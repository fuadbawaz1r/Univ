package com.lowheatpizza.progress;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.lowheatpizza.managers.GameSession;

public class ProgressSaveManager {
    private final Json json = new Json();

    public String toJson(GameSession session) {
        SaveData data = new SaveData();
        data.playerLevel = session.getPlayer().getLevel();
        data.currentExp = session.getPlayer().getExp();
        data.money = session.getPlayer().getMoney();
        data.ownedIngredients = session.getIngredientManager().getOwnedIngredientIds();
        return json.toJson(data);
    }

    public static class SaveData {
        public int playerLevel;
        public int currentExp;
        public int money;
        public Array<String> ownedIngredients = new Array<>();
    }
}

