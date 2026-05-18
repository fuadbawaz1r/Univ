package com.lowheatpizza.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lowheatpizza.LowHeatPizzaGame;
import com.lowheatpizza.utils.AssetPaths;

public class MainMenuScreen extends BaseScreen {
    private Label infoLabel;

    public MainMenuScreen(LowHeatPizzaGame game) {
        super(game, AssetPaths.MAIN_MENU_BG);
        buildUi();
    }

    @Override
    public void show() {
        super.show();
        game.getAudioManager().playLoop(AssetPaths.AUDIO_MAIN_MENU, 0.35f);
    }

    private void buildUi() {
        Table panel = new Table();
        panel.setBackground(skin.getDrawable("hud_panel"));
        panel.pad(34, 42, 34, 42);
        panel.defaults().pad(9).width(300).height(62);

        Image logo = buildImage(AssetPaths.LOGO, 360, 180);
        TextButton startButton = buildMenuButton("Start Game");
        TextButton continueButton = buildMenuButton("Continue");
        TextButton settingsButton = buildMenuButton("Settings");
        TextButton exitButton = buildMenuButton("Exit");
        infoLabel = buildLabel(game.getAudioManager().isEnabled() ? "Music: ON | SFX: ON" : "Music: OFF | SFX: OFF", "default");
        infoLabel.setFontScale(0.52f);
        infoLabel.setWrap(true);
        infoLabel.setAlignment(com.badlogic.gdx.utils.Align.center);

        continueButton.setDisabled(!game.getSaveManager().hasSave());

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getAudioManager().playSound(AssetPaths.AUDIO_CLICK_BUTTON, 0.75f);
                try {
                    game.getSaveManager().clear();
                    game.getSession().resetForNewGame();
                    transitionTo(new ShopScreen(game));
                } catch (Exception exception) {
                    showTransitionError("Start Game gagal: " + exception.getClass().getSimpleName());
                    exception.printStackTrace();
                }
            }
        });

        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getAudioManager().playSound(AssetPaths.AUDIO_CLICK_BUTTON, 0.75f);
                try {
                    if (game.getSaveManager().loadInto(game.getSession())) {
                        transitionTo(new ShopScreen(game));
                    } else {
                        showTransitionError("Belum ada save untuk dilanjutkan.");
                    }
                } catch (Exception exception) {
                    showTransitionError("Continue gagal: " + exception.getClass().getSimpleName());
                    exception.printStackTrace();
                }
            }
        });

        settingsButton.addListener(new ClickListener() {
            private boolean enabled = true;

            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getAudioManager().playSound(AssetPaths.AUDIO_CLICK_BUTTON, 0.75f);
                enabled = !enabled;
                game.getAudioManager().setEnabled(enabled);
                if (enabled) {
                    game.getAudioManager().playLoop(AssetPaths.AUDIO_MAIN_MENU, 0.35f);
                }
                infoLabel.setText(enabled ? "Music: ON | SFX: ON" : "Music: OFF | SFX: OFF");
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getAudioManager().playSound(AssetPaths.AUDIO_CLICK_BUTTON, 0.75f);
                com.badlogic.gdx.Gdx.app.exit();
            }
        });

        panel.add(logo).width(360).height(180).padBottom(16).row();
        panel.add(startButton).row();
        panel.add(continueButton).row();
        panel.add(settingsButton).row();
        panel.add(exitButton).row();
        panel.add(infoLabel).width(360).padTop(16);

        root.center();
        root.add(panel);
    }

    private TextButton buildMenuButton(String text) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = skin.getFont("buttonFont");
        style.fontColor = Color.valueOf("4A2419");
        style.downFontColor = Color.valueOf("4A2419");
        style.disabledFontColor = Color.valueOf("8A746C");
        style.up = skin.getDrawable("button_response");
        style.down = skin.getDrawable("button_response");
        style.over = skin.getDrawable("button_response");
        style.disabled = skin.getDrawable("button_response");
        TextButton button = new TextButton(text, style);
        button.getLabel().setFontScale(0.58f);
        return button;
    }

    private void showTransitionError(String message) {
        if (infoLabel != null) {
            infoLabel.setText(message);
        }
    }
}

