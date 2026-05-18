package com.lowheatpizza.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.lowheatpizza.LowHeatPizzaGame;

public class LowHeatLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Low Heat Pizza");
        configuration.setWindowedMode(1280, 720);
        configuration.useVsync(true);
        configuration.setForegroundFPS(60);
        new Lwjgl3Application(new LowHeatPizzaGame(), configuration);
    }
}

