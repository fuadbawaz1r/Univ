package com.lowheatpizza.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.lowheatpizza.utils.AssetPaths;

public class GameAssets implements Disposable {
    private final AssetManager manager = new AssetManager();
    private final ObjectMap<String, Texture> fallbackTextures = new ObjectMap<>();
    private final ObjectMap<String, Texture> externalTextures = new ObjectMap<>();

    public void load() {
        loadIfPresent(AssetPaths.MAIN_MENU_BG);
        loadIfPresent(AssetPaths.LOGO);
        loadIfPresent(AssetPaths.SHOP_BG);
        loadIfPresent(AssetPaths.SHOP_FOREGROUND);
        loadIfPresent(AssetPaths.UI_SPEECH_BUBBLE);
        loadIfPresent(AssetPaths.KITCHEN_BG);
        loadIfPresent(AssetPaths.KITCHEN_BG_NUMBERED);
        loadIfPresent(AssetPaths.KITCHEN_BG_CLEAN);
        loadIfPresent(AssetPaths.KITCHEN_FLOOR_BG);
        loadIfPresent(AssetPaths.KITCHEN_ELEMENTS);
        loadIfPresent(AssetPaths.KITCHEN_STATION_DOUGH);
        loadIfPresent(AssetPaths.KITCHEN_STATION_SAUCE);
        loadIfPresent(AssetPaths.KITCHEN_STATION_CHEESE);
        loadIfPresent(AssetPaths.KITCHEN_STATION_PEPPERONI);
        loadIfPresent(AssetPaths.KITCHEN_PIZZA_BOARD);
        loadIfPresent(AssetPaths.KITCHEN_STATION_MUSHROOM_LOCKED);
        loadIfPresent(AssetPaths.KITCHEN_STATION_ONION_LOCKED);
        loadIfPresent(AssetPaths.KITCHEN_STATION_FISH_LOCKED);
        loadIfPresent(AssetPaths.KITCHEN_PADLOCK);
        loadIfPresent(AssetPaths.CUSTOMER_ONE);
        loadIfPresent(AssetPaths.CUSTOMER_TWO);
        loadIfPresent(AssetPaths.CUSTOMER_THREE);
        loadIfPresent(AssetPaths.COUNTER);
        loadIfPresent(AssetPaths.DOUGH);
        loadIfPresent(AssetPaths.SAUCE_LAYER);
        loadIfPresent(AssetPaths.CHEESE_LAYER);
        loadIfPresent(AssetPaths.TOPPING_PEPPERONI);
        loadIfPresent(AssetPaths.TOPPING_MUSHROOM);
        loadIfPresent(AssetPaths.TOPPING_ONION);
        loadIfPresent(AssetPaths.TOPPING_BELL_PEPPER);
        loadIfPresent(AssetPaths.TOPPING_SAUSAGE);
        loadIfPresent(AssetPaths.UI_MONEY_BAR);
        loadIfPresent(AssetPaths.UI_CUSTOMER_TEXT);
        loadIfPresent(AssetPaths.UI_ICON_SUN);
        loadIfPresent(AssetPaths.UI_ICON_MONEY);
        manager.finishLoading();
    }

    private void loadIfPresent(String path) {
        if (Gdx.files.internal(path).exists()) {
            manager.load(path, Texture.class);
        }
    }

