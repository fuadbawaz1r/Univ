package com.lowheatpizza.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.lowheatpizza.managers.GameConfigManager.ToppingConfig;
import com.lowheatpizza.models.Player;
import com.lowheatpizza.models.Topping;
import com.lowheatpizza.models.ToppingStatus;
import com.lowheatpizza.progress.IngredientManager;
import com.lowheatpizza.progress.IngredientProgress;
import com.lowheatpizza.progress.IngredientStatus;
import com.lowheatpizza.utils.AssetPaths;

public class ToppingManager {
    private final Array<Topping> toppings = new Array<>();
    private final ObjectMap<String, Topping> byId = new ObjectMap<>();
    private final GameConfigManager configManager;

    public ToppingManager(GameConfigManager configManager) {
        this.configManager = configManager;
        loadFromConfig();
    }

    private void loadFromConfig() {
        toppings.clear();
        byId.clear();
        for (ToppingConfig item : configManager.getToppings()) {
            ToppingStatus initialStatus = item.ownedByDefault
                ? ToppingStatus.OWNED
                : (configManager.getStartingLevel() >= item.unlockLevel ? ToppingStatus.UNLOCKED : ToppingStatus.LOCKED);
            Topping topping = new Topping(
                item.id,
                item.displayName,
                item.purchasePrice,
                item.unlockLevel,
                initialStatus,
                resolveAssetPath(item.id)
            );
            toppings.add(topping);
            byId.put(topping.getId(), topping);
        }
    }

    public void resetStatuses() {
        for (Topping topping : toppings) {
            topping.setStatus(topping.getInitialStatus());
        }
        refreshUnlocks(configManager.getStartingLevel());
    }

    public void refreshUnlocks(int playerLevel) {
        for (Topping topping : toppings) {
            if (topping.getStatus() == ToppingStatus.LOCKED && topping.getRequiredLevel() <= playerLevel) {
                topping.setStatus(ToppingStatus.UNLOCKED);
            }
        }
    }

    public void syncFromIngredients(IngredientManager ingredientManager, int playerLevel) {
        for (Topping topping : toppings) {
            IngredientProgress ingredient = ingredientManager.findById(topping.getId());
            if (ingredient == null) {
                continue;
            }
            IngredientStatus status = ingredient.getStatus(playerLevel);
            if (status == IngredientStatus.OWNED) {
                topping.setStatus(ToppingStatus.OWNED);
            } else if (status == IngredientStatus.AVAILABLE_TO_BUY) {
                topping.setStatus(ToppingStatus.UNLOCKED);
            } else {
                topping.setStatus(ToppingStatus.LOCKED);
            }
        }
    }

    public boolean buyTopping(String toppingId, Player player) {
        Topping topping = byId.get(toppingId);
        if (topping == null || topping.getStatus() != ToppingStatus.UNLOCKED || !player.canAfford(topping.getPrice())) {
            return false;
        }
        player.spendMoney(topping.getPrice());
        topping.setStatus(ToppingStatus.OWNED);
        return true;
    }

    public Array<Topping> getAllToppings() {
        return toppings;
    }

    public Array<Topping> getOwnedToppings() {
        Array<Topping> result = new Array<>();
        for (Topping topping : toppings) {
            if (topping.getStatus() == ToppingStatus.OWNED) {
                result.add(topping);
            }
        }
        return result;
    }

    public Array<Topping> getOwnedExtraToppings() {
        Array<Topping> result = new Array<>();
        for (Topping topping : getOwnedToppings()) {
            if (!"sauce".equals(topping.getId()) && !"cheese".equals(topping.getId())) {
                result.add(topping);
            }
        }
        return result;
    }

    public Topping getById(String toppingId) {
        return byId.get(toppingId);
    }

    public Array<String> getOwnedToppingIds() {
        Array<String> result = new Array<>();
        for (Topping topping : toppings) {
            if (topping.getStatus() == ToppingStatus.OWNED) {
                result.add(topping.getId());
            }
        }
        return result;
    }

    public void applyStatuses(ObjectMap<String, String> statuses) {
        for (ObjectMap.Entry<String, String> entry : statuses.entries()) {
            Topping topping = byId.get(entry.key);
            if (topping != null) {
                topping.setStatus(ToppingStatus.valueOf(entry.value));
            }
        }
    }

    public ObjectMap<String, String> getStatusSnapshot() {
        ObjectMap<String, String> snapshot = new ObjectMap<>();
        for (Topping topping : toppings) {
            snapshot.put(topping.getId(), topping.getStatus().name());
        }
        return snapshot;
    }

    private String resolveAssetPath(String toppingId) {
        switch (toppingId) {
            case "sauce":
                return AssetPaths.SAUCE_LAYER;
            case "cheese":
                return AssetPaths.CHEESE_LAYER;
            case "pepperoni":
                return AssetPaths.TOPPING_PEPPERONI;
            case "mushroom":
                return AssetPaths.TOPPING_MUSHROOM;
            case "onion":
                return AssetPaths.TOPPING_ONION;
            case "bell_pepper":
                return AssetPaths.TOPPING_BELL_PEPPER;
            case "sausage":
                return AssetPaths.TOPPING_SAUSAGE;
            default:
                return AssetPaths.DOUGH;
        }
    }
}

