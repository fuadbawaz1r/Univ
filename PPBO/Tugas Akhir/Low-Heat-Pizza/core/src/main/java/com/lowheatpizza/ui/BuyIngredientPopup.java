package com.lowheatpizza.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lowheatpizza.progress.IngredientProgress;

public class BuyIngredientPopup extends Table {
    public BuyIngredientPopup(Skin skin, IngredientProgress ingredient, Runnable onConfirm) {
        setBackground(skin.getDrawable("speech_bubble_panel"));
        pad(28);
        defaults().pad(12);

        Label message = new Label("Buy " + ingredient.displayName + " for $" + ingredient.price + "?", skin, "dark");
        add(message).colspan(2).row();

        TextButton yesButton = buildPopupButton("Yes", skin);
        yesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
                onConfirm.run();
            }
        });

        TextButton noButton = buildPopupButton("No", skin);
        noButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
            }
        });

        add(yesButton).width(180).height(62);
        add(noButton).width(180).height(62);
    }

    private TextButton buildPopupButton(String text, Skin skin) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = skin.getFont("buttonFont");
        style.fontColor = Color.valueOf("4A2419");
        style.downFontColor = Color.valueOf("4A2419");
        style.up = skin.getDrawable("button_response");
        style.down = skin.getDrawable("button_response");
        style.over = skin.getDrawable("button_response");
        TextButton button = new TextButton(text, style);
        button.getLabel().setFontScale(0.62f);
        return button;
    }
}

