package com.lowheatpizza.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.lowheatpizza.progress.IngredientProgress;

public class UnlockPopup extends Table {
    public UnlockPopup(Skin skin, Array<IngredientProgress> ingredients, Runnable onClose) {
        setBackground(skin.getDrawable("speech_bubble_panel"));
        pad(28);
        defaults().pad(10);

        add(new Label("New topping unlocked!", skin, "title")).row();
        for (IngredientProgress ingredient : ingredients) {
            add(new Label(ingredient.displayName + " available for $" + ingredient.price, skin, "dark")).row();
        }

        TextButton continueButton = buildPopupButton("Continue", skin);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
                onClose.run();
            }
        });
        add(continueButton).width(240).height(66).padTop(16);
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

