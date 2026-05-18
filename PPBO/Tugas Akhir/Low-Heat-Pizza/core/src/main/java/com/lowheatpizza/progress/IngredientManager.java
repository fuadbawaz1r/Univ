package com.lowheatpizza.progress;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.lowheatpizza.utils.AssetPaths;

public class IngredientManager {
    private final Array<IngredientProgress> ingredients = new Array<>();
    private final ObjectMap<String, IngredientProgress> byId = new ObjectMap<>();

    public IngredientManager() {
        loadConfig();
    }

    private void loadConfig() {
        FileHandle handle = Gdx.files.internal(AssetPaths.INGREDIENT_CONFIG);
        if (!handle.exists()) {
            throw new IllegalStateException("Missing ingredient config: " + AssetPaths.INGREDIENT_CONFIG);
        }

        IngredientConfig config = new Json().fromJson(IngredientConfig.class, handle);
        ingredients.clear();
        byId.clear();
        for (IngredientProgress ingredient : config.ingredients) {
            ingredient.owned = ingredient.ownedByDefault;
            ingredients.add(ingredient);
            byId.put(ingredient.id, ingredient);
        }
    }

    public IngredientProgress findById(String ingredientId) {
        return byId.get(ingredientId);
    }

    public Array<IngredientProgress> getIngredients() {
        return ingredients;
    }

    public Array<IngredientProgress> getNewlyUnlockedIngredients(int previousLevel, int newLevel) {
        Array<IngredientProgress> result = new Array<>();
        for (IngredientProgress ingredient : ingredients) {
            if (!ingredient.owned && ingredient.unlockLevel > previousLevel && ingredient.unlockLevel <= newLevel) {
                result.add(ingredient);
            }
        }
        return result;
    }

    public boolean canUseIngredient(String ingredientId, int playerLevel) {
        IngredientProgress ingredient = findById(ingredientId);
        return ingredient != null && ingredient.getStatus(playerLevel) == IngredientStatus.OWNED;
    }

    public boolean canBuyIngredient(String ingredientId, int playerLevel, int money) {
        IngredientProgress ingredient = findById(ingredientId);
        return ingredient != null
            && ingredient.getStatus(playerLevel) == IngredientStatus.AVAILABLE_TO_BUY
            && money >= ingredient.price;
    }

    public boolean buyIngredient(String ingredientId, PlayerProgress playerProgress) {
        IngredientProgress ingredient = findById(ingredientId);
        if (ingredient == null || !canBuyIngredient(ingredientId, playerProgress.getLevel(), playerProgress.getMoney())) {
            return false;
        }
        playerProgress.subtractMoney(ingredient.price);
        ingredient.owned = true;
        return true;
    }

    public Array<String> getOwnedIngredientIds() {
        Array<String> result = new Array<>();
        for (IngredientProgress ingredient : ingredients) {
            if (ingredient.owned) {
                result.add(ingredient.id);
            }
        }
        return result;
    }

    public void applyOwnedIngredientIds(Array<String> ownedIngredients) {
        for (IngredientProgress ingredient : ingredients) {
            ingredient.owned = ingredient.ownedByDefault;
        }
        if (ownedIngredients == null) {
            return;
        }
        for (String ingredientId : ownedIngredients) {
            IngredientProgress ingredient = findById(ingredientId);
            if (ingredient != null) {
                ingredient.owned = true;
            }
        }
    }

    private static class IngredientConfig {
        public Array<IngredientProgress> ingredients = new Array<>();
    }
}

