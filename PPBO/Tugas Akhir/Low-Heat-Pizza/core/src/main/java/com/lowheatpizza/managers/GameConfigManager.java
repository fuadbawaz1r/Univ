package com.lowheatpizza.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.Json;
import com.lowheatpizza.utils.AssetPaths;

import java.io.File;

public class GameConfigManager {
    public static final int EXP_PER_LEVEL = 30;
    private final ConfigRoot configRoot;
    private final IntIntMap expThresholds = new IntIntMap();

    public GameConfigManager() {
        configRoot = loadConfig();
        indexExpThresholds();
        validateCustomerTextures();
    }

    public int getStartingLevel() {
        return configRoot.levelSystem.startingLevel;
    }

    public int getStartingExp() {
        return configRoot.levelSystem.startingExp;
    }

    public int getExpToNextLevel(int level) {
        if (level == 1 || level == 2) {
            return 30;
        } else if (level == 3 || level == 4) {
            return 50;
        } else {
            return 60;
        }
    }

    public Array<ToppingConfig> getToppings() {
        return configRoot.toppings;
    }

    public Array<CustomerConfig> getCustomers() {
        return configRoot.customers;
    }

    public int getVirtualWidth() {
        return configRoot.virtualResolution.width;
    }

    public int getVirtualHeight() {
        return configRoot.virtualResolution.height;
    }

    public String getCustomerRenderMode() {
        return configRoot.customerRenderMode;
    }

    private ConfigRoot loadConfig() {
        File absoluteFile = new File(AssetPaths.CUSTOMER_CONFIG).getAbsoluteFile();
        FileHandle fileHandle = Gdx.files.absolute(absoluteFile.getAbsolutePath());
        if (!fileHandle.exists()) {
            throw new IllegalStateException("Missing customer config: " + absoluteFile.getAbsolutePath());
        }
        ConfigRoot root = new Json().fromJson(ConfigRoot.class, fileHandle);
        for (CustomerConfig customer : root.customers) {
            if (customer.texturePath != null) {
                int index = customer.texturePath.indexOf("assest-low-heat-pizza");
                if (index != -1) {
                    String subPath = customer.texturePath.substring(index + "assest-low-heat-pizza".length());
                    customer.texturePath = AssetPaths.PROJECT_ASSETS_ROOT + subPath;
                }
            }
        }
        return root;
    }

    private void indexExpThresholds() {
        for (String levelKey : configRoot.levelSystem.expToNextLevel.keySet()) {
            expThresholds.put(
                Integer.parseInt(levelKey),
                configRoot.levelSystem.expToNextLevel.getOrDefault(levelKey, 100f).intValue()
            );
        }
    }

    private void validateCustomerTextures() {
        File root = new File(AssetPaths.PROJECT_ASSETS_ROOT).getAbsoluteFile();
        for (CustomerConfig customer : configRoot.customers) {
            File textureFile = new File(customer.texturePath).getAbsoluteFile();
            if (!textureFile.exists()) {
                throw new IllegalStateException("Missing customer texture: " + customer.texturePath);
            }
            if (!textureFile.toPath().normalize().startsWith(root.toPath().normalize())) {
                throw new IllegalStateException("Customer texture is outside assets folder: " + customer.texturePath);
            }
        }
    }

    public static class ConfigRoot {
        public int version;
        public VirtualResolution virtualResolution;
        public String customerRenderMode;
        public LevelSystemConfig levelSystem;
        public Array<ToppingConfig> toppings = new Array<>();
        public Array<CustomerConfig> customers = new Array<>();
    }

    public static class VirtualResolution {
        public int width;
        public int height;
    }

    public static class LevelSystemConfig {
        public int startingLevel;
        public int startingExp;
        public IntMapWrapper expToNextLevel;
    }

    public static class ToppingConfig {
        public String id;
        public String displayName;
        public int unlockLevel;
        public int purchasePrice;
        public boolean ownedByDefault;
    }

    public static class CustomerConfig {
        public String id;
        public String displayName;
        public String texturePath;
        public int availableFromLevel;
        public Array<OrderConfig> orders = new Array<>();
    }

    public static class OrderConfig {
        public String id;
        public int availableFromLevel;
        public String dialogue;
        public String clarificationDialogue;
        public Array<String> requiredToppings = new Array<>();
        public RewardConfig reward;
    }

    public static class RewardConfig {
        public int money;
        public int exp;
    }

    public static class IntMapWrapper extends java.util.HashMap<String, Number> {
    }
}

