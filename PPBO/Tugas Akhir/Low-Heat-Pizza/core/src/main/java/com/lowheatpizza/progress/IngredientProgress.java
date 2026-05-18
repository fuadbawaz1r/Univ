package com.lowheatpizza.progress;

public class IngredientProgress {
    public String id;
    public String displayName;
    public String type;
    public int unlockLevel;
    public int price;
    public boolean owned;
    public boolean ownedByDefault;
    public String stationAsset;
    public String lockedAsset;
    public String toppingAsset;

    public IngredientStatus getStatus(int playerLevel) {
        if (owned) {
            return IngredientStatus.OWNED;
        }
        if (playerLevel >= unlockLevel) {
            return IngredientStatus.AVAILABLE_TO_BUY;
        }
        return IngredientStatus.LOCKED_BY_LEVEL;
    }
}

