package com.lowheatpizza.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lowheatpizza.LowHeatPizzaGame;
import com.lowheatpizza.ui.UiFactory;

public abstract class BaseScreen extends ScreenAdapter {
    protected static final float VIRTUAL_WIDTH = 1920f;
    protected static final float VIRTUAL_HEIGHT = 1080f;
    protected final LowHeatPizzaGame game;
    protected final Stage worldStage;
    protected final Stage uiStage;
    protected final Skin skin;
    protected final Table root;
    private final UiFactory uiFactory;

    protected BaseScreen(LowHeatPizzaGame game, String backgroundPath) {
        this.game = game;
        this.worldStage = new Stage(new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT), game.getBatch());
        this.uiStage = new Stage(new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT), game.getBatch());
        this.uiFactory = new UiFactory(game.getAssets());
        this.skin = uiFactory.getSkin();
        this.root = new Table();
        root.setFillParent(true);
        if (backgroundPath != null) {
            root.setBackground(new TextureRegionDrawable(new TextureRegion(game.getAssets().getTexture(backgroundPath))));
        }
        worldStage.addActor(root);
    }

    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uiStage);
        multiplexer.addProcessor(worldStage);
        Gdx.input.setInputProcessor(multiplexer);

        Image overlay = new Image(skin.getDrawable("white"));
        overlay.setColor(0, 0, 0, 1);
        overlay.setSize(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        uiStage.addActor(overlay);
        overlay.addAction(com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence(
            com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut(0.4f),
            com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor()
        ));
    }

    public void transitionTo(final com.badlogic.gdx.Screen target) {
        Image overlay = new Image(skin.getDrawable("white"));
        overlay.setColor(0, 0, 0, 0);
        overlay.setSize(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        uiStage.addActor(overlay);
        
        game.getAudioManager().playSound(com.lowheatpizza.utils.AssetPaths.AUDIO_PAGE_SWITCH, 0.7f);
        overlay.addAction(com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence(
            com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn(0.4f),
            com.badlogic.gdx.scenes.scene2d.actions.Actions.run(() -> game.setScreen(target))
        ));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.12f, 0.08f, 0.06f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        worldStage.act(delta);
        worldStage.draw();
        
        game.getBatch().setColor(1, 1, 1, 1);
        uiStage.act(delta);
        uiStage.draw();
    }

    protected Image buildImage(String path, float width, float height) {
        Image image = new Image(game.getAssets().getTexture(path));
        image.setSize(width, height);
        return image;
    }

    protected Table createPanel(String colorHex, float alpha, int pad) {
        Table panel = new Table();
        panel.pad(pad);
        Color base = Color.valueOf(colorHex);
        TextureRegionDrawable white = (TextureRegionDrawable) skin.getDrawable("white");
        panel.setBackground(white.tint(new Color(base.r, base.g, base.b, alpha)));
        return panel;
    }

    protected Label buildLabel(String text, String styleName) {
        return new Label(text, skin, styleName);
    }

    @Override
    public void resize(int width, int height) {
        worldStage.getViewport().update(width, height, true);
        uiStage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        worldStage.dispose();
        uiStage.dispose();
        uiFactory.dispose();
    }
}

