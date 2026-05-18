package com.lowheatpizza.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.lowheatpizza.models.Player;
import com.lowheatpizza.progress.ProgressSaveManager;

public class SaveManager {
    private static final String PREFS_NAME = "low-heat-pizza-save";
    private static final String SAVE_FILE_NAME = "save_data.json";
    private final Preferences preferences = Gdx.app.getPreferences(PREFS_NAME);
    private final Json json = new Json();
    private final ProgressSaveManager progressSaveManager = new ProgressSaveManager();

    public boolean hasSave() {
        return preferences.contains("player");
    }

    public void save(GameSession session) {
        SaveData data = new SaveData();
        Player player = session.getPlayer();
        data.money = player.getMoney();
        data.level = player.getLevel();
        data.exp = player.getExp();
        data.expToNextLevel = player.getExpToNextLevel();
        data.currentDay = session.getCurrentDay();
        data.toppingStatuses = session.getToppingManager().getStatusSnapshot();
        data.ownedIngredients = session.getIngredientManager().getOwnedIngredientIds();

        preferences.putString("player", json.toJson(data));
        preferences.flush();
        Gdx.files.local(SAVE_FILE_NAME).writeString(progressSaveManager.toJson(session), false);
    }

    public boolean loadInto(GameSession session) {
        if (!hasSave()) {
            return false;
        }
        SaveData data = json.fromJson(SaveData.class, preferences.getString("player"));
        session.resetForNewGame();
        session.getPlayer().setProgress(data.money, data.level, data.exp, session.getConfigManager().getExpToNextLevel(data.level));
        session.getLevelSystem().normalizeProgress(session.getPlayer());
        if (data.ownedIngredients != null && data.ownedIngredients.size > 0) {
            session.getIngredientManager().applyOwnedIngredientIds(data.ownedIngredients);
        } else {
            applyLegacyToppingOwnership(session, data.toppingStatuses);
        }
        session.syncToppingsFromIngredients();
        while (session.getCurrentDay() < data.currentDay) {
            session.advanceDay();
        }
        return true;
    }

    private void applyLegacyToppingOwnership(GameSession session, ObjectMap<String, String> toppingStatuses) {
        if (toppingStatuses == null) {
            return;
        }
        Array<String> ownedIngredients = new Array<>();
        for (ObjectMap.Entry<String, String> entry : toppingStatuses.entries()) {
            if ("OWNED".equals(entry.value)) {
                ownedIngredients.add(entry.key);
            }
        }
        session.getIngredientManager().applyOwnedIngredientIds(ownedIngredients);
    }

    public void clear() {
        preferences.clear();
        preferences.flush();
    }

    private static class SaveData {
        public int money;
        public int level;
        public int exp;
        public int expToNextLevel;
        public int currentDay;
        public ObjectMap<String, String> toppingStatuses = new ObjectMap<>();
        public Array<String> ownedIngredients = new Array<>();
    }
}

