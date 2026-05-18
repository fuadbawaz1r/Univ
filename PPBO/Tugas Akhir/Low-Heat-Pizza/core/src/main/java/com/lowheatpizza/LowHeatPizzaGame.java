package com.lowheatpizza;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lowheatpizza.managers.GameAssets;
import com.lowheatpizza.managers.AudioManager;
import com.lowheatpizza.managers.GameSession;
import com.lowheatpizza.managers.SaveManager;
import com.lowheatpizza.screens.KitchenScreen;
import com.lowheatpizza.screens.MainMenuScreen;
import com.lowheatpizza.screens.ShopScreen;

public class LowHeatPizzaGame extends Game {
    private SpriteBatch batch;
    private GameAssets assets;
    private AudioManager audioManager;
    private GameSession session;
    private SaveManager saveManager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        assets = new GameAssets();
        assets.load();
        audioManager = new AudioManager();

        saveManager = new SaveManager();
        session = new GameSession();
        session.resetForNewGame();

        String debugScreen = System.getProperty("lowheat.screen", "menu").toLowerCase();
        try {
            switch (debugScreen) {
                case "shop":
                    setScreen(new ShopScreen(this));
                    break;
                case "kitchen":
                    prepareDebugKitchenSession();
                    setScreen(new KitchenScreen(this));
                    break;
                default:
                    setScreen(new MainMenuScreen(this));
                    break;
            }
        } catch (Exception exception) {
            Gdx.app.error("LowHeatPizzaGame", "Failed to open initial screen: " + debugScreen, exception);
            setScreen(new MainMenuScreen(this));
        }
    }

    private void prepareDebugKitchenSession() {
        if (session.getCurrentCustomer() == null) {
            session.setCurrentCustomer(session.getCustomerManager().nextCustomer(session.getPlayer(), session.getToppingManager()));
            session.setCurrentOrder(session.getCurrentCustomer().getOrder());
        }
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public GameAssets getAssets() {
        return assets;
    }

    public GameSession getSession() {
        return session;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public SaveManager getSaveManager() {
        return saveManager;
    }

    @Override
    public void dispose() {
        super.dispose();
        audioManager.dispose();
        assets.dispose();
        batch.dispose();
    }
}