    private String resolveExternalPath(String path) {
        if (path == null) return null;

        // 1. If it's already an external/absolute path or relative path to assets root:
        if (path.contains("assest-low-heat-pizza") || new java.io.File(path).exists()) {
            return new java.io.File(path).getAbsolutePath();
        }

        // 2. Map internal paths to their external filenames in the assets directory:
        String filename = new java.io.File(path).getName();

        // Try locating in assest-low-heat-pizza/halaman-game/
        java.io.File gamePageFile = new java.io.File(AssetPaths.PROJECT_ASSETS_ROOT, "halaman-game/" + filename);
        if (gamePageFile.exists()) {
            return gamePageFile.getAbsolutePath();
        }

        // Specific name mapping overrides:
        if (path.equals(AssetPaths.SHOP_BG)) {
            java.io.File bgFile = new java.io.File(AssetPaths.PROJECT_ASSETS_ROOT, "halaman-game/background_restaurant.png");
            if (bgFile.exists()) return bgFile.getAbsolutePath();
        }
        if (path.equals(AssetPaths.MAIN_MENU_BG)) {
            java.io.File bgFile = new java.io.File(AssetPaths.PROJECT_ASSETS_ROOT, "halaman-game/main-menu-page.png");
            if (bgFile.exists()) return bgFile.getAbsolutePath();
        }
        if (path.equals(AssetPaths.KITCHEN_FLOOR_BG)) {
            java.io.File bgFile = new java.io.File(AssetPaths.PROJECT_ASSETS_ROOT, "halaman-game/Floors.png");
            if (bgFile.exists()) return bgFile.getAbsolutePath();
        }
        if (path.equals(AssetPaths.LOGO)) {
            java.io.File logoFile = new java.io.File(AssetPaths.PROJECT_ASSETS_ROOT, "logo-game/logo-loka-loka-pizza.png");
            if (logoFile.exists()) return logoFile.getAbsolutePath();
        }
        if (path.equals(AssetPaths.UI_SPEECH_BUBBLE)) {
            java.io.File bubbleFile = new java.io.File(AssetPaths.PROJECT_ASSETS_ROOT, "halaman-game/bubble_chat.png");
            if (bubbleFile.exists()) return bubbleFile.getAbsolutePath();
        }
        if (path.equals(AssetPaths.UI_MONEY_BAR)) {
            java.io.File moneyFile = new java.io.File(AssetPaths.PROJECT_ASSETS_ROOT, "elemen/bar-uang.png");
            if (moneyFile.exists()) return moneyFile.getAbsolutePath();
        }
        if (path.equals(AssetPaths.UI_CUSTOMER_TEXT)) {
            java.io.File textFile = new java.io.File(AssetPaths.PROJECT_ASSETS_ROOT, "elemen/text-pelanggan.png");
            if (textFile.exists()) return textFile.getAbsolutePath();
        }

        return null;
    }

    public Texture getTexture(String path) {
        String resolvedExternal = resolveExternalPath(path);
        if (resolvedExternal != null) {
            return getExternalTexture(resolvedExternal);
        }
        if (manager.isLoaded(path, Texture.class)) {
            return manager.get(path, Texture.class);
        }
        if (!fallbackTextures.containsKey(path)) {
            fallbackTextures.put(path, createFallbackTexture(path));
        }
        return fallbackTextures.get(path);
    }

    private Texture getExternalTexture(String path) {
        if (!externalTextures.containsKey(path)) {
            externalTextures.put(path, new Texture(Gdx.files.absolute(path)));
        }
        return externalTextures.get(path);
    }

    private boolean isAbsolutePath(String path) {
        return path != null && path.matches("^[A-Za-z]:\\\\.*");
    }

    private Texture createFallbackTexture(String path) {
        Pixmap pixmap = new Pixmap(8, 8, Pixmap.Format.RGBA8888);
        Color color = Color.DARK_GRAY.cpy();
        if (path.contains("main_menu")) {
            color = Color.valueOf("AD412D");
        } else if (path.contains("shop") || path.contains("kasir")) {
            color = Color.valueOf("D9A35F");
        } else if (path.contains("kitchen") || path.contains("dough")) {
            color = Color.valueOf("E7C98B");
        } else if (path.contains("sauce")) {
            color = Color.valueOf("B13A2B");
        } else if (path.contains("cheese")) {
            color = Color.valueOf("F4D35E");
        } else if (path.contains("pepperoni")) {
            color = Color.valueOf("8F2D1D");
        } else if (path.contains("mushroom")) {
            color = Color.valueOf("A1866F");
        } else if (path.contains("onion")) {
            color = Color.valueOf("B992D6");
        } else if (path.contains("bell")) {
            color = Color.valueOf("4C9A2A");
        } else if (path.contains("sausage")) {
            color = Color.valueOf("7B4A2E");
        }
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    @Override
    public void dispose() {
        manager.dispose();
        for (Texture texture : fallbackTextures.values()) {
            texture.dispose();
        }
        fallbackTextures.clear();
        for (Texture texture : externalTextures.values()) {
            texture.dispose();
        }
        externalTextures.clear();
    }
}

